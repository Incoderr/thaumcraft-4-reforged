package thaumcraft.client.screen;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;
import thaumcraft.Thaumcraft;
import thaumcraft.client.platform.GuiBlit;
import thaumcraft.client.platform.GuiSprites;
import thaumcraft.device.ArcaneSpaBlockEntity;
import thaumcraft.device.ArcaneSpaMenu;
import thaumcraft.registry.ModSounds;

public class ArcaneSpaScreen extends AbstractContainerScreen<ArcaneSpaMenu> {
    private static final Identifier TEXTURE = Thaumcraft.id("textures/gui/gui_spa.png");

    public ArcaneSpaScreen(ArcaneSpaMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 166);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
    }

    private void sheet(GuiGraphicsExtractor graphics, int x, int y, int width, int height, int u, int v) {
        if (width > 0 && height > 0) {
            GuiBlit.blit(graphics, TEXTURE, x, y, width, height, u, v, width, height, 256, 256, 0xFFFFFFFF);
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        sheet(graphics, leftPos, topPos, imageWidth, imageHeight, 0, 0);
        ArcaneSpaBlockEntity spa = menu.spa();
        if (spa == null) {
            return;
        }
        if (spa.getMix()) {
            sheet(graphics, leftPos + 89, topPos + 35, 8, 8, 208, 16);
        } else {
            sheet(graphics, leftPos + 89, topPos + 35, 8, 8, 208, 32);
        }
        FluidStack fluid = spa.getTank().getFluid();
        if (spa.getTank().getAmount() > 0 && !fluid.isEmpty()) {
            FluidModel model = minecraft.getModelManager().getFluidStateModelSet().get(fluid.getFluid().defaultFluidState());
            TextureAtlasSprite sprite = model.stillMaterial().sprite();
            int tint = model.fluidTintSource() != null ? model.fluidTintSource().colorAsStack(fluid) : -1;
            int color = ARGB.opaque(tint);
            float bar = (float) spa.getTank().getAmount() / spa.getTank().getCapacity();
            for (int cell = 0; cell < 6; cell++) {
                GuiSprites.blit(graphics, sprite, leftPos + 107, topPos + 15 + cell * 8, 8, 8, color);
            }
            sheet(graphics, leftPos + 107, topPos + 15, 10, (int) (48.0F - 48.0F * bar), 107, 15);
        }
        sheet(graphics, leftPos + 106, topPos + 11, 10, 55, 232, 0);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        ArcaneSpaBlockEntity spa = menu.spa();
        if (spa == null) {
            return;
        }
        int relativeX = mouseX - (leftPos + 104);
        int relativeY = mouseY - (topPos + 10);
        if (relativeX >= 0 && relativeY >= 0 && relativeX < 10 && relativeY < 55) {
            FluidStack fluid = spa.getTank().getFluid();
            if (!fluid.isEmpty()) {
                List<Component> lines = new ArrayList<>();
                lines.add(fluid.getHoverName());
                lines.add(Component.translatable("tc.spa.mb", fluid.getAmount()));
                graphics.setTooltipForNextFrame(font, lines, java.util.Optional.empty(), mouseX, mouseY);
            }
        }
        relativeX = mouseX - (leftPos + 88);
        relativeY = mouseY - (topPos + 34);
        if (relativeX >= 0 && relativeY >= 0 && relativeX < 10 && relativeY < 10) {
            graphics.setTooltipForNextFrame(font, Component.translatable(spa.getMix() ? "text.spa.mix.true" : "text.spa.mix.false"), mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        boolean handled = super.mouseClicked(event, doubleClick);
        if (event.button() == InputConstants.MOUSE_BUTTON_LEFT) {
            double relativeX = event.x() - (leftPos + 89);
            double relativeY = event.y() - (topPos + 35);
            if (relativeX >= 0 && relativeY >= 0 && relativeX < 8 && relativeY < 8) {
                minecraft.gameMode.handleInventoryButtonClick(menu.containerId, ArcaneSpaMenu.BUTTON_MIX);
                minecraft.getSoundManager().play(SimpleSoundInstance.forUI(ModSounds.CAMERACLACK.get(), 1.0F, 0.4F));
                return true;
            }
        }
        return handled;
    }
}
