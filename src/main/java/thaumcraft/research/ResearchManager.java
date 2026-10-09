package thaumcraft.research;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;
import thaumcraft.aspect.Aspect;
import thaumcraft.network.PlayerCompletePayload;

public final class ResearchManager {
    private ResearchManager() {
    }

    public static boolean isComplete(Player player, String key) {
        return Knowledge.get(player).isResearchComplete(key);
    }

    public static boolean hasRequisites(Player player, String key) {
        ResearchItem item = ResearchCategories.getResearch(key);
        if (item == null) {
            return false;
        }
        for (String parent : item.parents()) {
            if (!isComplete(player, parent)) {
                return false;
            }
        }
        for (String parent : item.parentsHidden()) {
            if (!isComplete(player, parent)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isBuyable(ResearchItem item) {
        return ResearchConfig.RESEARCH_DIFFICULTY == -1 || ResearchConfig.RESEARCH_DIFFICULTY == 0 && item.isSecondary();
    }

    public static boolean complete(ServerPlayer player, String key) {
        return false;
    }

    public static void completeWithSiblings(ServerPlayer player, String key) {
    }

    public static boolean addAspectPool(ServerPlayer player, Aspect aspect, int amount) {
        return false;
    }

    public static boolean spendAspectPool(ServerPlayer player, Aspect aspect, int amount) {
        return false;
    }

    public static boolean checkAndSyncAspectKnowledge(ServerPlayer player, Aspect aspect, int amount) {
        return false;
    }

    public static @Nullable String findHiddenResearch(ServerPlayer player) {
        return null;
    }

    public static void handlePlayerComplete(PlayerCompletePayload payload, Player player) {
    }
}
