package thaumcraft.client.render.device;

import java.util.List;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.util.ARGB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import thaumcraft.Thaumcraft;
import thaumcraft.registry.ModBlockEntities;
import thaumcraft.registry.ModBlocks;

@EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class DeviceRenderers {
    private DeviceRenderers() {
    }

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.ARCANE_LAMP.get(), LampNozzleRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.LAMP_OF_GROWTH.get(), LampNozzleRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.LAMP_OF_FERTILITY.get(), LampNozzleRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.CRYSTAL_CLUSTER.get(), CrystalClusterRenderer::new);
    }

    @SubscribeEvent
    static void registerSpecialRenderers(RegisterSpecialModelRendererEvent event) {
        event.register(Thaumcraft.id("crystal_cluster"), DeviceSpecialRenderers.CrystalCluster.Unbaked.MAP_CODEC);
    }

    @SubscribeEvent
    static void registerBlockTints(RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(List.of(constant(0x00A000), constant(0xDD11FF)), ModBlocks.ARCANE_LEVITATOR.get());
    }

    private static BlockTintSource constant(int color) {
        return BlockTintSources.constant(ARGB.opaque(color));
    }
}
