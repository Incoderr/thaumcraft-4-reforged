package thaumcraft.entity.monster;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.server.level.ServerLevel;
import org.jspecify.annotations.Nullable;
import thaumcraft.entity.monster.eldritch.EldritchCrab;
import thaumcraft.registry.ModItems;

public class InhabitedZombie extends Zombie {
    public InhabitedZombie(EntityType<? extends InhabitedZombie> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
            .add(Attributes.MAX_HEALTH, 30.0)
            .add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(
        ServerLevelAccessor level,
        DifficultyInstance difficulty,
        net.minecraft.world.entity.EntitySpawnReason spawnReason,
        @Nullable SpawnGroupData spawnGroupData
    ) {
        float chance = level.getDifficulty() == Difficulty.HARD ? 0.9F : 0.6F;
        this.setItemSlot(EquipmentSlot.HEAD, new net.minecraft.world.item.ItemStack(ModItems.CRIMSON_CULT_HELM.get()));
        if (this.random.nextFloat() <= chance) {
            this.setItemSlot(EquipmentSlot.CHEST, new net.minecraft.world.item.ItemStack(ModItems.CRIMSON_CULT_CHESTPLATE.get()));
        }
        if (this.random.nextFloat() <= chance) {
            this.setItemSlot(EquipmentSlot.LEGS, new net.minecraft.world.item.ItemStack(ModItems.CRIMSON_CULT_GREAVES.get()));
        }
        return spawnGroupData;
    }

    @Override
    public void die(DamageSource damageSource) {
        if (this.level() instanceof ServerLevel serverLevel && this.isAlive()) {
            EldritchCrab.spawnFromInhabitedZombie(serverLevel, this);
        }
        super.die(damageSource);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
    }
}
