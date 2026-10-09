package thaumcraft.essentia;

import thaumcraft.aspect.AspectProvidingItem;
import thaumcraft.item.AspectContainerItem;

public class PhialItem extends AspectContainerItem implements AspectProvidingItem {
    private final boolean filled;

    public PhialItem(boolean filled, Properties properties) {
        super(properties, " x ", true);
        this.filled = filled;
    }

    public boolean isFilled() {
        return filled;
    }
}
