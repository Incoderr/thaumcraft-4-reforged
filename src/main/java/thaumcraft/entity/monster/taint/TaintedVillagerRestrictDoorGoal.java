package thaumcraft.entity.monster.taint;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jspecify.annotations.Nullable;

final class TaintedVillagerRestrictDoorGoal extends Goal {
    private final TaintedVillager villager;
    private TaintedVillagerIndoorGoal.@Nullable VillageDoorInfo doorInfo;

    TaintedVillagerRestrictDoorGoal(TaintedVillager villager) {
        this.villager = villager;
    }

    @Override
    public boolean canUse() {
        if (!(this.villager.level() instanceof ServerLevel level) || TaintedVillagerIndoorGoal.isDay(level)) {
            return false;
        }
        this.doorInfo = TaintedVillagerIndoorGoal.nearestVillageDoor(level, this.villager.blockPosition(), 16);
        return this.doorInfo != null && this.villager.distanceToSqr(net.minecraft.world.phys.Vec3.atCenterOf(this.doorInfo.doorPos())) < 2.25;
    }

    @Override
    public boolean canContinueToUse() {
        return this.doorInfo != null
            && this.villager.level() instanceof ServerLevel level
            && !TaintedVillagerIndoorGoal.isDay(level)
            && this.doorInfo.isInside(this.villager.blockPosition());
    }

    @Override
    public void start() {
        this.villager.getNavigation().setCanOpenDoors(false);
    }

    @Override
    public void stop() {
        this.villager.getNavigation().setCanOpenDoors(true);
        this.doorInfo = null;
    }
}
