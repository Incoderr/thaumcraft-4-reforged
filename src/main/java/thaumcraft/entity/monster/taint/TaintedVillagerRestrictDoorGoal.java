package thaumcraft.entity.monster.taint;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jspecify.annotations.Nullable;

final class TaintedVillagerRestrictDoorGoal extends Goal {
    private final TaintedVillager villager;
    @Nullable
    private BlockPos door;

    TaintedVillagerRestrictDoorGoal(TaintedVillager villager) {
        this.villager = villager;
    }

    @Override
    public boolean canUse() {
        if (!(this.villager.level() instanceof ServerLevel level) || TaintedVillagerIndoorGoal.isDay(level)) {
            return false;
        }
        this.door = TaintedVillagerIndoorGoal.nearestVillageDoor(level, this.villager.blockPosition(), 16);
        return this.door != null && this.villager.distanceToSqr(this.door.getX(), this.door.getY(), this.door.getZ()) < 2.25;
    }

    @Override
    public boolean canContinueToUse() {
        return this.door != null
            && this.villager.level() instanceof ServerLevel level
            && !TaintedVillagerIndoorGoal.isDay(level)
            && this.villager.distanceToSqr(this.door.getX(), this.villager.getY(), this.door.getZ()) < 2.25;
    }

    @Override
    public void start() {
        this.villager.getNavigation().setCanOpenDoors(false);
    }

    @Override
    public void stop() {
        this.villager.getNavigation().setCanOpenDoors(true);
        this.door = null;
    }
}
