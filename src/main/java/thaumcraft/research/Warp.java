package thaumcraft.research;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import thaumcraft.network.WarpMessagePayload;
import thaumcraft.platform.ModNetwork;

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
        if (amount == 0) {
            return;
        }
        PlayerKnowledge knowledge = Knowledge.get(player);
        if (amount < 0 && (type == WarpType.PERMANENT || knowledge.getWarp(type) <= 0)) {
            return;
        }
        knowledge.setWarp(type, knowledge.getWarp(type) + amount);
        knowledge.setWarpCounter(knowledge.getTotalWarp());
        Knowledge.sync(player);
        ModNetwork.sendToPlayer(player, new WarpMessagePayload(type.ordinal(), amount));
    }

    public static void set(ServerPlayer player, int amount, WarpType type) {
        Knowledge.get(player).setWarp(type, amount);
        Knowledge.sync(player);
    }
}
