package thaumcraft.client.render.entity.golem;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import thaumcraft.Thaumcraft;
import thaumcraft.client.golem.GolemAdvancedProperty;
import thaumcraft.client.golem.GolemHasCoreProperty;
import thaumcraft.client.screen.GolemScreen;
import thaumcraft.client.screen.TravelingTrunkScreen;
import thaumcraft.registry.ModEntities;
import thaumcraft.registry.ModMenus;

@EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class GolemClientEvents {
    private GolemClientEvents() {
    }

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.GOLEM.get(), GolemRenderer::new);
        event.registerEntityRenderer(ModEntities.TRAVELING_TRUNK.get(), TravelingTrunkRenderer::new);
        event.registerEntityRenderer(ModEntities.GOLEM_BOBBER.get(), GolemBobberRenderer::new);
    }

    @SubscribeEvent
    static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GolemModelLayers.GOLEM, GolemModelLayers::golem);
        event.registerLayerDefinition(GolemModelLayers.ACCESSORIES, GolemModelLayers::accessories);
        event.registerLayerDefinition(GolemModelLayers.MARKER, GolemModelLayers::marker);
        event.registerLayerDefinition(GolemModelLayers.TRUNK, GolemModelLayers::trunk);
    }

    @SubscribeEvent
    static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.GOLEM.get(), GolemScreen::new);
        event.register(ModMenus.TRAVELING_TRUNK.get(), TravelingTrunkScreen::new);
    }

    @SubscribeEvent
    static void registerItemProperties(RegisterConditionalItemModelPropertyEvent event) {
        event.register(Thaumcraft.id("golem_advanced"), GolemAdvancedProperty.MAP_CODEC);
        event.register(Thaumcraft.id("golem_has_core"), GolemHasCoreProperty.MAP_CODEC);
    }
}
