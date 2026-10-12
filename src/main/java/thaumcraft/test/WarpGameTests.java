package thaumcraft.test;

import io.netty.channel.embedded.EmbeddedChannel;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import thaumcraft.Thaumcraft;
import thaumcraft.aspect.Aspect;
import thaumcraft.effect.EffectHooks;
import thaumcraft.entity.monster.Pech;
import thaumcraft.menu.PechTradeMenu;
import thaumcraft.registry.ModEffects;
import thaumcraft.research.Knowledge;
import thaumcraft.research.Warp;
import thaumcraft.research.WarpEvents;
import thaumcraft.research.WarpType;
import thaumcraft.wand.WandManager;

@EventBusSubscriber(modid = Thaumcraft.MODID)
public final class WarpGameTests {
    private static final DeferredRegister<Consumer<GameTestHelper>> FUNCTIONS = DeferredRegister.create(Registries.TEST_FUNCTION, Thaumcraft.MODID);
    private static final String[] TESTS = {"warp_temp_decay_and_ward", "warp_vis_exhaust_wand_cost", "taint_poison_damages_bat", "warp_gap_effects_noop", "pech_trade_offer_tables"};

    static {
        FUNCTIONS.register(TESTS[0], () -> WarpGameTests::tempWarpDecayAndWard);
        FUNCTIONS.register(TESTS[1], () -> WarpGameTests::visExhaustWandCost);
        FUNCTIONS.register(TESTS[2], () -> WarpGameTests::taintPoisonDamagesBat);
        FUNCTIONS.register(TESTS[3], () -> WarpGameTests::warpGapEffectsNoop);
        FUNCTIONS.register(TESTS[4], () -> WarpGameTests::pechTradeOfferTables);
    }

    private WarpGameTests() {
    }

    public static void register(IEventBus bus) {
        FUNCTIONS.register(bus);
    }

    @SubscribeEvent
    static void registerTests(RegisterGameTestsEvent event) {
        Holder<TestEnvironmentDefinition<?>> environment = event.registerEnvironment(Thaumcraft.id("warp_default"), new TestEnvironmentDefinition.AllOf());
        for (String name : TESTS) {
            ResourceKey<Consumer<GameTestHelper>> function = ResourceKey.create(Registries.TEST_FUNCTION, Thaumcraft.id(name));
            event.registerTest(
                Thaumcraft.id(name),
                new FunctionGameTestInstance(function, new TestData<>(environment, Thaumcraft.id("test/platform"), 100, 0, true))
            );
        }
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
        player.connection.markClientLoaded();
        return player;
    }

    private static void tempWarpDecayAndWard(GameTestHelper helper) {
        ServerPlayer player = mockServerPlayer(helper);
        Knowledge.get(player);
        Warp.set(player, 2, WarpType.TEMPORARY);
        WarpEvents.checkWarpEvent(player);
        helper.assertValueEqual(Warp.get(player, WarpType.TEMPORARY), 1, "temporary warp should decay once per event check");
        Warp.set(player, 3, WarpType.TEMPORARY);
        player.tickCount = 1999;
        NeoForge.EVENT_BUS.post(new PlayerTickEvent.Post(player));
        helper.assertValueEqual(Warp.get(player, WarpType.TEMPORARY), 3, "temporary warp must not decay before the scheduled check");
        player.tickCount = 2000;
        NeoForge.EVENT_BUS.post(new PlayerTickEvent.Post(player));
        helper.assertValueEqual(Warp.get(player, WarpType.TEMPORARY), 2, "scheduled check must decay temporary warp once");
        player.addEffect(new net.minecraft.world.effect.MobEffectInstance(ModEffects.WARP_WARD, 100, 0));
        Warp.set(player, 2, WarpType.TEMPORARY);
        player.tickCount = 4000;
        NeoForge.EVENT_BUS.post(new PlayerTickEvent.Post(player));
        helper.assertValueEqual(Warp.get(player, WarpType.TEMPORARY), 2, "warp ward should suppress event and decay");
        helper.succeed();
    }

    private static void visExhaustWandCost(GameTestHelper helper) {
        ServerPlayer player = mockServerPlayer(helper);
        EffectHooks.applyVisExhaust(player, 100, 1);
        float discount = WandManager.getTotalVisDiscount(player, Aspect.AIR);
        helper.assertTrue(Math.abs(discount + 0.2F) < 0.001F, "vis exhaust should add 20 percent wand cost at amplifier one");
        player.removeEffect(ModEffects.VIS_EXHAUST);
        player.addEffect(new net.minecraft.world.effect.MobEffectInstance(ModEffects.INFECTIOUS_VIS_EXHAUST, 100, 2));
        discount = WandManager.getTotalVisDiscount(player, Aspect.AIR);
        helper.assertTrue(Math.abs(discount + 0.3F) < 0.001F, "infectious vis exhaust should add 30 percent wand cost at amplifier two");
        helper.succeed();
    }

    private static void taintPoisonDamagesBat(GameTestHelper helper) {
        LivingEntity bat = EntityTypes.BAT.create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
        if (bat == null) {
            helper.fail("bat entity could not be created");
            return;
        }
        BlockPos pos = helper.absolutePos(BlockPos.ZERO);
        bat.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        helper.getLevel().addFreshEntity(bat);
        float health = bat.getHealth();
        ModEffects.TAINT_POISON.value().applyEffectTick(helper.getLevel(), bat, 0);
        helper.assertTrue(bat.getHealth() < health, "taint poison should hurt an ordinary small non-undead mob");
        helper.succeed();
    }

    private static void warpGapEffectsNoop(GameTestHelper helper) {
        ServerPlayer player = mockServerPlayer(helper);
        Knowledge.get(player);
        Warp.set(player, 3, WarpType.STICKY);
        int airPool = Knowledge.get(player).getAspectPool(Aspect.AIR);
        try {
            var method = WarpEvents.class.getDeclaredMethod("applyWarpEvent", ServerPlayer.class, int.class, int.class);
            method.setAccessible(true);
            for (int effect = 73; effect <= 75; effect++) {
                method.invoke(null, player, 100, effect);
            }
        } catch (ReflectiveOperationException exception) {
            helper.fail("warp event entry could not be invoked: " + exception.getMessage());
            return;
        }
        helper.assertValueEqual(Warp.get(player, WarpType.STICKY), 3, "effects 73 through 75 must not change sticky warp");
        helper.assertValueEqual(Knowledge.get(player).getAspectPool(Aspect.AIR), airPool, "effects 73 through 75 must not grant aspects");
        helper.assertTrue(player.getActiveEffects().isEmpty(), "effects 73 through 75 must not apply a potion effect");
        helper.succeed();
    }

    private static void pechTradeOfferTables(GameTestHelper helper) {
        var registryAccess = helper.getLevel().registryAccess();
        helper.assertValueEqual(PechTradeMenu.tradeOffers(Pech.PechType.NORMAL, 1, registryAccess).size(), 8, "normal value-one offer count");
        helper.assertValueEqual(PechTradeMenu.tradeOffers(Pech.PechType.NORMAL, 2, registryAccess).size(), 4, "normal value-two offer count");
        helper.assertValueEqual(PechTradeMenu.tradeOffers(Pech.PechType.NORMAL, 3, registryAccess).size(), 5, "normal value-three offer count");
        helper.assertValueEqual(PechTradeMenu.tradeOffers(Pech.PechType.NORMAL, 4, registryAccess).size(), 1, "normal value-four offer count");
        helper.assertValueEqual(PechTradeMenu.tradeOffers(Pech.PechType.NORMAL, 5, registryAccess).size(), 3, "normal value-five offer count");
        helper.assertValueEqual(PechTradeMenu.tradeOffers(Pech.PechType.WARRIOR, 1, registryAccess).size(), 8, "warrior value-one offer count");
        helper.assertValueEqual(PechTradeMenu.tradeOffers(Pech.PechType.WARRIOR, 2, registryAccess).size(), 3, "warrior value-two offer count");
        helper.assertValueEqual(PechTradeMenu.tradeOffers(Pech.PechType.WARRIOR, 3, registryAccess).size(), 4, "warrior value-three offer count");
        helper.assertValueEqual(PechTradeMenu.tradeOffers(Pech.PechType.WARRIOR, 4, registryAccess).size(), 7, "warrior value-four offer count");
        helper.assertValueEqual(PechTradeMenu.tradeOffers(Pech.PechType.WARRIOR, 5, registryAccess).size(), 5, "warrior value-five offer count");
        helper.assertValueEqual(PechTradeMenu.tradeOffers(Pech.PechType.ARCHER, 1, registryAccess).size(), 16, "archer value-one offer count");
        helper.assertValueEqual(PechTradeMenu.tradeOffers(Pech.PechType.ARCHER, 2, registryAccess).size(), 4, "archer value-two offer count");
        helper.assertValueEqual(PechTradeMenu.tradeOffers(Pech.PechType.ARCHER, 3, registryAccess).size(), 5, "archer value-three offer count");
        helper.assertValueEqual(PechTradeMenu.tradeOffers(Pech.PechType.ARCHER, 4, registryAccess).size(), 1, "archer value-four offer count");
        helper.assertValueEqual(PechTradeMenu.tradeOffers(Pech.PechType.ARCHER, 5, registryAccess).size(), 4, "archer value-five offer count");
        helper.assertTrue(PechTradeMenu.tradeOffers(Pech.PechType.NORMAL, 1, registryAccess).stream().noneMatch(stack -> stack.is(thaumcraft.registry.ModItems.COPPER_NUGGET)), "normal value-one offers should use native clusters, not standalone metal nuggets");
        helper.succeed();
    }
}
