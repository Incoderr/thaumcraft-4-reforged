package thaumcraft.client.platform;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import thaumcraft.Thaumcraft;

public final class WarpVignetteOverlay {
    private static final Identifier TEXTURE = Thaumcraft.id("textures/misc/vignette.png");

    private WarpVignetteOverlay() {
    }

    public static void draw(RenderGuiLayerEvent.Post event, int alpha) {
        GuiGraphicsExtractor graphics = event.getGuiGraphics();
        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            TEXTURE,
            0,
            0,
            0.0F,
            0.0F,
            graphics.guiWidth(),
            graphics.guiHeight(),
            graphics.guiWidth(),
            graphics.guiHeight(),
            0x00FFFFFF | (alpha << 24)
        );
    }
}
