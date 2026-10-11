package thaumcraft.research;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class WarpEncounterHooks {
    private static Spawner spawner;

    private WarpEncounterHooks() {
    }

    public static void install(Spawner encounterSpawner) {
        if (spawner != null) {
            throw new IllegalStateException("Warp encounter spawner is already installed");
        }
        spawner = encounterSpawner;
    }

    public static void spawnGuardians(ServerLevel level, ServerPlayer player, int count) {
        requireSpawner().spawnGuardians(level, player, count);
    }

    public static void spawnMindSpiders(ServerLevel level, ServerPlayer player, boolean harmless, int count) {
        requireSpawner().spawnMindSpiders(level, player, harmless, count);
    }

    private static Spawner requireSpawner() {
        if (spawner == null) {
            throw new IllegalStateException("Warp encounter spawner has not been installed");
        }
        return spawner;
    }

    public interface Spawner {
        void spawnGuardians(ServerLevel level, ServerPlayer player, int count);

        void spawnMindSpiders(ServerLevel level, ServerPlayer player, boolean harmless, int count);
    }
}
