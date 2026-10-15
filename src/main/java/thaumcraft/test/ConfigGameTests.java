package thaumcraft.test;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import thaumcraft.Config;
import thaumcraft.Thaumcraft;
import thaumcraft.entity.monster.ChampionModifier;
import thaumcraft.entity.monster.SpawnToggles;
import thaumcraft.registry.ModEntities;
import thaumcraft.research.ResearchCategories;

@EventBusSubscriber(modid = Thaumcraft.MODID)
public final class ConfigGameTests {
    private static final DeferredRegister<Consumer<GameTestHelper>> FUNCTIONS = DeferredRegister.create(Registries.TEST_FUNCTION, Thaumcraft.MODID);
    private static final String[] TESTS = {"config_defaults", "config_champion_mobs_off", "config_spawn_toggles", "config_mirrors_off"};

    static {
        FUNCTIONS.register(TESTS[0], () -> ConfigGameTests::defaults);
        FUNCTIONS.register(TESTS[1], () -> ConfigGameTests::championMobsOff);
        FUNCTIONS.register(TESTS[2], () -> ConfigGameTests::spawnToggles);
        FUNCTIONS.register(TESTS[3], () -> ConfigGameTests::mirrorsOff);
    }

    private ConfigGameTests() {
    }

    public static void register(IEventBus bus) {
        FUNCTIONS.register(bus);
    }

    @SubscribeEvent
    static void registerTests(RegisterGameTestsEvent event) {
        var environment = event.registerEnvironment(Thaumcraft.id("config_toggles"), new TestEnvironmentDefinition.AllOf());
        for (String name : TESTS) {
            ResourceKey<Consumer<GameTestHelper>> function = ResourceKey.create(Registries.TEST_FUNCTION, Thaumcraft.id(name));
            event.registerTest(Thaumcraft.id(name), new FunctionGameTestInstance(function, new TestData<>(environment, Thaumcraft.id("test/platform"), 200, 0, true)));
        }
    }

    private static void defaults(GameTestHelper helper) {
        helper.assertTrue(Config.SPEC.isLoaded(), "synced config must be loaded in the test server");
        helper.assertTrue(Config.wardedStone(), "warded stone default");
        helper.assertFalse(Config.allowCheatSheet(), "cheat sheet default");
        helper.assertFalse(Config.wuss(), "wuss mode default");
        helper.assertTrue(Config.allowMirrors(), "mirrors default");
        helper.assertTrue(Config.championMobs(), "champion mobs default");
        helper.assertTrue(Config.hardNode(), "hard nodes default");
        helper.assertValueEqual(Config.researchDifficulty(), 0, "research difficulty default");
        helper.assertValueEqual(Config.aspectTotalCap(), 100, "aspect total cap default");
        helper.assertValueEqual(Config.nodeRarity(), 36, "node rarity default");
        helper.assertValueEqual(Config.specialNodeRarity(), 18, "special node rarity default");
        helper.assertValueEqual(Config.shieldRecharge(), 2000, "shield recharge default");
        helper.assertValueEqual(Config.shieldWait(), 80, "shield wait default");
        helper.assertValueEqual(Config.shieldCost(), 50, "shield cost default");
        helper.assertValueEqual(Config.golemDelay(), 5, "golem delay default");
        helper.assertValueEqual(Config.taintSpreadRate(), 200, "taint spread default");
        helper.assertTrue(Config.taintFromFlux(), "taint from flux default");
        helper.succeed();
    }

    private static void championMobsOff(GameTestHelper helper) {
        boolean previous = Config.CHAMPION_MOBS.getAsBoolean();
        Config.CHAMPION_MOBS.set(false);
        try {
            var zombieType = BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.withDefaultNamespace("zombie"));
            for (int index = 0; index < 60; index++) {
                Mob mob = (Mob) zombieType.create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
                helper.assertTrue(mob != null, "zombie factory returned null");
                mob.snapTo(helper.absolutePos(new BlockPos(2 + index % 4, 2, 2 + index / 4)), 0.0F, 0.0F);
                helper.getLevel().addFreshEntity(mob);
                helper.assertValueEqual(ChampionModifier.get(mob), ChampionModifier.NORMAL, "champion assignment must be off for zombie " + index);
            }
        } finally {
            Config.CHAMPION_MOBS.set(previous);
        }
        helper.succeed();
    }

    private static void spawnToggles(GameTestHelper helper) {
        var types = new net.minecraft.world.entity.EntityType<?>[] {
            ModEntities.PECH.get(),
            ModEntities.WISP.get(),
            ModEntities.FIREBAT.get(),
            ModEntities.TAINTACLE.get(),
            ModEntities.BRAINY_ZOMBIE.get(),
            ModEntities.ELDRITCH_GUARDIAN.get()
        };
        ModConfigSpec.BooleanValue[] toggles = {
            Config.SPAWN_PECH,
            Config.SPAWN_WISP,
            Config.SPAWN_FIRE_BAT,
            Config.SPAWN_TAINTACLE,
            Config.SPAWN_ANGRY_ZOMBIE,
            Config.SPAWN_ELDER
        };
        for (int index = 0; index < types.length; index++) {
            boolean previous = toggles[index].getAsBoolean();
            helper.assertTrue(SpawnToggles.isEnabled(types[index]), "spawn enabled by default for " + index);
            toggles[index].set(false);
            try {
                helper.assertFalse(SpawnToggles.isEnabled(types[index]), "spawn toggle off for " + index);
                Mob mob = (Mob) types[index].create(helper.getLevel(), EntitySpawnReason.NATURAL);
                helper.assertTrue(mob != null, "factory returned null for " + index);
                mob.snapTo(helper.absolutePos(new BlockPos(2, 2, 2)), 0.0F, 0.0F);
                helper.assertFalse(EventHooks.checkSpawnPosition(mob, helper.getLevel(), EntitySpawnReason.NATURAL), "natural spawn must be refused for " + index);
                helper.assertFalse(EventHooks.checkSpawnPosition(mob, helper.getLevel(), EntitySpawnReason.CHUNK_GENERATION), "chunk generation spawn must be refused for " + index);
            } finally {
                toggles[index].set(previous);
            }
            helper.assertTrue(SpawnToggles.isEnabled(types[index]), "spawn restored for " + index);
        }
        helper.succeed();
    }

    private static void mirrorsOff(GameTestHelper helper) {
        boolean previous = Config.ALLOW_MIRRORS.getAsBoolean();
        helper.assertTrue(ResearchCategories.getResearch("MIRROR") != null, "mirror research present by default");
        Config.ALLOW_MIRRORS.set(false);
        try {
            helper.assertTrue(ResearchCategories.getResearch("MIRROR") == null, "mirror research hidden when mirrors are off");
            helper.assertFalse(ResearchCategories.allResearch().stream().anyMatch(item -> item.key().equals("MIRROR")), "mirror research absent from the full list");
        } finally {
            Config.ALLOW_MIRRORS.set(previous);
        }
        helper.assertTrue(ResearchCategories.getResearch("MIRROR") != null, "mirror research restored");
        helper.succeed();
    }
}
