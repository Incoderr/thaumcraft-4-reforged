package thaumcraft.item;

import net.minecraft.world.item.Item;

public class ThaumometerItem extends Item {
    public ThaumometerItem(Properties properties) {
        super(properties);
    }

    public static Properties properties(Properties properties) {
        return properties.stacksTo(1);
    }
}
