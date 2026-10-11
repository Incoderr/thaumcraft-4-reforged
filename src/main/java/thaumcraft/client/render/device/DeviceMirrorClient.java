package thaumcraft.client.render.device;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import thaumcraft.Thaumcraft;
import thaumcraft.client.screen.HandMirrorScreen;
import thaumcraft.registry.ModBlockEntities;
import thaumcraft.registry.ModMenus;

@EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class DeviceMirrorClient {
    private DeviceMirrorClient() {
    }

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.MAGIC_MIRROR.get(), MirrorRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.ESSENTIA_MIRROR.get(), MirrorRenderer::new);
    }

    @SubscribeEvent
    static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.HAND_MIRROR.get(), HandMirrorScreen::new);
    }

    @SubscribeEvent
    static void registerItemProperties(RegisterConditionalItemModelPropertyEvent event) {
        event.register(Thaumcraft.id("sinister_lodestone_active"), SinisterLodestoneActive.MAP_CODEC);
    }

    @SubscribeEvent
    static void clientTick(ClientTickEvent.Post event) {
        SinisterNodes.tick(Minecraft.getInstance());
    }
}
