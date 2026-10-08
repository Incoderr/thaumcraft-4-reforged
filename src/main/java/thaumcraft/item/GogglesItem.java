package thaumcraft.item;

import net.minecraft.world.item.Item;

public class GogglesItem extends Item {
    public GogglesItem(Properties properties) {
        super(properties);
    }

    public static Properties properties(Properties properties) {
        return properties.stacksTo(1);
    }
}
