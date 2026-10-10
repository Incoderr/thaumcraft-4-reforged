package thaumcraft.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import thaumcraft.registry.ModMenus;

public class HoverHarnessMenu extends AbstractContainerMenu {
    public HoverHarnessMenu(int containerId, Inventory inventory) {
        super(ModMenus.HOVER_HARNESS.get(), containerId);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
