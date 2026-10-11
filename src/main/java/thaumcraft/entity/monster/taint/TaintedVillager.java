package thaumcraft.entity.monster.taint;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class TaintedVillager extends TaintedAnimalMonster {
    public TaintedVillager(EntityType<? extends TaintedVillager> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return TaintedAnimalMonster.createAttributes(30.0, 0.3, 4.0, 0.0);
    }

    @Override
    protected void dropCustomDeathLoot(net.minecraft.server.level.ServerLevel level, net.minecraft.world.damagesource.DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        this.dropTaintResource(level, 2, 0.5F);
        int looting = source.getEntity() instanceof net.minecraft.world.entity.LivingEntity killer
            ? killer.getMainHandItem().getEnchantmentLevel(level.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT)
                .getOrThrow(net.minecraft.world.item.enchantment.Enchantments.LOOTING))
            : 0;
        if (level.getRandom().nextInt(13) < 1 + looting) {
            this.spawnAtLocation(thaumcraft.registry.ModItems.GOLD_COIN.get());
        }
    }
}
