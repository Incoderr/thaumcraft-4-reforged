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
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import thaumcraft.Thaumcraft;
import thaumcraft.entity.monster.ChampionModifier;
import thaumcraft.world.gen.HalloweenFirebatSpawnModifier;

@EventBusSubscriber(modid = Thaumcraft.MODID)
public final class ChampionGameTests {
    private static final DeferredRegister<Consumer<GameTestHelper>> FUNCTIONS = DeferredRegister.create(Registries.TEST_FUNCTION, Thaumcraft.MODID);
    private static final String TEST = "champion_modifiers_apply_once";

    static {
        FUNCTIONS.register(TEST, () -> ChampionGameTests::modifiersApplyOnce);
    }

    private ChampionGameTests() {
    }

    public static void register(IEventBus bus) {
        FUNCTIONS.register(bus);
    }

    @SubscribeEvent
    static void registerTests(RegisterGameTestsEvent event) {
        var environment = event.registerEnvironment(Thaumcraft.id("champion_modifiers"), new TestEnvironmentDefinition.AllOf());
        ResourceKey<Consumer<GameTestHelper>> function = ResourceKey.create(Registries.TEST_FUNCTION, Thaumcraft.id(TEST));
        event.registerTest(Thaumcraft.id(TEST), new FunctionGameTestInstance(
            function,
            new TestData<>(environment, Thaumcraft.id("test/platform"), 100, 0, true)
        ));
    }

    private static void modifiersApplyOnce(GameTestHelper helper) {
        helper.assertValueEqual(ChampionModifier.count(), 13, "champion modifier count");
        helper.assertValueEqual(ChampionModifier.names().getFirst(), "bold", "champion modifier id 0");
        helper.assertValueEqual(ChampionModifier.names().get(12), "infested", "champion modifier id 12");
        helper.assertTrue(HalloweenFirebatSpawnModifier.isHalloween(10, 31), "Halloween seasonal spawn date");
        helper.assertFalse(HalloweenFirebatSpawnModifier.isHalloween(10, 30), "seasonal spawn must not include October 30");
        helper.assertFalse(HalloweenFirebatSpawnModifier.isHalloween(11, 1), "seasonal spawn must not include November 1");

        var zombieType = BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.withDefaultNamespace("zombie"));
        for (int id = 0; id < ChampionModifier.count(); id++) {
            Mob mob = (Mob) zombieType.create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
            helper.assertTrue(mob != null, "zombie factory returned null for champion id " + id);
            mob.snapTo(helper.absolutePos(new BlockPos(2 + id % 4, 2, 2 + id / 4)), 0.0F, 0.0F);
            double baseDamage = mob.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
            ChampionModifier.assign(mob, id, false);
            ChampionModifier.assign(mob, id, false);
            helper.assertValueEqual(ChampionModifier.get(mob), id, "champion attachment id " + id);
            helper.assertValueEqual(mob.getAttributeValue(Attributes.MAX_HEALTH), mob.getAttributeBaseValue(Attributes.MAX_HEALTH) + 30.0, "champion health bonus id " + id);
            double expectedDamage = baseDamage * (id == 3 ? 12.0 : 3.0);
            helper.assertValueEqual(mob.getAttributeValue(Attributes.ATTACK_DAMAGE), expectedDamage, "champion attack modifier id " + id);
            if (id == 0) {
                helper.assertValueEqual(mob.getAttributeValue(Attributes.MOVEMENT_SPEED), mob.getAttributeBaseValue(Attributes.MOVEMENT_SPEED) * 1.3, "bold speed bonus");
            }
            helper.getLevel().addFreshEntity(mob);
        }
        helper.succeed();
    }
}
