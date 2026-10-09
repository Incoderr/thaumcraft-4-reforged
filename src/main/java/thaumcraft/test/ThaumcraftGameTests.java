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
import thaumcraft.aspect.Aspect;
import thaumcraft.aspect.AspectList;
import thaumcraft.blockentity.WandPedestalBlockEntity;
import thaumcraft.node.NodeBlockEntity;
import thaumcraft.node.NodeGenerator;
import thaumcraft.node.NodeType;
import thaumcraft.registry.ModBlocks;
import thaumcraft.wand.WandRegistry;
import thaumcraft.wand.WandTriggers;
import thaumcraft.wand.Wands;

@EventBusSubscriber(modid = Thaumcraft.MODID)
public final class ThaumcraftGameTests {
    private static final DeferredRegister<Consumer<GameTestHelper>> FUNCTIONS = DeferredRegister.create(Registries.TEST_FUNCTION, Thaumcraft.MODID);
    private static final String[] TESTS = {"wand_taps_node", "pedestal_charges_wand", "node_jar_trigger"};

    static {
        FUNCTIONS.register(TESTS[0], () -> ThaumcraftGameTests::wandTapsNode);
        FUNCTIONS.register(TESTS[1], () -> ThaumcraftGameTests::pedestalChargesWand);
        FUNCTIONS.register(TESTS[2], () -> ThaumcraftGameTests::nodeJarTrigger);
    }

    private ThaumcraftGameTests() {
    }

    public static void register(IEventBus bus) {
        FUNCTIONS.register(bus);
    }

    @SubscribeEvent
    static void registerTests(RegisterGameTestsEvent event) {
        Holder<TestEnvironmentDefinition<?>> environment = event.registerEnvironment(
            Thaumcraft.id("default"),
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

    private static AspectList primals(int amount) {
        AspectList aspects = new AspectList();
        for (Aspect aspect : Aspect.getPrimalAspects()) {
            aspects.add(aspect, amount);
        }
        return aspects;
    }

    private static int totalVis(ItemStack wand) {
        return Wands.getAllVis(wand).visSize();
    }

    private static void wandTapsNode(GameTestHelper helper) {
        BlockPos nodePos = helper.absolutePos(new BlockPos(3, 2, 3));
        NodeGenerator.createNodeAt(helper.getLevel(), nodePos, NodeType.NORMAL, null, primals(20));
        NodeBlockEntity node = helper.getBlockEntity(new BlockPos(3, 2, 3), NodeBlockEntity.class);
        int before = node.getAspects().visSize();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack wand = Wands.create(WandRegistry.CAP_IRON, WandRegistry.ROD_WOOD, false);
        for (int tick = 5; tick <= 50; tick += 5) {
            node.onUsingWandTick(wand, player, tick);
        }
        helper.assertTrue(totalVis(wand) > 0, "wand received no vis");
        helper.assertTrue(node.getAspects().visSize() < before, "node lost no vis");
        helper.assertValueEqual(before - node.getAspects().visSize(), totalVis(wand) / 100, "vis moved");
        helper.succeed();
    }

    private static void pedestalChargesWand(GameTestHelper helper) {
        NodeGenerator.createNodeAt(helper.getLevel(), helper.absolutePos(new BlockPos(3, 4, 3)), NodeType.NORMAL, null, primals(20));
        helper.setBlock(new BlockPos(1, 1, 1), ModBlocks.WAND_RECHARGE_PEDESTAL.get());
        WandPedestalBlockEntity pedestal = helper.getBlockEntity(new BlockPos(1, 1, 1), WandPedestalBlockEntity.class);
        ItemStack wand = Wands.create(WandRegistry.CAP_GOLD, WandRegistry.ROD_GREATWOOD, false);
        pedestal.setStack(wand);
        helper.succeedWhen(() -> helper.assertTrue(totalVis(pedestal.getStack()) >= 600, "pedestal did not charge the wand"));
    }

    private static void nodeJarTrigger(GameTestHelper helper) {
        BlockPos center = new BlockPos(3, 2, 3);
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = -1; y <= 1; y++) {
                    if (x != 0 || y != 0 || z != 0) {
                        helper.setBlock(center.offset(x, y, z), Blocks.GLASS);
                    }
                }
                helper.setBlock(center.offset(x, 2, z), Blocks.OAK_SLAB);
            }
        }
        NodeGenerator.createNodeAt(helper.getLevel(), helper.absolutePos(center), NodeType.HUNGRY, null, primals(15));
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack wand = Wands.create(WandRegistry.CAP_GOLD, WandRegistry.ROD_SILVERWOOD, false);
        Wands.fill(wand);
        boolean done = WandTriggers.perform(helper.getLevel(), wand, player, helper.absolutePos(center.offset(1, 1, 1)), Direction.UP);
        helper.assertTrue(done, "trigger did not fire");
        helper.assertBlockPresent(ModBlocks.NODE_JAR.get(), center);
        helper.assertBlockPresent(Blocks.AIR, center.offset(1, 1, 1));
        helper.assertValueEqual(Wands.getVis(wand, Aspect.AIR), Wands.getMaxVis(wand) - 7000, "air vis left");
        helper.succeed();
    }
}
