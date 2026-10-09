package thaumcraft.network;

import thaumcraft.blockentity.ResearchTableBlockEntity;
import thaumcraft.platform.ModNetwork;
import thaumcraft.research.ResearchManager;
import thaumcraft.research.ScanManager;

public final class ModPayloads {
    private ModPayloads() {
    }

    public static void register() {
        ModNetwork.toClient(BeamPulsePayload.TYPE, BeamPulsePayload.STREAM_CODEC);
        ModNetwork.toClient(AspectTagsPayload.TYPE, AspectTagsPayload.STREAM_CODEC);
        ModNetwork.toClient(NodeZapPayload.TYPE, NodeZapPayload.STREAM_CODEC);
        ModNetwork.toClient(VisDrainFxPayload.TYPE, VisDrainFxPayload.STREAM_CODEC);
        ModNetwork.toClient(ResearchCompletePayload.TYPE, ResearchCompletePayload.STREAM_CODEC);
        ModNetwork.toClient(AspectPoolPayload.TYPE, AspectPoolPayload.STREAM_CODEC);
        ModNetwork.toClient(AspectDiscoveryPayload.TYPE, AspectDiscoveryPayload.STREAM_CODEC);
        ModNetwork.toClient(WarpMessagePayload.TYPE, WarpMessagePayload.STREAM_CODEC);
        ModNetwork.toServer(ScanPayload.TYPE, ScanPayload.STREAM_CODEC, ScanManager::handleScan);
        ModNetwork.toServer(PlayerCompletePayload.TYPE, PlayerCompletePayload.STREAM_CODEC, ResearchManager::handlePlayerComplete);
        ModNetwork.toServer(AspectPlacePayload.TYPE, AspectPlacePayload.STREAM_CODEC, ResearchTableBlockEntity::handlePlace);
        ModNetwork.toServer(AspectCombinePayload.TYPE, AspectCombinePayload.STREAM_CODEC, ResearchTableBlockEntity::handleCombine);
    }
}
