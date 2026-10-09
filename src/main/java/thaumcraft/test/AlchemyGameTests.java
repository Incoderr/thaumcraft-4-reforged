package thaumcraft.test;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import thaumcraft.Thaumcraft;
import thaumcraft.alchemy.CrucibleBlockEntity;
import thaumcraft.aspect.Aspect;
import thaumcraft.registry.ModBlocks;
import thaumcraft.registry.ModItems;
import thaumcraft.research.Knowledge;
import thaumcraft.wand.WandRegistry;
import thaumcraft.wand.WandTriggers;
import thaumcraft.wand.Wands;

@EventBusSubscriber(modid = Thaumcraft.MODID)
public final class AlchemyGameTests {
    private static final DeferredRegister<Consumer<GameTestHelper>> FUNCTIONS = DeferredRegister.create(Registries.TEST_FUNCTION, Thaumcraft.MODID);
    private static final String[] TESTS = {"crucible_recipe_output", "cauldron_to_crucible"};

    static {
        FUNCTIONS.register(TESTS[0], () -> AlchemyGameTests::crucibleRecipeOutput);
        FUNCTIONS.register(TESTS[1], () -> AlchemyGameTests::cauldronToCrucible);
    }

    private AlchemyGameTests() {
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
            Thaumcraft.id("alchemy_default"),
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

    private static void crucibleRecipeOutput(GameTestHelper helper) {
        BlockPos crucibleOffset = new BlockPos(2, 1, 2);
        helper.setBlock(crucibleOffset.below(), Blocks.LAVA);
        helper.setBlock(crucibleOffset, ModBlocks.CRUCIBLE.get());
        CrucibleBlockEntity crucible = helper.getBlockEntity(crucibleOffset, CrucibleBlockEntity.class);
        crucible.fillWater(1000);
        crucible.setHeat(180);
        crucible.getAspects().add(Aspect.FIRE, 2).add(Aspect.WATER, 2).add(Aspect.EARTH, 2).add(Aspect.ORDER, 2).add(Aspect.ENTROPY, 2);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        Knowledge.get(player).addResearch("CRUCIBLE");
        ItemEntity shard = helper.spawnItem(ModItems.AIR_SHARD.get(), crucibleOffset.above());
        shard.setThrower(player);
        helper.succeedWhen(() -> {
            helper.assertItemEntityPresent(ModItems.BALANCED_SHARD.get(), crucibleOffset, 2.0);
            helper.assertTrue(crucible.getAspects().isEmpty(), "aspects left in crucible");
        });
    }

    private static void cauldronToCrucible(GameTestHelper helper) {
        BlockPos offset = new BlockPos(2, 1, 2);
        helper.setBlock(offset, Blocks.CAULDRON);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack wand = Wands.create(WandRegistry.CAP_GOLD, WandRegistry.ROD_GREATWOOD, false);
        Wands.fill(wand);
        boolean done = WandTriggers.perform(helper.getLevel(), wand, player, helper.absolutePos(offset), Direction.UP);
        helper.assertTrue(done, "trigger did not fire");
        helper.assertBlockPresent(ModBlocks.CRUCIBLE.get(), offset);
        helper.succeed();
    }
}
