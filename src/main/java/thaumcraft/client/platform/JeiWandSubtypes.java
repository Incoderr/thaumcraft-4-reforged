package thaumcraft.client.platform;

import mezz.jei.api.registration.ISubtypeRegistration;
import thaumcraft.registry.ModItems;
import thaumcraft.wand.Wands;

public final class JeiWandSubtypes {
    private JeiWandSubtypes() {
    }

    public static void register(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(ModItems.WAND.get(), (stack, context) -> Wands.getParts(stack));
    }
}
