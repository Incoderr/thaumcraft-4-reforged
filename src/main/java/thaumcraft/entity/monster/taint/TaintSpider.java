package thaumcraft.entity.monster.taint;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.level.Level;
import thaumcraft.entity.TaintedMob;

public class TaintSpider extends Spider implements TaintedMob {
    public TaintSpider(EntityType<? extends TaintSpider> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return net.minecraft.world.entity.monster.Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 5.0)
            .add(Attributes.MOVEMENT_SPEED, 0.3)
            .add(Attributes.FOLLOW_RANGE, 12.0)
            .add(Attributes.ATTACK_DAMAGE, 2.0);
    }
}
