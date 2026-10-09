package thaumcraft.client.render;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import thaumcraft.Thaumcraft;
import thaumcraft.client.hud.WandHud;

@EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class ItemRenderers {
    private ItemRenderers() {
    }

    @SubscribeEvent
    static void registerSpecialRenderers(RegisterSpecialModelRendererEvent event) {
        event.register(Thaumcraft.id("wand"), WandSpecialRenderer.Unbaked.MAP_CODEC);
        event.register(Thaumcraft.id("aura_node"), NodeItemRenderer.Unbaked.MAP_CODEC);
        event.register(Thaumcraft.id("node_jar"), NodeJarItemRenderer.Unbaked.MAP_CODEC);
    }

    @SubscribeEvent
    static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, Thaumcraft.id("wand_hud"), WandHud::render);
    }
}
