package thaumcraft.platform.baubles;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import thaumcraft.platform.Bauble;
import thaumcraft.platform.BaubleType;

public class BaubleSlot extends Slot {
    private final BaubleInventory baubles;
    private final BaubleType type;

    public BaubleSlot(BaubleInventory baubles, BaubleType type, int index, int x, int y) {
        super(baubles, index, x, y);
        this.baubles = baubles;
        this.type = type;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return BaubleInventory.isValidFor(type, stack, baubles.player());
    }

    @Override
    public boolean mayPickup(Player player) {
        ItemStack stack = getItem();
        return !stack.isEmpty() && stack.getItem() instanceof Bauble bauble && bauble.canUnequip(stack, player);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
