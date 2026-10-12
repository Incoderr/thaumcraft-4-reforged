package thaumcraft.entity.monster.taint;

import java.util.Comparator;
import java.util.List;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

final class TaintedVillagerIndoorGoal extends Goal {
    private final TaintedVillager villager;
    @Nullable
    private BlockPos door;
    @Nullable
    private BlockPos lastDoor;

    TaintedVillagerIndoorGoal(TaintedVillager villager) {
        this.villager = villager;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!(this.villager.level() instanceof ServerLevel level) || !isNightRainOrNoPrecipitation(level, this.villager.blockPosition())) {
            return false;
        }
        if (this.villager.getRandom().nextInt(50) != 0) {
            return false;
        }
        BlockPos pos = this.villager.blockPosition();
        if (this.lastDoor != null && this.villager.distanceToSqr(this.lastDoor.getX(), pos.getY(), this.lastDoor.getZ()) < 4.0) {
            return false;
        }
        this.door = nearestVillageDoor(level, pos, 14);
        return this.door != null;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.villager.getNavigation().isDone();
    }

    @Override
    public void start() {
        if (this.door == null) {
            return;
        }
        if (this.villager.distanceToSqr(this.door.getX() + 0.5, this.door.getY(), this.door.getZ() + 0.5) > 256.0) {
            Vec3 target = DefaultRandomPos.getPosTowards(
                this.villager,
                14,
                3,
                Vec3.atBottomCenterOf(this.door),
                (float) (Math.PI / 2.0)
            );
            if (target != null) {
                this.villager.getNavigation().moveTo(target.x, target.y, target.z, 1.0);
            }
        } else {
            this.villager.getNavigation().moveTo(this.door.getX() + 0.5, this.door.getY(), this.door.getZ() + 0.5, 1.0);
        }
    }

    @Override
    public void stop() {
        this.lastDoor = this.door;
        this.door = null;
    }

    static boolean isNightRainOrNoPrecipitation(ServerLevel level, BlockPos pos) {
        return (!isDay(level) || level.isRaining() || !level.getBiome(pos).value().hasPrecipitation())
            && level.dimensionType().hasSkyLight();
    }

    static boolean isDay(ServerLevel level) {
        return level.isBrightOutside();
    }

    @Nullable
    static BlockPos nearestVillageDoor(ServerLevel level, BlockPos origin, int villageRadius) {
        List<BlockPos> villagePoints = level.getPoiManager()
            .findAll(type -> type.is(PoiTypeTags.VILLAGE), pos -> true, origin, villageRadius, PoiManager.Occupancy.ANY)
            .sorted(Comparator.comparingDouble(origin::distSqr))
            .limit(8)
            .toList();
        if (villagePoints.isEmpty()) {
            return null;
        }
        BlockPos villagePoint = villagePoints.getFirst();
        BlockPos nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        for (int x = -villageRadius; x <= villageRadius; x++) {
            for (int z = -villageRadius; z <= villageRadius; z++) {
                for (int y = -3; y <= 3; y++) {
                    BlockPos candidate = villagePoint.offset(x, y, z);
                    var state = level.getBlockState(candidate);
                    if (!(state.getBlock() instanceof DoorBlock) || state.getValue(DoorBlock.HALF) != DoubleBlockHalf.LOWER
                        || !state.is(BlockTags.WOODEN_DOORS)) {
                        continue;
                    }
                    double distance = origin.distSqr(candidate);
                    if (distance < nearestDistance) {
                        nearest = candidate.immutable();
                        nearestDistance = distance;
                    }
                }
            }
        }
        return nearest;
    }
}
