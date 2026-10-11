package thaumcraft.entity.monster.taint;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class TaintedChicken extends TaintedAnimalMonster {
    public TaintedChicken(EntityType<? extends TaintedChicken> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return TaintedAnimalMonster.createAttributes(8.0, 0.4, 3.0, 2.0);
    }

    @Override
    protected void dropCustomDeathLoot(net.minecraft.server.level.ServerLevel level, net.minecraft.world.damagesource.DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        this.dropTaintResource(level, 1, 0.25F);
    }
}
