package thaumcraft.entity.monster.taint;

import java.util.List;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import thaumcraft.effect.EffectHooks;
import thaumcraft.entity.TaintedMob;

public class TaintedCreeper extends Creeper implements TaintedMob {
    public TaintedCreeper(EntityType<? extends TaintedCreeper> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Creeper.createAttributes().add(Attributes.MAX_HEALTH, 20.0);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide() && this.getSwellDir() > 0 && this.tickCount % 10 == 0) {
            List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.blockPosition()).inflate(3.0));
            for (LivingEntity target : targets) {
                if (target != this && !(target instanceof TaintedMob) && !target.getType().builtInRegistryHolder().is(net.minecraft.tags.EntityTypeTags.UNDEAD)) {
                    EffectHooks.applyTaintPoison(target, 100, 0);
                }
            }
        }
    }
}
