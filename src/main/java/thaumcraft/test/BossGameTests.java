package thaumcraft.test;

import java.util.function.Consumer;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import thaumcraft.entity.boss.EldritchWarden;
import thaumcraft.entity.boss.CultistLeader;
import thaumcraft.registry.ModItems;
import thaumcraft.entity.boss.TaintacleGiant;
import thaumcraft.entity.monster.ChampionModifier;
import thaumcraft.registry.EldritchContent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import thaumcraft.Thaumcraft;
import thaumcraft.entity.boss.CultistPortal;
import thaumcraft.entity.boss.EldritchGolem;
import thaumcraft.registry.EldritchEntities;
import thaumcraft.entity.monster.Cultist;

@EventBusSubscriber(modid = Thaumcraft.MODID)
public final class BossGameTests {
    private static final DeferredRegister<Consumer<GameTestHelper>> FUNCTIONS = DeferredRegister.create(Registries.TEST_FUNCTION, Thaumcraft.MODID);
    private static final String[] TESTS = {
        "eldritch_golem_enters_headless_phase",
        "cultist_portal_starts_minion_wave",
        "eldritch_warden_enters_frenzy_once",
        "taint_boss_encounter_has_five_champions",
        "boss_phase_and_portal_wave_survive_save",
        "cultist_leader_drops_one_tier_two_reward",
        "base_boss_drops_pearl_and_rare_bag",
        "cultist_portal_drops_one_pearl",
        "taintacle_giant_last_death_drops_pearl"
    };

    static {
        FUNCTIONS.register(TESTS[0], () -> BossGameTests::golemEntersHeadlessPhase);
        FUNCTIONS.register(TESTS[1], () -> BossGameTests::portalStartsMinionWave);
        FUNCTIONS.register(TESTS[2], () -> BossGameTests::wardenEntersFrenzyOnce);
        FUNCTIONS.register(TESTS[3], () -> BossGameTests::taintEncounterHasFiveChampions);
        FUNCTIONS.register(TESTS[4], () -> BossGameTests::phaseAndWaveSurviveSave);
        FUNCTIONS.register(TESTS[5], () -> BossGameTests::leaderRewardIsUnique);
        FUNCTIONS.register(TESTS[6], () -> BossGameTests::baseBossRewardIsUnique);
        FUNCTIONS.register(TESTS[7], () -> BossGameTests::portalRewardIsUnique);
        FUNCTIONS.register(TESTS[8], () -> BossGameTests::lastGiantRewardIsUnique);
    }

    private BossGameTests() {
    }

    public static void register(IEventBus bus) {
        FUNCTIONS.register(bus);
    }

    @SubscribeEvent
    static void registerTests(RegisterGameTestsEvent event) {
        var environment = event.registerEnvironment(Thaumcraft.id("boss_default"), new TestEnvironmentDefinition.AllOf());
        for (String name : TESTS) {
            ResourceKey<Consumer<GameTestHelper>> function = ResourceKey.create(Registries.TEST_FUNCTION, Thaumcraft.id(name));
            event.registerTest(Thaumcraft.id(name), new FunctionGameTestInstance(
                function,
                new TestData<>(environment, Thaumcraft.id("test/platform"), 300, 0, true)
            ));
        }
    }

    private static void golemEntersHeadlessPhase(GameTestHelper helper) {
        EldritchGolem golem = EldritchEntities.ELDRITCH_GOLEM.get().create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
        helper.assertTrue(golem != null, "golem factory returned null");
        golem.snapTo(helper.absolutePos(new net.minecraft.core.BlockPos(2, 2, 2)), 0.0F, 0.0F);
        helper.getLevel().addFreshEntity(golem);
        boolean hit = golem.hurtServer(helper.getLevel(), helper.getLevel().damageSources().generic(), 300.0F);
        helper.assertFalse(hit, "first lethal hit should trigger the headless phase");
        helper.assertTrue(golem.isAlive(), "golem died instead of entering its second phase");
        helper.assertTrue(golem.isHeadless(), "golem did not synchronize its headless phase");
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, helper.getLevel().registryAccess());
        golem.saveWithoutId(output);
        EldritchGolem loaded = EldritchEntities.ELDRITCH_GOLEM.get().create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
        loaded.load(TagValueInput.create(ProblemReporter.DISCARDING, helper.getLevel().registryAccess(), output.buildResult()));
        helper.assertTrue(loaded.isHeadless(), "headless phase did not survive entity save/load");
        helper.succeed();
    }

    private static void portalStartsMinionWave(GameTestHelper helper) {
        CultistPortal portal = EldritchEntities.CULTIST_PORTAL.get().create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
        helper.assertTrue(portal != null, "portal factory returned null");
        net.minecraft.core.BlockPos pos = helper.absolutePos(new net.minecraft.core.BlockPos(3, 2, 3));
        portal.snapTo(pos, 0.0F, 0.0F);
        helper.getLevel().addFreshEntity(portal);
        ServerPlayer player = (ServerPlayer) helper.makeMockServerPlayer(GameType.SURVIVAL);
        CommonListenerCookie cookie = CommonListenerCookie.createInitial(player.getGameProfile(), false);
        Connection connection = new Connection(PacketFlow.SERVERBOUND);
        new EmbeddedChannel(connection);
        player.connection = new ServerGamePacketListenerImpl(helper.getLevel().getServer(), connection, player, cookie) {
            @Override
            public void send(Packet<?> packet) {
            }
        };
        player.snapTo(pos.getX() + 2.0, pos.getY(), pos.getZ() + 2.0, 0.0F, 0.0F);
        helper.getLevel().addNewPlayer(player);
        player.connection.markClientLoaded();
        for (int tick = 0; tick < 201; tick++) {
            portal.tick();
        }
        helper.assertTrue(portal.getStage() > 0, "portal did not advance after its initial countdown");
        helper.assertTrue(!helper.getLevel().getEntitiesOfClass(Cultist.class, portal.getBoundingBox().inflate(8.0)).isEmpty(), "portal did not spawn its first cultist");
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, helper.getLevel().registryAccess());
        portal.saveWithoutId(output);
        CultistPortal loaded = EldritchEntities.CULTIST_PORTAL.get().create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
        loaded.load(TagValueInput.create(ProblemReporter.DISCARDING, helper.getLevel().registryAccess(), output.buildResult()));
        helper.assertValueEqual(loaded.getStage(), portal.getStage(), "portal wave stage survives entity save/load");
        helper.succeed();
    }

    private static void wardenEntersFrenzyOnce(GameTestHelper helper) {
        EldritchWarden warden = EldritchEntities.ELDRITCH_WARDEN.get().create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
        helper.assertTrue(warden != null, "warden factory returned null");
        warden.snapTo(helper.absolutePos(new BlockPos(2, 2, 2)), 0.0F, 0.0F);
        helper.getLevel().addFreshEntity(warden);
        warden.setAbsorptionAmount(0.0F);
        warden.hurtServer(helper.getLevel(), helper.getLevel().damageSources().generic(), 1.0F);
        helper.assertTrue(warden.getFrenzyTicks() > 0, "warden did not enter frenzy when its absorption shield broke");
        warden.setAbsorptionAmount(10.0F);
        warden.hurtServer(helper.getLevel(), helper.getLevel().damageSources().generic(), 1.0F);
        helper.assertTrue(warden.getFrenzyTicks() > 0, "warden prematurely left or restarted frenzy");
        helper.succeed();
    }

    private static void taintEncounterHasFiveChampions(GameTestHelper helper) {
        BlockPos center = helper.absolutePos(new BlockPos(8, 2, 8));
        helper.assertTrue(EldritchEntities.spawnTaintEncounter(helper.getLevel(), center), "taint encounter spawn failed");
        var bosses = helper.getLevel().getEntitiesOfClass(thaumcraft.entity.monster.taint.Taintacle.class, new AABB(center).inflate(9.0));
        helper.assertValueEqual(bosses.size(), 5, "taint encounter entity count");
        for (var boss : bosses) {
            helper.assertTrue(ChampionModifier.get(boss) >= 0, "taint encounter member lacks champion modifier");
            boss.discard();
        }
        helper.succeed();
    }

    private static void leaderRewardIsUnique(GameTestHelper helper) {
        CultistLeader leader = EldritchEntities.CULTIST_LEADER.get().create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
        helper.assertTrue(leader != null, "leader factory returned null");
        BlockPos pos = helper.absolutePos(new BlockPos(2, 2, 2));
        leader.snapTo(pos, 0.0F, 0.0F);
        leader.finalizeSpawn(helper.getLevel(), helper.getLevel().getCurrentDifficultyAt(pos), EntitySpawnReason.TRIGGERED, null);
        helper.getLevel().addFreshEntity(leader);
        leader.setHealth(1.0F);
        helper.assertTrue(leader.hurtServer(helper.getLevel(), helper.getLevel().damageSources().generic(), 10.0F), "lethal leader hit was rejected");
        var drops = helper.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(4.0));
        long bags = drops.stream().filter(drop -> drop.getItem().is(ModItems.RARE_LOOT_BAG.get())).count();
        long pearls = drops.stream().filter(drop -> drop.getItem().is(EldritchContent.PRIMORDIAL_PEARL.get())).count();
        helper.assertValueEqual(bags, 1L, "leader tier two reward count");
        helper.assertValueEqual(pearls, 0L, "leader must not drop the base boss pearl");
        helper.succeed();
    }

    private static void baseBossRewardIsUnique(GameTestHelper helper) {
        EldritchGolem golem = EldritchEntities.ELDRITCH_GOLEM.get().create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
        helper.assertTrue(golem != null, "golem factory returned null");
        BlockPos pos = helper.absolutePos(new BlockPos(2, 2, 2));
        golem.snapTo(pos, 0.0F, 0.0F);
        golem.setHeadless(true);
        golem.setSpawnTimer(0);
        golem.setHealth(1.0F);
        helper.assertTrue(helper.getLevel().addFreshEntity(golem), "golem could not be added");
        helper.assertTrue(golem.hurtServer(helper.getLevel(), helper.getLevel().damageSources().generic(), 100.0F), "lethal golem hit was rejected");
        AABB bounds = new AABB(pos).inflate(4.0);
        helper.assertValueEqual(countDrops(helper, bounds, EldritchContent.PRIMORDIAL_PEARL.get()), 1L, "base boss pearl count");
        helper.assertValueEqual(countDrops(helper, bounds, ModItems.RARE_LOOT_BAG.get()), 1L, "base boss rare bag count");
        helper.succeed();
    }

    private static void portalRewardIsUnique(GameTestHelper helper) {
        CultistPortal portal = EldritchEntities.CULTIST_PORTAL.get().create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
        helper.assertTrue(portal != null, "portal factory returned null");
        BlockPos pos = helper.absolutePos(new BlockPos(2, 2, 2));
        portal.snapTo(pos, 0.0F, 0.0F);
        portal.setHealth(1.0F);
        helper.assertTrue(helper.getLevel().addFreshEntity(portal), "portal could not be added");
        helper.assertTrue(portal.hurtServer(helper.getLevel(), helper.getLevel().damageSources().generic(), 100.0F), "lethal portal hit was rejected");
        AABB bounds = new AABB(pos).inflate(4.0);
        helper.assertValueEqual(countDrops(helper, bounds, EldritchContent.PRIMORDIAL_PEARL.get()), 1L, "portal pearl count");
        helper.assertValueEqual(countDrops(helper, bounds, ModItems.RARE_LOOT_BAG.get()), 0L, "portal must not drop the base boss rare bag");
        helper.succeed();
    }

    private static void lastGiantRewardIsUnique(GameTestHelper helper) {
        BlockPos pos = helper.absolutePos(new BlockPos(4, 2, 4));
        TaintacleGiant first = EldritchEntities.TAINTACLE_GIANT.get().create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
        TaintacleGiant last = EldritchEntities.TAINTACLE_GIANT.get().create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
        helper.assertTrue(first != null && last != null, "giant factory returned null");
        first.snapTo(pos, 0.0F, 0.0F);
        last.snapTo(pos.offset(2, 0, 0), 0.0F, 0.0F);
        first.setHealth(1.0F);
        last.setHealth(1.0F);
        helper.assertTrue(helper.getLevel().addFreshEntity(first), "first giant could not be added");
        helper.assertTrue(helper.getLevel().addFreshEntity(last), "last giant could not be added");
        helper.assertTrue(first.hurtServer(helper.getLevel(), helper.getLevel().damageSources().generic(), 100.0F), "first giant lethal hit was rejected");
        helper.assertFalse(first.isAlive(), "first giant survived: health=" + first.getHealth());
        AABB bounds = new AABB(pos).inflate(48.0);
        helper.assertValueEqual(countDrops(helper, bounds, EldritchContent.PRIMORDIAL_PEARL.get()), 0L, "non-final giant must not drop a pearl");
        helper.assertTrue(last.hurtServer(helper.getLevel(), helper.getLevel().damageSources().generic(), 100.0F), "last giant lethal hit was rejected");
        helper.assertFalse(last.isAlive(), "last giant survived: health=" + last.getHealth());
        helper.assertValueEqual(countDrops(helper, bounds, EldritchContent.PRIMORDIAL_PEARL.get()), 1L, "last giant pearl count");
        helper.succeed();
    }

    private static long countDrops(GameTestHelper helper, AABB bounds, Item item) {
        return helper.getLevel().getEntitiesOfClass(ItemEntity.class, bounds).stream()
            .filter(drop -> drop.getItem().is(item))
            .mapToLong(drop -> drop.getItem().getCount())
            .sum();
    }

    private static void phaseAndWaveSurviveSave(GameTestHelper helper) {
        EldritchGolem golem = EldritchEntities.ELDRITCH_GOLEM.get().create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
        EldritchGolem loadedGolem = EldritchEntities.ELDRITCH_GOLEM.get().create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
        golem.setHeadless(true);
        golem.setBeamCharge(91);
        TagValueOutput golemOutput = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, helper.getLevel().registryAccess());
        golem.saveWithoutId(golemOutput);
        loadedGolem.load(TagValueInput.create(ProblemReporter.DISCARDING, helper.getLevel().registryAccess(), golemOutput.buildResult()));
        helper.assertTrue(loadedGolem.isHeadless() && loadedGolem.getBeamCharge() == 91, "golem beam phase not saved");
        CultistPortal portal = EldritchEntities.CULTIST_PORTAL.get().create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
        CultistPortal loadedPortal = EldritchEntities.CULTIST_PORTAL.get().create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
        TagValueOutput portalOutput = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, helper.getLevel().registryAccess());
        portal.saveWithoutId(portalOutput);
        loadedPortal.load(TagValueInput.create(ProblemReporter.DISCARDING, helper.getLevel().registryAccess(), portalOutput.buildResult()));
        helper.assertValueEqual(loadedPortal.getStage(), portal.getStage(), "portal stage is stable across save/load");
        helper.succeed();
    }
}
