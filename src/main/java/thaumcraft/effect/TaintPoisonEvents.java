package thaumcraft.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import thaumcraft.Thaumcraft;
import thaumcraft.alchemy.AlchemyHooks;
import thaumcraft.entity.TaintedMob;
import thaumcraft.entity.monster.taint.TaintMobFactory;

@EventBusSubscriber(modid = Thaumcraft.MODID)
public final class TaintPoisonEvents {
    private TaintPoisonEvents() {
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity original = event.getEntity();
        if (!(original.level() instanceof ServerLevel level) || original instanceof TaintedMob || !EffectHooks.hasTaintPoison(original)) {
            return;
        }
        Entity replacement = TaintMobFactory.createReplacement(original);
        if (replacement != null) {
            if (level.addFreshEntity(replacement)) {
                return;
            }
            return;
        }
        int size = (int) (1.0F + Math.min(original.getMaxHealth() / 10.0F, 6.0F));
        AlchemyHooks.spawnThaumicSlime(level, original.blockPosition(), size);
    }
}
