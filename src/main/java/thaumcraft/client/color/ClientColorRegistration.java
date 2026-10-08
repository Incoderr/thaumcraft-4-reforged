package thaumcraft.client.color;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import thaumcraft.Thaumcraft;

@EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class ClientColorRegistration {
    private ClientColorRegistration() {
    }

    @SubscribeEvent
    static void registerItemTints(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(Thaumcraft.id("aspect"), AspectTint.MAP_CODEC);
    }
}
