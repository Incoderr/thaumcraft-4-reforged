package thaumcraft.client.aspect;

import net.minecraft.world.item.ItemStack;
import thaumcraft.aspect.Aspect;

public final class AspectVisibility {
    private AspectVisibility() {
    }

    public static boolean isScanned(ItemStack stack) {
        return true;
    }

    public static boolean isKnown(Aspect aspect) {
        return true;
    }
}
