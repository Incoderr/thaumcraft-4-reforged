package thaumcraft.item;

import net.minecraft.world.item.Item;

public class ResearchNotesItem extends Item {
    private final boolean discovery;

    public ResearchNotesItem(Properties properties, boolean discovery) {
        super(properties);
        this.discovery = discovery;
    }

    public boolean isDiscovery() {
        return discovery;
    }
}
