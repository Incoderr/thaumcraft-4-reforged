package thaumcraft.platform;

import net.neoforged.bus.api.IEventBus;
import thaumcraft.platform.baubles.BaublesSetup;

public final class Platform {
    private Platform() {
    }

    public static void register(IEventBus bus) {
        BaublesSetup.register(bus);
    }
}
