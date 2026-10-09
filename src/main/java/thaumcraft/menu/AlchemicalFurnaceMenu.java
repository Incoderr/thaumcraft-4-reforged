package thaumcraft.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import thaumcraft.registry.ModMenus;

public class AlchemicalFurnaceMenu extends AbstractContainerMenu {
    private final BlockPos pos;

    public AlchemicalFurnaceMenu(int containerId, Inventory inventory, BlockPos pos) {
        super(ModMenus.ALCHEMICAL_FURNACE.get(), containerId);
        this.pos = pos;
    }

    public BlockPos pos() {
        return pos;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
    }
}
