package thaumcraft.test;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import thaumcraft.Thaumcraft;
import thaumcraft.effect.TaintPoisonEvents;
import thaumcraft.entity.TaintedMob;
import thaumcraft.entity.monster.taint.TaintMobFactory;
import thaumcraft.entity.monster.taint.Taintacle;
import thaumcraft.entity.monster.taint.TaintedCow;
import thaumcraft.registry.ModBlocks;
import thaumcraft.registry.ModEffects;
import thaumcraft.registry.ModEntities;
import thaumcraft.taint.TaintBiomes;
import thaumcraft.taint.TaintSpread;

@EventBusSubscriber(modid = Thaumcraft.MODID)
public final class TaintGameTests {
    private static final DeferredRegister<Consumer<GameTestHelper>> FUNCTIONS = DeferredRegister.create(Registries.TEST_FUNCTION, Thaumcraft.MODID);
    private static final String[] TESTS = {
        "taint_fibres_spread_to_replaceable_ground",
        "taint_flux_seeds_fibres_and_biome",
        "taint_bloom_restores_original_biome",
        "taint_tentacle_stays_stationary",
        "taint_poison_replacement_preserves_position",
        "taint_poison_conversion_removes_original",
        "taint_poison_ignores_tainted_mobs"
    };

    static {
        FUNCTIONS.register(TESTS[0], () -> TaintGameTests::fibresSpreadToReplaceableGround);
        FUNCTIONS.register(TESTS[1], () -> TaintGameTests::fluxSeedsFibresAndBiome);
        FUNCTIONS.register(TESTS[2], () -> TaintGameTests::bloomRestoresOriginalBiome);
        FUNCTIONS.register(TESTS[3], () -> TaintGameTests::tentacleStaysStationary);
        FUNCTIONS.register(TESTS[4], () -> TaintGameTests::poisonReplacementPreservesPosition);
        FUNCTIONS.register(TESTS[5], () -> TaintGameTests::poisonConversionRemovesOriginal);
        FUNCTIONS.register(TESTS[6], () -> TaintGameTests::poisonIgnoresTaintedMobs);
    }

    private TaintGameTests() {
    }

    public static void register(IEventBus bus) {
        FUNCTIONS.register(bus);
    }

    @SubscribeEvent
    static void registerTests(RegisterGameTestsEvent event) {
        var environment = event.registerEnvironment(Thaumcraft.id("taint_default"), new TestEnvironmentDefinition.AllOf());
        for (String name : TESTS) {
            ResourceKey<Consumer<GameTestHelper>> function = ResourceKey.create(Registries.TEST_FUNCTION, Thaumcraft.id(name));
            event.registerTest(
                Thaumcraft.id(name),
                new FunctionGameTestInstance(function, new TestData<>(environment, Thaumcraft.id("test/platform"), 100, 0, true))
            );
        }
    }

    private static void fibresSpreadToReplaceableGround(GameTestHelper helper) {
        BlockPos target = new BlockPos(3, 2, 3);
        helper.setBlock(target.below(), Blocks.STONE);
        helper.setBlock(target.east(), ModBlocks.FIBROUS_TAINT.get());
        BlockPos worldTarget = helper.absolutePos(target);
        helper.assertTrue(TaintSpread.spreadFibres(helper.getLevel(), worldTarget), "fibres rejected replaceable air next to solid support");
        helper.assertTrue(TaintSpread.isTaintBlock(helper.getBlockState(target)), "fibres spread did not place a taint block");
        helper.succeed();
    }

    private static void fluxSeedsFibresAndBiome(GameTestHelper helper) {
        BlockPos target = new BlockPos(3, 2, 3);
        helper.setBlock(target.below(), Blocks.STONE);
        BlockPos worldTarget = helper.absolutePos(target);
        helper.assertTrue(TaintSpread.taintFromFlux(helper.getLevel(), worldTarget), "flux failed to seed taint");
        helper.assertTrue(TaintBiomes.isTainted(helper.getLevel(), worldTarget), "flux did not taint the biome column");
        helper.assertTrue(helper.getBlockState(target).is(ModBlocks.FIBROUS_TAINT.get()), "flux did not place fibres on supported air");
        helper.succeed();
    }

    private static void bloomRestoresOriginalBiome(GameTestHelper helper) {
        BlockPos target = new BlockPos(3, 2, 3);
        BlockPos worldTarget = helper.absolutePos(target);
        var original = helper.getLevel().getBiome(worldTarget);
        helper.assertTrue(TaintBiomes.set(helper.getLevel(), worldTarget, TaintBiomes.TAINTED_LAND), "test setup failed to taint biome column");
        helper.assertTrue(TaintBiomes.purify(helper.getLevel(), worldTarget), "purification did not restore a biome column");
        var expected = original.is(TaintBiomes.TAINTED_LAND)
            ? helper.getLevel().registryAccess().lookupOrThrow(Registries.BIOME).getOrThrow(net.minecraft.world.level.biome.Biomes.PLAINS)
            : original;
        helper.assertValueEqual(helper.getLevel().getBiome(worldTarget), expected, "purification did not restore the original biome source");
        helper.succeed();
    }

    private static void tentacleStaysStationary(GameTestHelper helper) {
        Taintacle tentacle = ModEntities.TAINTACLE.get().create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
        helper.assertTrue(tentacle != null, "failed to create taintacle");
        BlockPos worldPos = helper.absolutePos(new BlockPos(3, 2, 3));
        tentacle.absSnapTo(worldPos.getX() + 0.5, worldPos.getY(), worldPos.getZ() + 0.5, 0.0F, 0.0F);
        helper.getLevel().addFreshEntity(tentacle);
        tentacle.move(MoverType.SELF, new Vec3(2.0, 2.0, -3.0));
        helper.assertValueEqual(tentacle.getX(), worldPos.getX() + 0.5, "taintacle accepted horizontal movement");
        helper.assertValueEqual(tentacle.getZ(), worldPos.getZ() + 0.5, "taintacle accepted horizontal movement");
        helper.assertValueEqual(tentacle.getY(), (double) worldPos.getY(), "taintacle accepted upward movement");
        helper.succeed();
    }

    private static void poisonReplacementPreservesPosition(GameTestHelper helper) {
        Cow original = EntityTypes.COW.create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
        helper.assertTrue(original != null, "failed to create test cow");
        BlockPos worldPos = helper.absolutePos(new BlockPos(2, 2, 4));
        original.absSnapTo(worldPos.getX() + 0.25, worldPos.getY(), worldPos.getZ() + 0.75, 47.0F, 0.0F);
        Entity replacement = TaintMobFactory.createReplacement(original);
        helper.assertTrue(replacement instanceof TaintedCow, "cow replacement did not create tainted cow");
        helper.assertTrue(replacement instanceof TaintedMob, "replacement does not implement tainted mob contract");
        helper.assertValueEqual(replacement.getX(), original.getX(), "replacement changed source x coordinate");
        helper.assertValueEqual(replacement.getY(), original.getY(), "replacement changed source y coordinate");
        helper.assertValueEqual(replacement.getZ(), original.getZ(), "replacement changed source z coordinate");
        helper.assertValueEqual(replacement.getYRot(), original.getYRot(), "replacement changed source yaw");
        helper.succeed();
    }

    private static void poisonConversionRemovesOriginal(GameTestHelper helper) {
        Cow original = EntityTypes.COW.create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
        helper.assertTrue(original != null, "failed to create test cow");
        BlockPos worldPos = helper.absolutePos(new BlockPos(2, 2, 4));
        double x = worldPos.getX() + 0.25;
        double y = worldPos.getY();
        double z = worldPos.getZ() + 0.75;
        original.absSnapTo(x, y, z, 47.0F, 16.0F);
        helper.getLevel().addFreshEntity(original);
        original.addEffect(new MobEffectInstance(ModEffects.TAINT_POISON, 100, 0));
        original.hurtServer(helper.getLevel(), helper.getLevel().damageSources().genericKill(), original.getMaxHealth() + 1.0F);
        helper.assertTrue(original.isRemoved(), "original entity was not removed after replacement");
        var replacements = helper.getLevel().getEntitiesOfClass(TaintedCow.class, original.getBoundingBox().inflate(2.0));
        helper.assertValueEqual(replacements.size(), 1, "poison conversion should add exactly one tainted cow");
        TaintedCow replacement = replacements.getFirst();
        helper.assertValueEqual(replacement.getX(), x, "replacement changed source x coordinate");
        helper.assertValueEqual(replacement.getY(), y, "replacement changed source y coordinate");
        helper.assertValueEqual(replacement.getZ(), z, "replacement changed source z coordinate");
        helper.assertValueEqual(replacement.getYRot(), 47.0F, "replacement changed source yaw");
        helper.assertValueEqual(replacement.getXRot(), 0.0F, "replacement did not reset source pitch");
        helper.succeed();
    }

    private static void poisonIgnoresTaintedMobs(GameTestHelper helper) {
        TaintedCow tainted = ModEntities.TAINTED_COW.get().create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
        helper.assertTrue(tainted != null, "failed to create tainted cow");
        BlockPos worldPos = helper.absolutePos(new BlockPos(3, 2, 3));
        tainted.absSnapTo(worldPos.getX() + 0.5, worldPos.getY(), worldPos.getZ() + 0.5, 0.0F, 0.0F);
        helper.getLevel().addFreshEntity(tainted);
        tainted.addEffect(new MobEffectInstance(ModEffects.TAINT_POISON, 100, 0));
        TaintPoisonEvents.onLivingDeath(new LivingDeathEvent(tainted, helper.getLevel().damageSources().generic()));
        helper.assertTrue(!tainted.isRemoved(), "tainted mob should be immune to poison death replacement");
        helper.assertValueEqual(
            helper.getLevel().getEntitiesOfClass(TaintedCow.class, tainted.getBoundingBox().inflate(2.0)).size(),
            1,
            "tainted mob should not create another replacement"
        );
        helper.succeed();
    }
}
