package thaumcraft.client;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import thaumcraft.aspect.AspectRegistry;
import thaumcraft.client.platform.ClientNetwork;
import thaumcraft.fx.Fx;
import thaumcraft.network.AspectTagsPayload;
import thaumcraft.network.BeamPulsePayload;
import thaumcraft.network.NodeZapPayload;
import thaumcraft.network.VisDrainFxPayload;

public final class ClientPayloadHandlers {
    private ClientPayloadHandlers() {
    }

    public static void register() {
        ClientNetwork.handle(BeamPulsePayload.TYPE, ClientPayloadHandlers::beamPulse);
        ClientNetwork.handle(AspectTagsPayload.TYPE, ClientPayloadHandlers::aspectTags);
        ClientNetwork.handle(NodeZapPayload.TYPE, ClientPayloadHandlers::nodeZap);
        ClientNetwork.handle(VisDrainFxPayload.TYPE, ClientPayloadHandlers::visDrain);
    }

    private static void beamPulse(BeamPulsePayload payload, Player player) {
        Entity source = player.level().getEntity(payload.source());
        Entity target = player.level().getEntity(payload.target());
        if (source != null && target != null) {
            Fx.get().beamPulse(player.level(), source, target, payload.color());
        }
    }

    private static void aspectTags(AspectTagsPayload payload, Player player) {
        AspectRegistry.setActive(AspectRegistry.fromSync(payload.items(), payload.entities()));
    }

    private static void nodeZap(NodeZapPayload payload, Player player) {
    }

    private static void visDrain(VisDrainFxPayload payload, Player player) {
    }
}
