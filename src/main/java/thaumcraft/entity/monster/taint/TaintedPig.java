package thaumcraft.entity.monster.taint;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class TaintedPig extends TaintedAnimalMonster {
    public TaintedPig(EntityType<? extends TaintedPig> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return TaintedAnimalMonster.createAttributes().add(Attributes.MAX_HEALTH, 16.0).add(Attributes.ATTACK_DAMAGE, 3.0);
    }
}
