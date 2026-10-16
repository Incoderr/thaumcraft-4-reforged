package thaumcraft.test;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import thaumcraft.Thaumcraft;
import thaumcraft.aspect.Aspect;
import thaumcraft.essentia.EssentiaHandler;
import thaumcraft.essentia.JarBlockEntity;
import thaumcraft.registry.ModBlocks;

@EventBusSubscriber(modid = Thaumcraft.MODID)
public final class EssentiaGameTests {
    private static final DeferredRegister<Consumer<GameTestHelper>> FUNCTIONS = DeferredRegister.create(Registries.TEST_FUNCTION, Thaumcraft.MODID);
    private static final String[] TESTS = {"essentia_moves_through_tubes", "void_jar_voids_overflow", "essentia_cache_recovers_replaced_jars"};

    static {
        FUNCTIONS.register(TESTS[0], () -> EssentiaGameTests::essentiaMovesThroughTubes);
        FUNCTIONS.register(TESTS[1], () -> EssentiaGameTests::voidJarVoidsOverflow);
        FUNCTIONS.register(TESTS[2], () -> EssentiaGameTests::cacheRecoversReplacedJars);
    }

    private EssentiaGameTests() {
    }

    public static void register(IEventBus bus) {
        FUNCTIONS.register(bus);
    }

    @SubscribeEvent
    static void registerTests(RegisterGameTestsEvent event) {
        if (TESTS.length == 0) {
            return;
        }
        Holder<TestEnvironmentDefinition<?>> environment = event.registerEnvironment(
            Thaumcraft.id("essentia_default"),
            new TestEnvironmentDefinition.AllOf()
        );
        for (String name : TESTS) {
            ResourceKey<Consumer<GameTestHelper>> function = ResourceKey.create(Registries.TEST_FUNCTION, Thaumcraft.id(name));
            event.registerTest(
                Thaumcraft.id(name),
                new FunctionGameTestInstance(function, new TestData<>(environment, Thaumcraft.id("test/platform"), 200, 0, true))
            );
        }
    }

    private static void essentiaMovesThroughTubes(GameTestHelper helper) {
        BlockPos source = new BlockPos(1, 1, 1);
        BlockPos target = new BlockPos(4, 1, 1);
        helper.setBlock(source, ModBlocks.WARDED_JAR.get());
        helper.setBlock(target, ModBlocks.WARDED_JAR.get());
        for (int x = 1; x <= 4; x++) {
            helper.setBlock(new BlockPos(x, 2, 1), ModBlocks.ESSENTIA_TUBE.get());
        }
        JarBlockEntity sourceJar = helper.getBlockEntity(source, JarBlockEntity.class);
        JarBlockEntity targetJar = helper.getBlockEntity(target, JarBlockEntity.class);
        sourceJar.addToContainer(Aspect.AIR, 10);
        targetJar.setAspectFilter(Aspect.AIR);
        helper.succeedWhen(() -> {
            helper.assertTrue(targetJar.getAmount() >= 3, "essentia did not reach the second jar");
            helper.assertTrue(sourceJar.getAmount() + targetJar.getAmount() <= 10, "essentia was duplicated");
        });
    }

    private static void cacheRecoversReplacedJars(GameTestHelper helper) {
        BlockPos consumerPos = new BlockPos(3, 3, 3);
        BlockPos oldSource = new BlockPos(1, 1, 1);
        helper.setBlock(consumerPos, ModBlocks.WARDED_JAR.get());
        helper.setBlock(oldSource, ModBlocks.WARDED_JAR.get());
        JarBlockEntity consumer = helper.getBlockEntity(consumerPos, JarBlockEntity.class);
        helper.getBlockEntity(oldSource, JarBlockEntity.class).addToContainer(Aspect.MOTION, 8);
        helper.assertTrue(EssentiaHandler.drainEssentia(consumer, Aspect.MOTION, null, 12), "initial source was not cached");
        helper.setBlock(oldSource, Blocks.AIR);
        Aspect[] aspects = {Aspect.AIR, Aspect.MOTION, Aspect.ENERGY};
        BlockPos[] sources = {new BlockPos(0, 1, 3), new BlockPos(3, 1, 0), new BlockPos(6, 1, 3)};
        for (int index = 0; index < sources.length; index++) {
            helper.setBlock(sources[index], ModBlocks.WARDED_JAR.get());
            helper.getBlockEntity(sources[index], JarBlockEntity.class).addToContainer(aspects[index], 8);
        }
        EssentiaHandler.drainEssentia(consumer, Aspect.AIR, null, 12);
        helper.succeedWhen(() -> {
            for (Aspect aspect : aspects) {
                helper.assertTrue(EssentiaHandler.findEssentia(consumer, aspect, null, 12), "replacement source not found for " + aspect);
            }
            for (int index = 0; index < sources.length; index++) {
                helper.assertTrue(EssentiaHandler.drainEssentia(consumer, aspects[index], null, 12), "replacement source not drained for " + aspects[index]);
                helper.assertValueEqual(helper.getBlockEntity(sources[index], JarBlockEntity.class).getAmount(), 7, "replacement jar amount");
            }
        });
    }

    private static void voidJarVoidsOverflow(GameTestHelper helper) {
        BlockPos position = new BlockPos(1, 1, 1);
        helper.setBlock(position, ModBlocks.VOID_JAR.get());
        JarBlockEntity voidJar = helper.getBlockEntity(position, JarBlockEntity.class);
        helper.assertValueEqual(voidJar.addToContainer(Aspect.FIRE, 60), 0, "first add remainder");
        helper.assertValueEqual(voidJar.addToContainer(Aspect.FIRE, 20), 0, "overflow remainder");
        helper.assertValueEqual(voidJar.getAmount(), 64, "void jar amount");
        BlockPos plainPosition = new BlockPos(3, 1, 1);
        helper.setBlock(plainPosition, ModBlocks.WARDED_JAR.get());
        JarBlockEntity plainJar = helper.getBlockEntity(plainPosition, JarBlockEntity.class);
        plainJar.addToContainer(Aspect.FIRE, 60);
        helper.assertValueEqual(plainJar.addToContainer(Aspect.FIRE, 20), 16, "plain jar remainder");
        helper.succeed();
    }
}
