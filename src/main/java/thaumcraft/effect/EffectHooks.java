package thaumcraft.effect;

import net.minecraft.world.entity.LivingEntity;

public final class EffectHooks {
    private EffectHooks() {
    }

    public static void applyVisExhaust(LivingEntity entity, int duration, int amplifier) {
    }

    public static void applyTaintPoison(LivingEntity entity, int duration, int amplifier) {
    }

    public static boolean isTainted(LivingEntity entity) {
        return false;
    }
}
