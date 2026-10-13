package thaumcraft.entity.boss;

import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.entity.monster.Cultist;
import thaumcraft.entity.projectile.GolemOrb;
import thaumcraft.registry.ModItems;
import thaumcraft.registry.ModSounds;

public class CultistLeader extends ThaumcraftBoss implements RangedAttackMob {
    private static final String[] TITLES = {"Alberic", "Anselm", "Bastian", "Beturian", "Chabier", "Chorache", "Chuse", "Dodorol", "Ebardo", "Ferrando", "Fertus", "Guillen", "Larpe", "Obano", "Zelipe"};
    private int title;

    public CultistLeader(EntityType<? extends CultistLeader> type, Level level) {
        super(type, level, BossEvent.BossBarColor.RED);
        this.xpReward = 40;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBossAttributes().add(Attributes.MOVEMENT_SPEED, 0.32).add(Attributes.MAX_HEALTH, 125.0).add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0, 30, 40, 24.0F));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.1, false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers(Cultist.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (this.level() instanceof net.minecraft.server.level.ServerLevel level && this.hasLineOfSight(target)) {
            level.addFreshEntity(GolemOrb.create(level, this, target, true));
            this.playSound(ModSounds.ELDRITCH_GUARDIAN_ATTACK.get(), 1.0F, 1.0F + this.random.nextFloat() * 0.1F);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof net.minecraft.server.level.ServerLevel level && this.tickCount % 20 == 0) {
            for (Cultist cultist : level.getEntitiesOfClass(Cultist.class, this.getBoundingBox().inflate(8.0))) {
                if (!cultist.hasEffect(net.minecraft.world.effect.MobEffects.REGENERATION)) {
                    cultist.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.REGENERATION, 60, 1), this);
                }
            }
        }
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return !(target instanceof Cultist) && super.canAttack(target);
    }

    @Override
    public boolean considersEntityAsAlly(net.minecraft.world.entity.Entity entity) {
        return entity instanceof Cultist || super.considersEntityAsAlly(entity);
    }

    @Override
    public net.minecraft.world.entity.SpawnGroupData finalizeSpawn(net.minecraft.world.level.ServerLevelAccessor level, net.minecraft.world.DifficultyInstance difficulty, net.minecraft.world.entity.EntitySpawnReason reason, net.minecraft.world.entity.@org.jspecify.annotations.Nullable SpawnGroupData data) {
        var result = super.finalizeSpawn(level, difficulty, reason, data);
        this.title = this.random.nextInt(TITLES.length);
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ModItems.CRIMSON_PRAETOR_HELM.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ModItems.CRIMSON_PRAETOR_CHESTPLATE.get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ModItems.CRIMSON_PRAETOR_GREAVES.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(ModItems.CRIMSON_CULT_BOOTS.get()));
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(difficulty.getDifficulty() == Difficulty.EASY ? ModItems.VOID_SWORD.get() : ModItems.CRIMSON_BLADE.get()));
        return result;
    }

    @Override
    protected void dropCustomDeathLoot(net.minecraft.server.level.ServerLevel level, net.minecraft.world.damagesource.DamageSource source, boolean recentlyHit) {
        this.spawnAtLocation(level, new ItemStack(ModItems.RARE_LOOT_BAG.get()));
    }

    @Override
    protected void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("Title", this.title);
    }

    @Override
    protected void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput input) {
        super.readAdditionalSaveData(input);
        this.title = Math.floorMod(input.getIntOr("Title", 0), TITLES.length);
    }

    public String getTitle() {
        return TITLES[Math.floorMod(this.title, TITLES.length)];
    }
}
