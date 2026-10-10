package thaumcraft.item.tool;

import net.minecraft.world.item.Item;

public class PrimalArrowItem extends Item {
    private final int type;

    public PrimalArrowItem(Properties properties, int type) {
        super(properties);
        this.type = type;
    }

    public int getArrowType() {
        return type;
    }
}
