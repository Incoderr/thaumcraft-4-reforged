package thaumcraft.node;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import thaumcraft.aspect.Aspect;

public final class NodeHooks {
    private NodeHooks() {
    }

    public static void spreadTaintFibres(ServerLevel level, BlockPos pos) {
    }

    public static boolean spawnAspectOrb(ServerLevel level, BlockPos pos, Aspect aspect, int amount) {
        return false;
    }

    public static boolean spawnGiantBrainyZombie(ServerLevel level, BlockPos node, BlockPos pos) {
        return false;
    }

    public static void spillFlux(ServerLevel level, BlockPos pos, boolean gas) {
    }
}
