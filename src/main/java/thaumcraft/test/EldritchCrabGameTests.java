package thaumcraft.test;

import java.util.function.Consumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.GameType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import thaumcraft.Thaumcraft;
import thaumcraft.entity.monster.eldritch.EldritchCrab;
import thaumcraft.registry.ModEntities;

@EventBusSubscriber(modid = Thaumcraft.MODID)
public final class EldritchCrabGameTests {
    private static final DeferredRegister<Consumer<GameTestHelper>> FUNCTIONS = DeferredRegister.create(Registries.TEST_FUNCTION, Thaumcraft.MODID);
    private static final String[] TESTS = {"eldritch_crab_breaks_helm", "eldritch_crab_mounts_revenge_target"};

    static {
        FUNCTIONS.register(TESTS[0], () -> EldritchCrabGameTests::eldritchCrabBreaksHelm);
        FUNCTIONS.register(TESTS[1], () -> EldritchCrabGameTests::eldritchCrabMountsRevengeTarget);
    }

    private EldritchCrabGameTests() {
    }

    public static void register(IEventBus bus) {
        FUNCTIONS.register(bus);
    }

    @SubscribeEvent
    static void registerTests(RegisterGameTestsEvent event) {
        var environment = event.registerEnvironment(Thaumcraft.id("crab_default"), new TestEnvironmentDefinition.AllOf());
        for (String name : TESTS) {
            ResourceKey<Consumer<GameTestHelper>> function = ResourceKey.create(Registries.TEST_FUNCTION, Thaumcraft.id(name));
            event.registerTest(Thaumcraft.id(name), new FunctionGameTestInstance(
                function,
                new TestData<>(environment, Thaumcraft.id("test/platform"), 200, 0, true)
            ));
        }
    }

    private static void eldritchCrabBreaksHelm(GameTestHelper helper) {
        EldritchCrab crab = ModEntities.ELDRITCH_CRAB.get().create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
        helper.assertTrue(crab != null, "eldritch crab factory returned null");
        crab.snapTo(helper.absolutePos(new net.minecraft.core.BlockPos(2, 2, 2)), 0.0F, 0.0F);
        helper.getLevel().addFreshEntity(crab);
        crab.setHelm(true);
        helper.assertTrue(crab.hurtServer(helper.getLevel(), helper.getLevel().damageSources().generic(), 20.0F), "damage did not hit the crab");
        helper.assertFalse(crab.hasHelm(), "helm did not break at half health");
        helper.assertValueEqual(crab.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED), 0.3, "unhelmeted movement speed");
        helper.assertValueEqual(crab.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.ARMOR), 0.0, "unhelmeted armor");
        helper.succeed();
    }

    private static void eldritchCrabMountsRevengeTarget(GameTestHelper helper) {
        EldritchCrab crab = ModEntities.ELDRITCH_CRAB.get().create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
        helper.assertTrue(crab != null, "eldritch crab factory returned null");
        Player target = helper.makeMockPlayer(GameType.SURVIVAL);
        net.minecraft.core.BlockPos targetPos = helper.absolutePos(new net.minecraft.core.BlockPos(2, 2, 2));
        target.snapTo(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, 0.0F, 0.0F);
        helper.getLevel().addFreshEntity(target);
        crab.snapTo(targetPos.getX() + 0.5, targetPos.getY() + 1.5, targetPos.getZ() + 0.5, 0.0F, 0.0F);
        helper.getLevel().addFreshEntity(crab);
        crab.setLastHurtByMob(target);
        crab.setHelm(false);
        crab.tick();
        helper.assertTrue(crab.getVehicle() == target, "crab did not mount its airborne revenge target");
        helper.succeed();
    }
}
