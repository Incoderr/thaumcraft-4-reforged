package thaumcraft.entity.monster.taint;

import net.minecraft.world.entity.EntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import thaumcraft.entity.TaintedMob;
import thaumcraft.registry.ModEntities;
import thaumcraft.taint.TaintBiomes;
import thaumcraft.taint.TaintSpread;

public class Taintacle extends Monster implements TaintedMob {
    public Taintacle(EntityType<? extends Taintacle> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 30.0)
            .add(Attributes.MOVEMENT_SPEED, 0.18)
            .add(Attributes.ATTACK_DAMAGE, 7.0)
            .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static boolean canSpawnNaturally(EntityType<Taintacle> type, ServerLevelAccessor level, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        if (!level.getBiome(pos).is(TaintBiomes.TAINTED_LAND)
            || !level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), net.minecraft.core.Direction.UP)) {
            return false;
        }
        AABB area = new AABB(pos).inflate(16.0);
        return level.getEntitiesOfClass(Taintacle.class, area).size() < 4;
    }

    @Override
    public boolean hurtServer(ServerLevel level, net.minecraft.world.damagesource.DamageSource source, float damage) {
        if (!(this instanceof TaintacleTiny) && source.getEntity() instanceof net.minecraft.world.entity.Entity attacker
            && this.distanceToSqr(attacker) > 256.0 && TaintBiomes.isTainted(level, attacker.blockPosition())
            && (TaintSpread.isTaintBlock(level.getBlockState(attacker.blockPosition()))
                || TaintSpread.isTaintBlock(level.getBlockState(attacker.blockPosition().below())))) {
            TaintacleTiny tiny = ModEntities.TAINTACLE_TINY.get().create(level, net.minecraft.world.entity.EntitySpawnReason.TRIGGERED);
            if (tiny != null) {
                tiny.moveTo(attacker.getX(), attacker.getY(), attacker.getZ(), this.random.nextFloat() * 360.0F, 0.0F);
                level.addFreshEntity(tiny);
            }
        }
        return super.hurtServer(level, source, damage);
    }
}
