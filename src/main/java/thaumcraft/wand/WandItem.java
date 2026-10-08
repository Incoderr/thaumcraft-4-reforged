package thaumcraft.wand;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class WandItem extends Item {
    public WandItem(Properties properties) {
        super(properties);
    }

    public static Properties properties(Properties properties) {
        return WandKeys.PARTS.apply(properties.stacksTo(1).rarity(Rarity.UNCOMMON), WandParts.DEFAULT);
    }
}
