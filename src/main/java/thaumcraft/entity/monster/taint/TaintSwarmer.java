package thaumcraft.entity.monster.taint;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import thaumcraft.registry.ModEntities;

public class TaintSwarmer extends TaintSpore {
    private boolean burst;

    public TaintSwarmer(EntityType<? extends TaintSwarmer> type, Level level) {
        super(type, level);
    }

    public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
        return TaintSpore.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 16.0);
    }

    @Override
    protected void burstIntoSpiders() {
    }

    @Override
    public void die(net.minecraft.world.damagesource.DamageSource source) {
        if (!this.level().isClientSide() && !this.burst) {
            this.burst = true;
            thaumcraft.entity.monster.taint.TaintSwarm swarm = ModEntities.TAINT_SWARM.get().create(this.level(), net.minecraft.world.entity.EntitySpawnReason.TRIGGERED);
            if (swarm != null) {
                swarm.moveTo(this.getX(), this.getY() + 0.5, this.getZ(), this.getYRot(), this.getXRot());
                this.level().addFreshEntity(swarm);
            }
        }
        super.die(source);
    }
}
