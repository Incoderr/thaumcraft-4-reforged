package thaumcraft.entity.monster.taint;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import java.util.List;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.IShearable;
import net.minecraft.world.level.Level;

public class TaintedSheep extends TaintedAnimalMonster implements IShearable {
    private static final EntityDataAccessor<Byte> DATA_FLAGS = SynchedEntityData.defineId(TaintedSheep.class, EntityDataSerializers.BYTE);
    private static final byte SHEARED_FLAG = 16;
    public TaintedSheep(EntityType<? extends TaintedSheep> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return TaintedAnimalMonster.createAttributes(20.0, 0.25, 3.0, 1.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_FLAGS, (byte) 0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new net.minecraft.world.entity.ai.goal.EatBlockGoal(this));
    }

    public boolean isSheared() {
        return (this.entityData.get(DATA_FLAGS) & SHEARED_FLAG) != 0;
    }

    public void setSheared(boolean sheared) {
        byte flags = this.entityData.get(DATA_FLAGS);
        this.entityData.set(DATA_FLAGS, (byte) (sheared ? flags | SHEARED_FLAG : flags & ~SHEARED_FLAG));
    }

    @Override
    public boolean isShearable(Player player, ItemStack item, Level level, BlockPos pos) {
        return !this.isSheared();
    }

    @Override
    public List<ItemStack> onSheared(Player player, ItemStack item, Level level, BlockPos pos) {
        this.setSheared(true);
        int count = 1 + this.random.nextInt(3);
        return List.of(new ItemStack(Blocks.WOOL.black().asItem(), count));
    }

    @Override
    protected void dropCustomDeathLoot(net.minecraft.server.level.ServerLevel level, net.minecraft.world.damagesource.DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        this.dropTaintResource(level, 3, 0.5F);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("Sheared", this.isSheared());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setSheared(input.getBooleanOr("Sheared", false));
    }
}
