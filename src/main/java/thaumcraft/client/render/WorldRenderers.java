package thaumcraft.client.render;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import thaumcraft.Thaumcraft;
import thaumcraft.client.platform.ModRenderPipelines;
import thaumcraft.registry.ModBlockEntities;

@EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class WorldRenderers {
    private WorldRenderers() {
    }

    @SubscribeEvent
    static void registerPipelines(RegisterRenderPipelinesEvent event) {
        event.registerPipeline(ModRenderPipelines.ADDITIVE_NO_DEPTH);
        event.registerPipeline(ModRenderPipelines.TRANSLUCENT_NO_DEPTH);
    }

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.NODE.get(), AuraNodeBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.ENERGIZED_NODE.get(), EnergizedNodeRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.NODE_JAR.get(), JarNodeRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.NODE_STABILIZER.get(), NodeStabilizerRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.NODE_TRANSDUCER.get(), NodeTransducerRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.VIS_RELAY.get(), VisRelayRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.WAND_PEDESTAL.get(), WandPedestalRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.RESEARCH_TABLE.get(), ResearchTableRenderer::new);
    }

    @SubscribeEvent
    static void registerSpecialRenderers(RegisterSpecialModelRendererEvent event) {
        event.register(Thaumcraft.id("node_stabilizer"), NodeDeviceSpecialRenderers.Stabilizer.Unbaked.MAP_CODEC);
        event.register(Thaumcraft.id("node_transducer"), NodeDeviceSpecialRenderers.Transducer.Unbaked.MAP_CODEC);
        event.register(Thaumcraft.id("vis_relay"), NodeDeviceSpecialRenderers.Relay.Unbaked.MAP_CODEC);
    }
}
