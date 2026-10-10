package thaumcraft.item.bauble;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import thaumcraft.platform.Bauble;
import thaumcraft.platform.BaubleType;

public class VisAmuletItem extends Item implements Bauble {
    private final boolean stone;

    public VisAmuletItem(Properties properties, boolean stone) {
        super(properties);
        this.stone = stone;
    }

    public boolean isStone() {
        return stone;
    }

    @Override
    public BaubleType getBaubleType(ItemStack stack) {
        return BaubleType.AMULET;
    }
}
