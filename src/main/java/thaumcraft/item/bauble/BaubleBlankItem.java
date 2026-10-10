package thaumcraft.item.bauble;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import thaumcraft.platform.Bauble;
import thaumcraft.platform.BaubleType;

public class BaubleBlankItem extends Item implements Bauble {
    private final int kind;

    public BaubleBlankItem(Properties properties, int kind) {
        super(properties);
        this.kind = kind;
    }

    public int getKind() {
        return kind;
    }

    @Override
    public BaubleType getBaubleType(ItemStack stack) {
        return kind == 0 ? BaubleType.AMULET : kind == 2 ? BaubleType.BELT : BaubleType.RING;
    }
}
