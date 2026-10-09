package thaumcraft.client.research;

import net.minecraft.world.entity.player.Player;
import thaumcraft.network.AspectDiscoveryPayload;
import thaumcraft.network.AspectPoolPayload;
import thaumcraft.network.ResearchCompletePayload;
import thaumcraft.network.WarpMessagePayload;

public final class ResearchNotifications {
    private ResearchNotifications() {
    }

    public static void researchComplete(ResearchCompletePayload payload, Player player) {
    }

    public static void aspectPool(AspectPoolPayload payload, Player player) {
    }

    public static void aspectDiscovery(AspectDiscoveryPayload payload, Player player) {
    }

    public static void warpMessage(WarpMessagePayload payload, Player player) {
    }
}
