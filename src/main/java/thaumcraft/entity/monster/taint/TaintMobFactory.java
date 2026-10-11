package thaumcraft.entity.monster.taint;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import thaumcraft.registry.ModEntities;

public final class TaintMobFactory {
    private TaintMobFactory() {
    }

    public static @Nullable Entity createReplacement(LivingEntity original) {
        EntityType<?> type = original.getType();
        EntityType<? extends Entity> replacementType;
        if (type == vanilla("chicken")) {
            replacementType = ModEntities.TAINTED_CHICKEN.get();
        } else if (type == vanilla("cow") || type == vanilla("mooshroom")) {
            replacementType = ModEntities.TAINTED_COW.get();
        } else if (type == vanilla("creeper")) {
            replacementType = ModEntities.TAINTED_CREEPER.get();
        } else if (type == vanilla("pig")) {
            replacementType = ModEntities.TAINTED_PIG.get();
        } else if (type == vanilla("sheep")) {
            replacementType = ModEntities.TAINTED_SHEEP.get();
        } else if (type == vanilla("villager")) {
            replacementType = ModEntities.TAINTED_VILLAGER.get();
        } else {
            return null;
        }
        Entity replacement = replacementType.create(original.level(), EntitySpawnReason.CONVERSION);
        if (replacement == null) {
            return null;
        }
        replacement.moveTo(original.getX(), original.getY(), original.getZ(), original.getYRot(), original.getXRot());
        replacement.setDeltaMovement(original.getDeltaMovement());
        replacement.setCustomName(original.getCustomName());
        replacement.setCustomNameVisible(original.isCustomNameVisible());
        if (replacement instanceof Mob mob) {
            mob.setPersistenceRequired();
        }
        return replacement;
    }

    private static EntityType<?> vanilla(String name) {
        return BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.fromNamespaceAndPath("minecraft", name));
    }
}
