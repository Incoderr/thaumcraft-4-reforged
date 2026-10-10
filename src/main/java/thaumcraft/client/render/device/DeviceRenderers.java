package thaumcraft.client.render.device;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import thaumcraft.Thaumcraft;
import thaumcraft.registry.ModBlockEntities;

@EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class DeviceRenderers {
    private DeviceRenderers() {
    }

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.ARCANE_BORE.get(), ArcaneBoreRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.ARCANE_BORE_BASE.get(), ArcaneBoreBaseRenderer::new);
    }

    @SubscribeEvent
    static void registerSpecialRenderers(RegisterSpecialModelRendererEvent event) {
        event.register(Thaumcraft.id("arcane_bore_base"), DeviceSpecialRenderers.BoreBase.Unbaked.MAP_CODEC);
        event.register(Thaumcraft.id("arcane_bore"), DeviceSpecialRenderers.Bore.Unbaked.MAP_CODEC);
    }
}
