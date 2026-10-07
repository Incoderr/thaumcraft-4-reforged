package thaumcraft.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import thaumcraft.Thaumcraft;
import thaumcraft.client.fx.ClientFx;
import thaumcraft.client.platform.ModRenderPipelines;
import thaumcraft.fx.Fx;

@Mod(value = Thaumcraft.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class ThaumcraftClient {
    public ThaumcraftClient(IEventBus modEventBus, ModContainer container) {
        ClientPayloadHandlers.register();
        Fx.set(new ClientFx());
    }

    @SubscribeEvent
    static void registerPipelines(RegisterRenderPipelinesEvent event) {
        event.registerPipeline(ModRenderPipelines.ADDITIVE_PARTICLE);
        event.registerPipeline(ModRenderPipelines.TRANSLUCENT_PARTICLE);
    }
}
