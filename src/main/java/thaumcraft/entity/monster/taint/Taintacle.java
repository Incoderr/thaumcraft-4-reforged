package thaumcraft.entity.monster.taint;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import thaumcraft.entity.TaintedMob;
import thaumcraft.registry.ModBlocks;
import thaumcraft.registry.ModEntities;
import thaumcraft.taint.TaintBiomes;
import thaumcraft.taint.TaintSpread;

public class Taintacle extends Monster implements TaintedMob {
    @Nullable
    private LivingEntity target;
    private int attackTime;

    public Taintacle(EntityType<? extends Taintacle> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 50.0).add(Attributes.ATTACK_DAMAGE, 7.0);
    }

    @Override
    protected void registerGoals() {
    }

    public static boolean canSpawnNaturally(EntityType<Taintacle> type, ServerLevelAccessor level, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        if (!level.getBiome(pos).is(TaintBiomes.TAINTED_LAND)) {
            return false;
        }
        if (!level.getBlockState(pos).is(ModBlocks.FIBROUS_TAINT.get()) && !level.getBlockState(pos).is(ModBlocks.TAINTED_SOIL.get())) {
            return false;
        }
        AABB area = new AABB(pos).inflate(24.0, 8.0, 24.0);
        return level.getEntitiesOfClass(Taintacle.class, area).isEmpty();
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public void travel(Vec3 input) {
        super.travel(new Vec3(0.0, Math.min(input.y, 0.0), 0.0));
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            return;
        }
        if (this.tickCount % 20 == 0 && !this.level().getBiome(this.blockPosition()).is(TaintBiomes.TAINTED_LAND)) {
            this.hurt(this.damageSources().starve(), 1.0F);
        }
        if (this.attackTime > 0) {
            this.attackTime--;
        }
        if (this.target == null || !this.target.isAlive() || this.target instanceof TaintedMob || !this.isAgitated()) {
            this.target = this.findTarget();
        }
        if (this.target == null || this.attackTime > 0 || !this.hasLineOfSight(this.target)) {
            return;
        }
        double distance = this.distanceTo(this.target);
        if (distance <= this.getBbHeight() && verticalOverlap(this, this.target)) {
            this.attackTime = 20;
            this.doHurtTarget((ServerLevel) this.level(), this.target);
        } else if (distance > this.getBbHeight() && this.target.onGround()) {
            this.spawnTentacle(this.target);
        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (!(this instanceof TaintacleTiny) && source.getEntity() instanceof Entity attacker && this.distanceTo(attacker) > 16.0) {
            this.spawnTentacle(attacker);
        }
        return super.hurtServer(level, source, damage);
    }

    protected void spawnTentacle(Entity victim) {
        if (this instanceof TaintacleTiny || !(this.level() instanceof ServerLevel level)) {
            return;
        }
        BlockPos pos = victim.blockPosition();
        boolean onTaint = TaintBiomes.isTainted(level, pos)
            && (TaintSpread.isTaintBlock(level.getBlockState(pos)) || TaintSpread.isTaintBlock(level.getBlockState(pos.below())));
        if (!onTaint && !level.getBiome(pos).is(TaintBiomes.EERIE)) {
            return;
        }
        this.attackTime = 40 + level.getRandom().nextInt(20);
        TaintacleTiny tiny = ModEntities.TAINTACLE_TINY.get().create(level, EntitySpawnReason.TRIGGERED);
        if (tiny != null) {
            tiny.absMoveTo(victim.getX() + level.getRandom().nextFloat() - level.getRandom().nextFloat(), victim.getY(),
                victim.getZ() + level.getRandom().nextFloat() - level.getRandom().nextFloat(), 0.0F, 0.0F);
            level.addFreshEntity(tiny);
        }
        if (!onTaint && level.isEmptyBlock(pos) && hasAdjacentSolid(level, pos)) {
            TaintBiomes.set(level, pos, TaintBiomes.TAINTED_LAND);
            level.setBlockAndUpdate(pos, ModBlocks.FIBROUS_TAINT.get().defaultBlockState());
        }
    }

    @Nullable
    private LivingEntity findTarget() {
        double radius = this.getBbHeight() * 6.0;
        AABB area = new AABB(this.blockPosition()).inflate(radius, this.getBbHeight() * 3.0, radius);
        return this.level().getEntitiesOfClass(LivingEntity.class, area, living -> living != this && !(living instanceof TaintedMob))
            .stream().min(java.util.Comparator.comparingDouble(this::distanceToSqr)).orElse(null);
    }

    private boolean isAgitated() {
        return this.target != null && this.distanceToSqr(this.target) < this.getBbHeight() * 7.0 * this.getBbHeight() * 7.0;
    }

    private static boolean verticalOverlap(Entity left, Entity right) {
        return right.getBoundingBox().maxY > left.getBoundingBox().minY && right.getBoundingBox().minY < left.getBoundingBox().maxY;
    }

    private static boolean hasAdjacentSolid(ServerLevel level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos neighbor = pos.relative(direction);
            if (level.getBlockState(neighbor).isFaceSturdy(level, neighbor, direction.getOpposite())) {
                return true;
            }
        }
        return false;
    }
}
