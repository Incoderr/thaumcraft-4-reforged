package thaumcraft.entity.boss;

import net.minecraft.world.BossEvent;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.server.level.ServerPlayer;
import thaumcraft.research.Warp;
import thaumcraft.research.WarpType;
import thaumcraft.entity.monster.Cultist;
import thaumcraft.entity.projectile.EldritchOrb;
import thaumcraft.registry.ModSounds;
import thaumcraft.registry.ModBlocks;

public class EldritchWarden extends ThaumcraftBoss implements RangedAttackMob, thaumcraft.entity.EldritchMob {
    private static final EntityDataAccessor<Byte> DATA_TITLE = SynchedEntityData.defineId(EldritchWarden.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Float> DATA_ARM_LIFT_L = SynchedEntityData.defineId(EldritchWarden.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_ARM_LIFT_R = SynchedEntityData.defineId(EldritchWarden.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_FRENZY = SynchedEntityData.defineId(EldritchWarden.class, EntityDataSerializers.INT);
    private int title;
    private int frenzy;
    private boolean lastBlast;
    private boolean frenzyUsed;

    public EldritchWarden(EntityType<? extends EldritchWarden> type, Level level) {
        super(type, level, BossEvent.BossBarColor.PURPLE);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBossAttributes().add(Attributes.MAX_HEALTH, 200.0).add(Attributes.MOVEMENT_SPEED, 0.33).add(Attributes.ATTACK_DAMAGE, 10.0).add(Attributes.ARMOR, 4.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TITLE, (byte) 0);
        builder.define(DATA_ARM_LIFT_L, 0.0F);
        builder.define(DATA_ARM_LIFT_R, 0.0F);
        builder.define(DATA_FRENZY, 0);
    }

    public float getArmLiftLeft() {
        return this.entityData.get(DATA_ARM_LIFT_L);
    }

    public float getArmLiftRight() {
        return this.entityData.get(DATA_ARM_LIFT_R);
    }

    public int getFrenzyTicks() {
        return this.entityData.get(DATA_FRENZY);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0, 20, 40, 24.0F));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.1, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Cultist.class, true));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (!(this.level() instanceof net.minecraft.server.level.ServerLevel level)) {
            return;
        }
        if (this.random.nextFloat() > 0.2F) {
            this.lastBlast = !this.lastBlast;
            EldritchOrb orb = EldritchOrb.create(level, this, target, this.lastBlast);
            level.addFreshEntity(orb);
            this.level().broadcastEntityEvent(this, (byte) (this.lastBlast ? 16 : 15));
            this.playSound(ModSounds.ELDRITCH_GUARDIAN_ATTACK.get(), 2.0F, 1.0F + this.random.nextFloat() * 0.1F);
        } else if (this.hasLineOfSight(target)) {
            this.level().broadcastEntityEvent(this, (byte) 17);
            double yaw = Math.toRadians(this.getYRot());
            target.push(-Math.sin(yaw) * 1.5, 0.1, Math.cos(yaw) * 1.5);
            target.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.WITHER, 400), this);
            target.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.WEAKNESS, 400), this);
            if (target instanceof ServerPlayer player) {
                Warp.add(player, 3 + this.random.nextInt(3), WarpType.TEMPORARY);
            }
            this.playSound(ModSounds.ELDRITCH_GUARDIAN_SCREECH.get(), 4.0F, 1.0F + this.random.nextFloat() * 0.1F);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.frenzy > 0) {
            if (this.frenzy == 150 && this.hasHome()) {
                this.teleportTo(this.getHomePosition().getX() + 0.5, this.getHomePosition().getY(), this.getHomePosition().getZ() + 0.5);
                this.level().broadcastEntityEvent(this, (byte) 18);
            }
            if (this.frenzy < 121 && this.frenzy % 10 == 0 && this.level() instanceof net.minecraft.server.level.ServerLevel level) {
                double radius = (150 - this.frenzy) / 8.0;
                int stride = 1 + this.frenzy / 8;
                for (int angle = 0; angle < 180; angle += 2 * stride) {
                    int x = (int) Math.floor(this.getX() + radius * Math.cos(Math.toRadians(angle)));
                    int z = (int) Math.floor(this.getZ() + radius * Math.sin(Math.toRadians(angle)));
                    net.minecraft.core.BlockPos pos = new net.minecraft.core.BlockPos(x, this.getBlockY(), z);
                    if (level.isEmptyBlock(pos) && level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), net.minecraft.core.Direction.UP)) {
                        level.setBlockAndUpdate(pos, ModBlocks.SAPPING_FIELD.get().defaultBlockState());
                        level.scheduleTick(pos, ModBlocks.SAPPING_FIELD.get(), 250 + this.random.nextInt(150));
                    }
                }
            }
            this.frenzy--;
            this.entityData.set(DATA_FRENZY, this.frenzy);
        }
        if (this.level().isClientSide()) {
            this.entityData.set(DATA_ARM_LIFT_L, Math.max(0.0F, this.getArmLiftLeft() - 0.05F));
            this.entityData.set(DATA_ARM_LIFT_R, Math.max(0.0F, this.getArmLiftRight() - 0.05F));
        }
        if (!this.level().isClientSide() && this.tickCount % 25 == 0 && this.getAbsorptionAmount() < 132.0F) {
            this.setAbsorptionAmount(this.getAbsorptionAmount() + 1.0F);
        }
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return EntityDimensions.scalable(1.5F, 3.5F).withEyeHeight(3.1F);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason reason, @org.jspecify.annotations.Nullable SpawnGroupData data) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, data);
        this.title = this.random.nextInt(15);
        this.entityData.set(DATA_TITLE, (byte) this.title);
        this.setSpawnTimer(150);
        this.setAbsorptionAmount(this.getAbsorptionAmount() + (float) (this.getAttributeBaseValue(Attributes.MAX_HEALTH) * 0.66));
        return result;
    }

    @Override
    public boolean isInvulnerableTo(net.minecraft.server.level.ServerLevel level, net.minecraft.world.damagesource.DamageSource source) {
        return this.frenzy > 0 || super.isInvulnerableTo(level, source);
    }

    @Override
    public boolean hurtServer(net.minecraft.server.level.ServerLevel level, net.minecraft.world.damagesource.DamageSource source, float damage) {
        boolean hit = super.hurtServer(level, source, damage);
        if (hit && !this.frenzyUsed && this.getAbsorptionAmount() <= 0.0F) {
            this.frenzyUsed = true;
            this.frenzy = 150;
            this.entityData.set(DATA_FRENZY, 150);
        }
        return hit;
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return !(target instanceof Cultist) && !(target instanceof thaumcraft.entity.monster.eldritch.EldritchGuardian) && super.canAttack(target);
    }

    @Override
    public void handleEntityEvent(byte event) {
        if (event == 15) {
            this.entityData.set(DATA_ARM_LIFT_L, 0.5F);
        } else if (event == 16) {
            this.entityData.set(DATA_ARM_LIFT_R, 0.5F);
        } else if (event == 17) {
            this.entityData.set(DATA_ARM_LIFT_L, 0.9F);
            this.entityData.set(DATA_ARM_LIFT_R, 0.9F);
        } else if (event == 18 && this.level().isClientSide()) {
            for (int count = 0; count < 128; count++) {
                double progress = count / 127.0;
                double x = this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth() * 2.0;
                double y = this.getY() + progress * this.getBbHeight();
                double z = this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth() * 2.0;
                this.level().addParticle(net.minecraft.core.particles.ParticleTypes.PORTAL, x, y, z,
                    (this.random.nextFloat() - 0.5F) * 0.2F,
                    (this.random.nextFloat() - 0.5F) * 0.2F,
                    (this.random.nextFloat() - 0.5F) * 0.2F);
            }
        } else {
            super.handleEntityEvent(event);
        }
    }

    @Override
    public boolean doHurtTarget(net.minecraft.server.level.ServerLevel level, Entity target) {
        boolean hit = super.doHurtTarget(level, target);
        if (hit && this.frenzy > 0) {
            target.push(0.0, 0.6, 0.0);
        }
        return hit;
    }

    @Override
    protected void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("Title", this.title);
        output.putInt("FrenzyCounter", this.frenzy);
        output.putBoolean("LastBlast", this.lastBlast);
        output.putBoolean("FrenzyUsed", this.frenzyUsed);
    }

    @Override
    protected void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput input) {
        super.readAdditionalSaveData(input);
        this.title = Math.floorMod(input.getIntOr("Title", 0), 15);
        this.entityData.set(DATA_TITLE, (byte) this.title);
        this.lastBlast = input.getBooleanOr("LastBlast", false);
        this.frenzyUsed = input.getBooleanOr("FrenzyUsed", this.frenzy > 0);
        this.frenzy = input.getIntOr("FrenzyCounter", 0);
        this.entityData.set(DATA_FRENZY, this.frenzy);
    }
}
