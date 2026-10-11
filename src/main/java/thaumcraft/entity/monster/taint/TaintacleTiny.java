package thaumcraft.entity.monster.taint;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class TaintacleTiny extends Taintacle {
    public TaintacleTiny(EntityType<? extends TaintacleTiny> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Taintacle.createAttributes().add(Attributes.MAX_HEALTH, 6.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount >= 200) {
            this.discard();
        }
    }
}
