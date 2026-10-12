package thaumcraft.entity.monster;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import thaumcraft.registry.ModItems;

public abstract class Cultist extends Monster {
    protected Cultist(net.minecraft.world.entity.EntityType<? extends Cultist> type, Level level) {
        super(type, level);
        this.setCanPickUpLoot(false);
    }

    public static AttributeSupplier.Builder createCultistAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 30.0)
            .add(Attributes.FOLLOW_RANGE, 32.0)
            .add(Attributes.MOVEMENT_SPEED, 0.3)
            .add(Attributes.ATTACK_DAMAGE, 4.0);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        int roll = this.random.nextInt(10);
        if (roll == 0) {
            this.spawnAtLocation(level, new ItemStack(ModItems.KNOWLEDGE_FRAGMENT.get()));
        } else if (roll == 1) {
            this.spawnAtLocation(level, new ItemStack(ModItems.VOID_SEED.get()));
        } else if (roll <= 3 + this.random.nextInt(3)) {
            this.spawnAtLocation(level, new ItemStack(ModItems.GOLD_COIN.get()));
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        if (this.hasHome() && this.getHomeRadius() > 0) {
            output.putInt("HomeRadius", this.getHomeRadius());
            output.putInt("HomeX", this.getHomePosition().getX());
            output.putInt("HomeY", this.getHomePosition().getY());
            output.putInt("HomeZ", this.getHomePosition().getZ());
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.getInt("HomeRadius").ifPresent(radius -> this.setHomeTo(
            new net.minecraft.core.BlockPos(input.getIntOr("HomeX", 0), input.getIntOr("HomeY", 0), input.getIntOr("HomeZ", 0)),
            radius
        ));
    }
}
