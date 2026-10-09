package thaumcraft.research;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public final class Warp {
    private Warp() {
    }

    public static int get(Player player, WarpType type) {
        return Knowledge.get(player).getWarp(type);
    }

    public static int total(Player player) {
        return Knowledge.get(player).getTotalWarp();
    }

    public static void add(ServerPlayer player, int amount, WarpType type) {
    }

    public static void set(ServerPlayer player, int amount, WarpType type) {
    }
}
