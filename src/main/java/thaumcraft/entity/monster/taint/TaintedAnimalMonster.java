package thaumcraft.entity.monster.taint;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import thaumcraft.registry.ModItems;
import thaumcraft.entity.TaintedMob;

public abstract class TaintedAnimalMonster extends Monster implements TaintedMob {
    protected TaintedAnimalMonster(EntityType<? extends TaintedAnimalMonster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes(double health, double speed, double damage, double armor) {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, health)
            .add(Attributes.MOVEMENT_SPEED, speed)
            .add(Attributes.ATTACK_DAMAGE, damage)
            .add(Attributes.ARMOR, armor);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new net.minecraft.world.entity.ai.goal.FloatGoal(this));
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Animal.class, true));
    }

    protected void dropTaintResource(ServerLevel level, int chanceDenominator, float gooChance) {
        if (level.getRandom().nextInt(chanceDenominator) == 0) {
            Item item = level.getRandom().nextFloat() < gooChance ? ModItems.TAINTED_GOO.get() : ModItems.TAINT_TENDRIL.get();
            this.spawnAtLocation(new ItemStack(item));
        }
    }
}
