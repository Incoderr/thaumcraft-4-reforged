package thaumcraft.entity.monster.taint;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.item.ItemStack;
import thaumcraft.effect.EffectHooks;
import thaumcraft.entity.TaintedMob;
import thaumcraft.registry.ModBlocks;
import thaumcraft.registry.ModItems;
import thaumcraft.taint.TaintBiomes;

public class TaintedCreeper extends Creeper implements TaintedMob {
    private boolean taintBurstHandled;

    public TaintedCreeper(EntityType<? extends TaintedCreeper> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return net.minecraft.world.entity.monster.Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 30.0)
            .add(Attributes.MOVEMENT_SPEED, 0.25)
            .add(Attributes.FOLLOW_RANGE, 16.0)
            .add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide() && this.isAlive() && !this.taintBurstHandled && this.getSwellDir() > 0 && this.getSwelling(1.0F) >= 1.0F) {
            this.taintBurstHandled = true;
            this.infectExplosionArea((ServerLevel) this.level());
        }
        super.tick();
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        this.spawnAtLocation(new ItemStack(level.getRandom().nextBoolean() ? ModItems.TAINTED_GOO.get() : ModItems.TAINT_TENDRIL.get()));
    }

    private void infectExplosionArea(ServerLevel level) {
        AABB area = new AABB(this.blockPosition()).inflate(6.0);
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, area)) {
            if (target != this && !(target instanceof TaintedMob) && !target.getType().builtInRegistryHolder().is(net.minecraft.tags.EntityTypeTags.UNDEAD)) {
                EffectHooks.applyTaintPoison(target, 100, 0);
            }
        }
        int x = (int) this.getX();
        int y = (int) this.getY();
        int z = (int) this.getZ();
        for (int index = 0; index < 10; index++) {
            int targetX = x + (int) ((level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 5.0F);
            int targetZ = z + (int) ((level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 5.0F);
            BlockPos target = new BlockPos(targetX, y, targetZ);
            if (!level.getRandom().nextBoolean() || TaintBiomes.isTainted(level, target)) {
                continue;
            }
            TaintBiomes.set(level, target, TaintBiomes.TAINTED_LAND);
            BlockPos below = target.below();
            if (level.getBlockState(below).isFaceSturdy(level, below, Direction.UP) && level.getBlockState(target).canBeReplaced()) {
                level.setBlockAndUpdate(target, ModBlocks.FIBROUS_TAINT.get().defaultBlockState());
            }
        }
    }
}
