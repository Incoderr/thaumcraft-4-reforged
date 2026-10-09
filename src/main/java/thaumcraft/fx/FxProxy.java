package thaumcraft.fx;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public interface FxProxy {
    default void beamPulse(Level level, Entity source, Entity target, int color) {
    }

    default void nodeBolt(Level level, float x, float y, float z, float targetX, float targetY, float targetZ) {
    }

    default void visDrain(Level level, BlockPos drainer, BlockPos source, int primal) {
    }
}
