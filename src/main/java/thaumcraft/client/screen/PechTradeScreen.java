package thaumcraft.client.screen;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import thaumcraft.Thaumcraft;
import thaumcraft.client.platform.GuiBlit;
import thaumcraft.menu.PechTradeMenu;
import thaumcraft.registry.ModSounds;

public class PechTradeScreen extends AbstractContainerScreen<PechTradeMenu> {
    private static final Identifier TEXTURE = Thaumcraft.id("textures/gui/gui_pech.png");
    private static final int BUTTON_X = 67;
    private static final int BUTTON_Y = 24;

    public PechTradeScreen(PechTradeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 175, 232);
    }

    private boolean overTradeButton(double mouseX, double mouseY) {
        double x = mouseX - (leftPos + BUTTON_X);
        double y = mouseY - (topPos + BUTTON_Y);
        return x >= 0 && y >= 0 && x < 25 && y < 25;
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        GuiBlit.blit(graphics, TEXTURE, leftPos, topPos, imageWidth, imageHeight, 0, 0, imageWidth, imageHeight, 256, 256, 0xFFFFFFFF);
        if (menu.canTrade()) {
            GuiBlit.blit(graphics, TEXTURE, leftPos + BUTTON_X, topPos + BUTTON_Y, 25, 25, 176, 0, 25, 25, 256, 256, 0xFFFFFFFF);
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        if (menu.canTrade() && overTradeButton(mouseX, mouseY)) {
            graphics.setTooltipForNextFrame(font, Component.translatable("gui.thaumcraft.pech.trade"), mouseX, mouseY - 8, null);
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() == InputConstants.MOUSE_BUTTON_LEFT && menu.canTrade() && overTradeButton(event.x(), event.y())) {
            minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 0);
            minecraft.getSoundManager().play(SimpleSoundInstance.forUI(ModSounds.PECH_TRADE.get(), 0.95F + minecraft.level.getRandom().nextFloat() * 0.1F, 0.5F));
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }
}
