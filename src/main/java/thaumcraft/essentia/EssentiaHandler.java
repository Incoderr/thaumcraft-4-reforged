package thaumcraft.essentia;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import thaumcraft.aspect.Aspect;

public final class EssentiaHandler {
    private EssentiaHandler() {
    }

    public static boolean drainEssentia(BlockEntity tile, Aspect aspect, Direction direction, int range) {
        return drainEssentia(tile, aspect, direction, range, false);
    }

    public static boolean drainEssentia(BlockEntity tile, Aspect aspect, Direction direction, int range, boolean ignoreMirror) {
        return false;
    }

    public static boolean findEssentia(BlockEntity tile, Aspect aspect, Direction direction, int range) {
        return false;
    }

    public static void refreshSources(BlockEntity tile) {
    }
}
