package thaumcraft.client.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import thaumcraft.Thaumcraft;
import thaumcraft.client.platform.GuiBlit;
import thaumcraft.menu.FocusPouchMenu;

public class FocusPouchScreen extends AbstractContainerScreen<FocusPouchMenu> {
    private static final Identifier TEXTURE = Thaumcraft.id("textures/gui/gui_focuspouch.png");
    private final int blockSlot;

    public FocusPouchScreen(FocusPouchMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 175, 232);
        this.blockSlot = inventory.getSelectedSlot();
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (minecraft.player != null && minecraft.player.getInventory().getItem(blockSlot).isEmpty()) {
            onClose();
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        GuiBlit.blit(graphics, TEXTURE, leftPos, topPos, imageWidth, imageHeight, 0, 0, imageWidth, imageHeight, 256, 256, 0xFFFFFFFF);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        GuiBlit.blit(graphics, TEXTURE, leftPos + 8 + blockSlot * 18, topPos + 209, 16, 16, 240, 0, 16, 16, 256, 256, 0xFFFFFFFF);
    }
}
