package thaumcraft.entity.boss;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import thaumcraft.Thaumcraft;
import thaumcraft.registry.EldritchContent;
import thaumcraft.registry.ModItems;
import net.minecraft.world.item.ItemStack;

public abstract class ThaumcraftBoss extends Monster {
    private static final EntityDataAccessor<Integer> DATA_ANGER = SynchedEntityData.defineId(ThaumcraftBoss.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_SPAWN_TIMER = SynchedEntityData.defineId(ThaumcraftBoss.class, EntityDataSerializers.INT);
    private static final AttributeModifier[] HEALTH_SCALING = new AttributeModifier[5];
    private static final AttributeModifier[] DAMAGE_SCALING = new AttributeModifier[5];

    static {
        for (int index = 0; index < 5; index++) {
            HEALTH_SCALING[index] = new AttributeModifier(Thaumcraft.id("boss/health_" + index), 50.0, AttributeModifier.Operation.ADD_VALUE);
            DAMAGE_SCALING[index] = new AttributeModifier(Thaumcraft.id("boss/damage_" + index), 0.5, AttributeModifier.Operation.ADD_VALUE);
        }
    }
    private final ServerBossEvent bossEvent;
    private final Map<Integer, Integer> aggro = new HashMap<>();
    private int spawnTimer;

    protected ThaumcraftBoss(EntityType<? extends ThaumcraftBoss> type, Level level, BossEvent.BossBarColor color) {
        super(type, level);
        this.xpReward = 50;
        this.setCanPickUpLoot(false);
        this.bossEvent = new ServerBossEvent(java.util.UUID.randomUUID(), this.getDisplayName(), color, BossEvent.BossBarOverlay.PROGRESS);
    }

    public static AttributeSupplier.Builder createBossAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.KNOCKBACK_RESISTANCE, 0.95).add(Attributes.FOLLOW_RANGE, 40.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ANGER, 0);
        builder.define(DATA_SPAWN_TIMER, 0);
    }

    public int getAnger() {
        return this.entityData.get(DATA_ANGER);
    }

    public void setAnger(int anger) {
        this.entityData.set(DATA_ANGER, Math.max(0, Math.min(Short.MAX_VALUE, anger)));
    }

    public int getSpawnTimer() {
        return this.entityData.get(DATA_SPAWN_TIMER);
    }

    public void setSpawnTimer(int ticks) {
        this.spawnTimer = Math.max(0, ticks);
        this.entityData.set(DATA_SPAWN_TIMER, this.spawnTimer);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            return;
        }
        if (this.spawnTimer > 0) {
            this.spawnTimer--;
            this.entityData.set(DATA_SPAWN_TIMER, this.spawnTimer);
        }
        if (this.getAnger() > 0) {
            this.setAnger(this.getAnger() - 1);
        }
        if (this.tickCount % 30 == 0) {
            this.heal(1.0F);
        }
        this.bossEvent.setProgress(this.getMaxHealth() <= 0.0F ? 0.0F : this.getHealth() / this.getMaxHealth());
        if (this.tickCount % 20 == 0 && this.getTarget() != null) {
            this.updateAggroTargetAndScaling();
        }
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        if (this.getSpawnTimer() <= 0) {
            super.customServerAiStep(level);
        }
    }

    private void updateAggroTargetAndScaling() {
        LivingEntity current = this.getTarget();
        int currentAggro = current == null ? 0 : this.aggro.getOrDefault(current.getId(), 0);
        LivingEntity candidate = current;
        int highestAggro = currentAggro;
        int playerCount = 0;
        for (Map.Entry<Integer, Integer> entry : this.aggro.entrySet()) {
            if (this.level().getEntity(entry.getKey()) instanceof LivingEntity living && living.isAlive() && this.distanceToSqr(living) <= 16384.0) {
                if (living instanceof Player) {
                    playerCount++;
                }
                int threat = entry.getValue();
                if (threat > currentAggro + 25 && threat > currentAggro * 1.1 && threat > highestAggro) {
                    candidate = living;
                    highestAggro = threat;
                }
            }
        }
        if (candidate != null) {
            this.setTarget(candidate);
        }
        this.aggro.entrySet().removeIf(entry -> !(this.level().getEntity(entry.getKey()) instanceof LivingEntity living) || !living.isAlive());
        this.bossEvent.setVisible(true);
        float oldMax = this.getMaxHealth();
        float healthRatio = oldMax <= 0.0F ? 0.0F : this.getHealth() / oldMax;
        for (int index = 0; index < 5; index++) {
            this.getAttribute(Attributes.MAX_HEALTH).removeModifier(HEALTH_SCALING[index].id());
            this.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(DAMAGE_SCALING[index].id());
        }
        for (int index = 0; index < Math.min(5, playerCount - 1); index++) {
            this.getAttribute(Attributes.MAX_HEALTH).addOrUpdateTransientModifier(HEALTH_SCALING[index]);
            this.getAttribute(Attributes.ATTACK_DAMAGE).addOrUpdateTransientModifier(DAMAGE_SCALING[index]);
        }
        this.setHealth(this.getMaxHealth() * healthRatio);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (source.getEntity() instanceof LivingEntity living) {
            this.aggro.merge(living.getId(), (int) damage, Integer::sum);
        }
        if (damage > 35.0F) {
            if (this.getAnger() == 0) {
                this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, (int) (damage / 15.0F)));
                this.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 200, (int) (damage / 40.0F)));
                this.addEffect(new MobEffectInstance(MobEffects.SPEED, 200, (int) (damage / 40.0F)));
                this.setAnger(200);
            }
            damage = 35.0F;
        }
        return super.hurtServer(level, source, damage);
    }

    @Override
    public boolean isInvulnerableTo(ServerLevel level, DamageSource source) {
        return this.spawnTimer > 0 || super.isInvulnerableTo(level, source);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected int increaseAirSupply(int air) {
        return air;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return this.spawnTimer <= 0 && super.isPushable();
    }

    @Override
    public boolean considersEntityAsAlly(net.minecraft.world.entity.Entity entity) {
        return entity instanceof thaumcraft.entity.EldritchMob || super.considersEntityAsAlly(entity);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData data) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, data);
        this.setHomeTo(this.blockPosition(), 24);
        return result;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        this.bossEvent.setVisible(false);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        this.spawnAtLocation(level, new ItemStack(EldritchContent.PRIMORDIAL_PEARL.get()));
        this.spawnAtLocation(level, new ItemStack(ModItems.RARE_LOOT_BAG.get()));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("BossSpawnTimer", this.spawnTimer);
        output.putInt("BossAnger", this.getAnger());
        if (this.hasHome() && this.getHomeRadius() > 0) {
            BlockPos pos = this.getHomePosition();
            output.putInt("HomeD", this.getHomeRadius());
            output.putInt("HomeX", pos.getX());
            output.putInt("HomeY", pos.getY());
            output.putInt("HomeZ", pos.getZ());
        }
        output.store("BossAggro", net.minecraft.nbt.CompoundTag.CODEC, this.aggro.entrySet().stream().collect(net.minecraft.nbt.CompoundTag::new, (tag, entry) -> tag.putInt(Integer.toString(entry.getKey()), entry.getValue()), net.minecraft.nbt.CompoundTag::merge));
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.spawnTimer = input.getIntOr("BossSpawnTimer", 0);
        this.entityData.set(DATA_SPAWN_TIMER, this.spawnTimer);
        this.setAnger(input.getIntOr("BossAnger", 0));
        input.getInt("HomeD").ifPresent(radius -> this.setHomeTo(new BlockPos(input.getIntOr("HomeX", 0), input.getIntOr("HomeY", 0), input.getIntOr("HomeZ", 0)), radius));
        input.read("BossAggro", net.minecraft.nbt.CompoundTag.CODEC).ifPresent(tag -> tag.keySet().forEach(key -> this.aggro.put(parseId(key), tag.getIntOr(key, 0))));
    }

    private static int parseId(String key) {
        try {
            return Integer.parseInt(key);
        } catch (NumberFormatException ignored) {
            return -1;
        }
    }
}
