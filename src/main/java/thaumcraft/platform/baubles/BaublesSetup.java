package thaumcraft.platform.baubles;

import net.neoforged.bus.api.IEventBus;

public final class BaublesSetup {
    private BaublesSetup() {
    }

    public static void register(IEventBus bus) {
        BaubleAttachments.register(bus);
        BaubleMenus.register(bus);
        OpenBaublesPayload.register();
    }
}
