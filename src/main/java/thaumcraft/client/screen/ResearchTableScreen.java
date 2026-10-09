package thaumcraft.client.screen;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import thaumcraft.menu.ResearchTableMenu;

public class ResearchTableScreen extends AbstractContainerScreen<ResearchTableMenu> {
    public ResearchTableScreen(ResearchTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 255, 255);
    }
}
