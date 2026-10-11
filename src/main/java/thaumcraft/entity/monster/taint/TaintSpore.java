package thaumcraft.entity.monster.taint;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import thaumcraft.entity.TaintedMob;
import thaumcraft.registry.ModBlocks;
import thaumcraft.registry.ModEntities;

public class TaintSpore extends Monster implements TaintedMob {
    private static final EntityDataAccessor<Integer> DATA_SIZE = SynchedEntityData.defineId(TaintSpore.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_ATTACHED = SynchedEntityData.defineId(TaintSpore.class, EntityDataSerializers.BOOLEAN);
    private int outsideBiomeTicks;
    private int growthTicks;
    private BlockPos stalkPos;
    private boolean burst;

    public TaintSpore(EntityType<? extends TaintSpore> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 10.0)
            .add(Attributes.FLYING_SPEED, 0.25)
            .add(Attributes.MOVEMENT_SPEED, 0.25)
            .add(Attributes.ATTACK_DAMAGE, 2.0)
            .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    public static TaintSpore create(ServerLevel level, BlockPos stalkPos) {
        TaintSpore spore = ModEntities.TAINT_SPORE.get().create(level, net.minecraft.world.entity.EntitySpawnReason.TRIGGERED);
        if (spore == null) {
            return null;
        }
        spore.stalkPos = stalkPos;
        spore.entityData.set(DATA_ATTACHED, true);
        spore.setNoAi(true);
        spore.setPos(stalkPos.getX() + 0.5, stalkPos.getY() + 1.0, stalkPos.getZ() + 0.5);
        return spore;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_SIZE, 1);
        entityData.define(DATA_ATTACHED, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.entityData.get(DATA_ATTACHED)) {
            if (this.level().isClientSide()) {
                return;
            }
            if (this.stalkPos == null || !this.level().getBlockState(this.stalkPos).is(ModBlocks.MATURE_SPORE_STALK.get())) {
                this.burstIntoSpiders();
                return;
            }
            this.setPos(this.stalkPos.getX() + 0.5, this.stalkPos.getY() + 1.0, this.stalkPos.getZ() + 0.5);
            this.setDeltaMovement(0.0, 0.0, 0.0);
            if (++this.growthTicks >= 1200) {
                this.growthTicks = 0;
                if (this.entityData.get(DATA_SIZE) < 10) {
                    this.entityData.set(DATA_SIZE, this.entityData.get(DATA_SIZE) + 1);
                }
            }
            return;
        }
        if (this.tickCount % 20 == 0 && !this.level().isClientSide()) {
            if (!this.level().getBiome(this.blockPosition()).is(thaumcraft.taint.TaintBiomes.TAINTED_LAND)) {
                this.outsideBiomeTicks++;
                if (this.outsideBiomeTicks >= 30) {
                    this.hurt(this.damageSources().starve(), 1.0F);
                }
            } else {
                this.outsideBiomeTicks = 0;
            }
        }
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        this.burstIntoSpiders();
    }

    protected void burstIntoSpiders() {
        if (this.burst || this.level().isClientSide()) {
            return;
        }
        this.burst = true;
        ServerLevel level = (ServerLevel) this.level();
        int size = this.entityData.get(DATA_SIZE);
        int count = Math.max(1, size / 3 + level.getRandom().nextInt(size / 2 + 1));
        for (int index = 0; index < count; index++) {
            TaintSpider spider = ModEntities.TAINT_SPIDER.get().create(level, net.minecraft.world.entity.EntitySpawnReason.TRIGGERED);
            if (spider == null) {
                continue;
            }
            spider.moveTo(this.getX() + level.getRandom().nextFloat() - level.getRandom().nextFloat(), this.getY() + level.getRandom().nextFloat(),
                this.getZ() + level.getRandom().nextFloat() - level.getRandom().nextFloat(), level.getRandom().nextFloat() * 360.0F, 0.0F);
            level.addFreshEntity(spider);
        }
        if (this.stalkPos != null && level.getBlockState(this.stalkPos).is(ModBlocks.MATURE_SPORE_STALK.get())) {
            level.setBlockAndUpdate(this.stalkPos, ModBlocks.SPORE_STALK.get().defaultBlockState());
        }
        this.discard();
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float multiplier, net.minecraft.world.damagesource.DamageSource source) {
        return false;
    }
}
