package thaumcraft.world.gen;

import java.util.Random;
import net.minecraft.world.level.WorldGenLevel;

public final class NodeHooks {
    private NodeHooks() {
    }

    public static boolean templeNode(WorldGenLevel level, Random random, int chunkX, int chunkZ) {
        return false;
    }

    public static boolean wildNode(WorldGenLevel level, Random random, int chunkX, int chunkZ, boolean auraGen) {
        return false;
    }

    public static void moundNode(WorldGenLevel level, Random random, int x, int y, int z) {
    }

    public static void hilltopNode(WorldGenLevel level, Random random, int x, int y, int z) {
    }

    public static boolean eldritchRing(WorldGenLevel level, Random random, int chunkX, int chunkZ, int x, int y, int z) {
        return false;
    }

    public static void totem(WorldGenLevel level, Random random, int chunkX, int chunkZ, boolean auraGen) {
    }

    public static void netherTotem(WorldGenLevel level, Random random, int chunkX, int chunkZ) {
    }

    public static void netherWildNode(WorldGenLevel level, Random random, int chunkX, int chunkZ) {
    }
}
