package thaumcraft.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import thaumcraft.registry.ModMenus;

public class FocalManipulatorMenu extends AbstractContainerMenu {
    private final BlockPos pos;

    public FocalManipulatorMenu(int containerId, Inventory inventory, BlockPos pos) {
        super(ModMenus.FOCAL_MANIPULATOR.get(), containerId);
        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void startCraft(net.minecraft.server.level.ServerPlayer player, int upgrade) {
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
