package thaumcraft.item;

import net.minecraft.world.item.Item;

public class ScribingToolsItem extends Item {
    public ScribingToolsItem(Properties properties) {
        super(properties);
    }

    public static Properties properties(Properties properties) {
        return properties.durability(100);
    }
}
