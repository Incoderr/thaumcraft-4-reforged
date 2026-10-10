package thaumcraft.item.bauble;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import thaumcraft.platform.Bauble;
import thaumcraft.platform.BaubleType;

public class HoverGirdleItem extends Item implements Bauble {
    public HoverGirdleItem(Properties properties) {
        super(properties);
    }

    @Override
    public BaubleType getBaubleType(ItemStack stack) {
        return BaubleType.BELT;
    }
}
