package thaumcraft.test;

import io.netty.channel.embedded.EmbeddedChannel;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import thaumcraft.Thaumcraft;
import thaumcraft.block.LiquidDeathBlock;
import thaumcraft.registry.ModBlocks;
import thaumcraft.registry.ModItems;
import thaumcraft.research.Warp;
import thaumcraft.research.WarpType;

@EventBusSubscriber(modid = Thaumcraft.MODID)
public final class FluidGameTests {
    private static final DeferredRegister<Consumer<GameTestHelper>> FUNCTIONS = DeferredRegister.create(Registries.TEST_FUNCTION, Thaumcraft.MODID);
    private static final String[] TESTS = {"liquid_death_horizontal_quanta", "liquid_death_vertical_quanta", "sanitizing_soap_release_threshold"};

    static {
        FUNCTIONS.register(TESTS[0], () -> FluidGameTests::liquidDeathHorizontalQuanta);
        FUNCTIONS.register(TESTS[1], () -> FluidGameTests::liquidDeathVerticalQuanta);
        FUNCTIONS.register(TESTS[2], () -> FluidGameTests::sanitizingSoapReleaseThreshold);
    }

    private FluidGameTests() {
    }

    public static void register(IEventBus bus) {
        FUNCTIONS.register(bus);
    }

    @SubscribeEvent
    static void registerTests(RegisterGameTestsEvent event) {
        var environment = event.registerEnvironment(Thaumcraft.id("fluid_default"), new TestEnvironmentDefinition.AllOf());
        for (String name : TESTS) {
            ResourceKey<Consumer<GameTestHelper>> function = ResourceKey.create(Registries.TEST_FUNCTION, Thaumcraft.id(name));
            event.registerTest(Thaumcraft.id(name), new FunctionGameTestInstance(
                function,
                new TestData<>(environment, Thaumcraft.id("test/platform"), 200, 0, true)
            ));
        }
    }

    private static void liquidDeathHorizontalQuanta(GameTestHelper helper) {
        BlockPos source = new BlockPos(2, 2, 2);
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                helper.setBlock(source.offset(x, -1, z), Blocks.STONE);
            }
        }
        helper.setBlock(source, ModBlocks.LIQUID_DEATH.get());
        helper.runAfterDelay(6, () -> {
            boolean spread = false;
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    spread |= (x != 0 || z != 0) && quanta(helper.getBlockState(source.offset(x, 0, z))) > 0;
                }
            }
            helper.assertValueEqual(totalQuanta(helper, source, -1, 1, -1, 1, -1, 1), LiquidDeathBlock.QUANTA_PER_BLOCK, "scheduled horizontal flow changed liquid death quanta");
            helper.assertTrue(spread, "scheduled horizontal tick did not move liquid death");
            helper.runAfterDelay(15, () -> {
                helper.assertValueEqual(totalQuanta(helper, source, -2, 2, -1, 1, -2, 2), LiquidDeathBlock.QUANTA_PER_BLOCK, "repeated scheduled horizontal ticks duplicated or lost liquid death quanta");
                helper.succeed();
            });
        });
    }

    private static void liquidDeathVerticalQuanta(GameTestHelper helper) {
        BlockPos source = new BlockPos(2, 3, 2);
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                helper.setBlock(source.offset(x, -2, z), Blocks.STONE);
            }
        }
        helper.setBlock(source, ModBlocks.LIQUID_DEATH.get());
        helper.runAfterDelay(6, () -> {
            helper.assertValueEqual(totalQuanta(helper, source, -1, 1, -2, 0, -1, 1), LiquidDeathBlock.QUANTA_PER_BLOCK, "scheduled vertical flow changed liquid death quanta");
            helper.assertValueEqual(quanta(helper.getBlockState(source.below())), LiquidDeathBlock.QUANTA_PER_BLOCK, "scheduled vertical tick did not move all liquid death quanta");
            helper.runAfterDelay(15, () -> {
                helper.assertValueEqual(totalQuanta(helper, source, -2, 2, -2, 0, -2, 2), LiquidDeathBlock.QUANTA_PER_BLOCK, "repeated scheduled vertical ticks duplicated or lost liquid death quanta");
                helper.succeed();
            });
        });
    }

    private static void sanitizingSoapReleaseThreshold(GameTestHelper helper) {
        ServerPlayer player = mockServerPlayer(helper);
        Warp.set(player, 0, WarpType.STICKY);
        Warp.set(player, 4, WarpType.TEMPORARY);
        ItemStack soap = new ItemStack(ModItems.SANITIZING_SOAP.get(), 2);
        player.setItemInHand(InteractionHand.MAIN_HAND, soap);
        player.startUsingItem(InteractionHand.MAIN_HAND);
        ModItems.SANITIZING_SOAP.get().onUseTick(helper.getLevel(), player, soap, 5);
        helper.assertValueEqual(soap.getCount(), 2, "soap was consumed before 196 use ticks");
        helper.assertValueEqual(Warp.get(player, WarpType.TEMPORARY), 4, "soap cleared temporary warp before 196 use ticks");
        ModItems.SANITIZING_SOAP.get().onUseTick(helper.getLevel(), player, soap, 4);
        helper.assertValueEqual(soap.getCount(), 1, "soap was not consumed at 196 use ticks");
        helper.assertValueEqual(Warp.get(player, WarpType.TEMPORARY), 0, "soap did not clear temporary warp at 196 use ticks");
        helper.succeed();
    }

    private static int quanta(BlockState state) {
        if (!(state.getBlock() instanceof LiquidDeathBlock)) {
            return 0;
        }
        int level = state.getValue(LiquidBlock.LEVEL);
        return level == 0 ? LiquidDeathBlock.QUANTA_PER_BLOCK : Math.max(0, LiquidDeathBlock.QUANTA_PER_BLOCK - level);
    }

    private static int totalQuanta(GameTestHelper helper, BlockPos origin, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        int total = 0;
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    total += quanta(helper.getBlockState(origin.offset(x, y, z)));
                }
            }
        }
        return total;
    }

    private static ServerPlayer mockServerPlayer(GameTestHelper helper) {
        ServerPlayer player = (ServerPlayer) helper.makeMockServerPlayer(GameType.SURVIVAL);
        CommonListenerCookie cookie = CommonListenerCookie.createInitial(player.getGameProfile(), false);
        Connection connection = new Connection(PacketFlow.SERVERBOUND);
        new EmbeddedChannel(connection);
        new ServerGamePacketListenerImpl(helper.getLevel().getServer(), connection, player, cookie) {
            @Override
            public void send(Packet<?> packet) {
            }
        };
        return player;
    }
}
