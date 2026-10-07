package thaumcraft.network;

import thaumcraft.platform.ModNetwork;

public final class ModPayloads {
    private ModPayloads() {
    }

    public static void register() {
        ModNetwork.toClient(BeamPulsePayload.TYPE, BeamPulsePayload.STREAM_CODEC);
    }
}
