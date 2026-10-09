package thaumcraft.client;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import thaumcraft.aspect.AspectRegistry;
import thaumcraft.client.platform.ClientNetwork;
import thaumcraft.client.research.ResearchNotifications;
import thaumcraft.fx.Fx;
import thaumcraft.infusion.InfusionMatrixBlockEntity;
import thaumcraft.network.AspectDiscoveryPayload;
import thaumcraft.network.AspectPoolPayload;
import thaumcraft.network.AspectTagsPayload;
import thaumcraft.network.BeamPulsePayload;
import thaumcraft.network.BlockSparkleFxPayload;
import thaumcraft.network.EssentiaSourceFxPayload;
import thaumcraft.network.InfusionSourceFxPayload;
import thaumcraft.network.NodeZapPayload;
import thaumcraft.network.ResearchCompletePayload;
import thaumcraft.network.VisDrainFxPayload;
import thaumcraft.network.WarpMessagePayload;
import thaumcraft.registry.ModSounds;

public final class ClientPayloadHandlers {
    private ClientPayloadHandlers() {
    }

    public static void register() {
        ClientNetwork.handle(BeamPulsePayload.TYPE, ClientPayloadHandlers::beamPulse);
        ClientNetwork.handle(AspectTagsPayload.TYPE, ClientPayloadHandlers::aspectTags);
        ClientNetwork.handle(NodeZapPayload.TYPE, ClientPayloadHandlers::nodeZap);
        ClientNetwork.handle(VisDrainFxPayload.TYPE, ClientPayloadHandlers::visDrain);
        ClientNetwork.handle(ResearchCompletePayload.TYPE, ResearchNotifications::researchComplete);
        ClientNetwork.handle(AspectPoolPayload.TYPE, ResearchNotifications::aspectPool);
        ClientNetwork.handle(AspectDiscoveryPayload.TYPE, ResearchNotifications::aspectDiscovery);
        ClientNetwork.handle(WarpMessagePayload.TYPE, ResearchNotifications::warpMessage);
        ClientNetwork.handle(EssentiaSourceFxPayload.TYPE, ClientPayloadHandlers::essentiaSource);
        ClientNetwork.handle(InfusionSourceFxPayload.TYPE, ClientPayloadHandlers::infusionSource);
        ClientNetwork.handle(BlockSparkleFxPayload.TYPE, ClientPayloadHandlers::blockSparkle);
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
        Level level = player.level();
        Fx.get().nodeBolt(level, payload.x(), payload.y(), payload.z(), payload.targetX(), payload.targetY(), payload.targetZ());
        level.playLocalSound(payload.x(), payload.y(), payload.z(), ModSounds.ZAP.get(), SoundSource.BLOCKS, 0.1F, 1.0F + level.getRandom().nextFloat() * 0.2F, false);
    }

    private static void visDrain(VisDrainFxPayload payload, Player player) {
        Fx.get().visDrain(player.level(), payload.drainer(), payload.source(), payload.primal());
    }

    private static void essentiaSource(EssentiaSourceFxPayload payload, Player player) {
        Fx.get().essentiaSource(player.level(), payload.pos(), payload.dx(), payload.dy(), payload.dz(), payload.color());
    }

    private static void infusionSource(InfusionSourceFxPayload payload, Player player) {
        if (player.level().getBlockEntity(payload.pos()) instanceof InfusionMatrixBlockEntity matrix) {
            matrix.addSourceFx(payload.dx(), payload.dy(), payload.dz(), payload.color());
        }
    }

    private static void blockSparkle(BlockSparkleFxPayload payload, Player player) {
        Fx.get().blockSparkle(player.level(), payload.pos(), payload.color(), 7);
    }
}
