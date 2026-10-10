package thaumcraft.client.render.device;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterBlockStateModels;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import thaumcraft.Thaumcraft;
import thaumcraft.registry.ModBlockEntities;

@EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class DeviceRenderers {
    private DeviceRenderers() {
    }

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.HUNGRY_CHEST.get(), HungryChestRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.BRAIN_JAR.get(), BrainJarRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.BANNER.get(), BannerRenderer::new);
    }

    @SubscribeEvent
    static void registerSpecialRenderers(RegisterSpecialModelRendererEvent event) {
        event.register(Thaumcraft.id("hungry_chest"), DeviceSpecialRenderers.HungryChest.Unbaked.MAP_CODEC);
        event.register(Thaumcraft.id("brain_jar"), DeviceSpecialRenderers.BrainJar.Unbaked.MAP_CODEC);
        event.register(Thaumcraft.id("banner"), DeviceSpecialRenderers.Banner.Unbaked.MAP_CODEC);
    }

    @SubscribeEvent
    static void registerModels(RegisterBlockStateModels event) {
        event.registerModel(Thaumcraft.id("warded_glass"), WardedGlassModel.Unbaked.MAP_CODEC);
    }
}
