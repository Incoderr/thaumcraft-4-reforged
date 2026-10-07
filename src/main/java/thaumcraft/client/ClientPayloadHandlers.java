package thaumcraft.client;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import thaumcraft.client.platform.ClientNetwork;
import thaumcraft.fx.Fx;
import thaumcraft.network.BeamPulsePayload;

public final class ClientPayloadHandlers {
    private ClientPayloadHandlers() {
    }

    public static void register() {
        ClientNetwork.handle(BeamPulsePayload.TYPE, ClientPayloadHandlers::beamPulse);
    }

    private static void beamPulse(BeamPulsePayload payload, Player player) {
        Entity source = player.level().getEntity(payload.source());
        Entity target = player.level().getEntity(payload.target());
        if (source != null && target != null) {
            Fx.get().beamPulse(player.level(), source, target, payload.color());
        }
    }
}
