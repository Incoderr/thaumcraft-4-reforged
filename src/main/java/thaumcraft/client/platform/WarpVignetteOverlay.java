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

    public static void draw(RenderGuiLayerEvent.Post event, int strength) {
        GuiGraphicsExtractor graphics = event.getGuiGraphics();
        int brightness = Math.clamp(strength, 0, 255);
        graphics.blit(
            RenderPipelines.VIGNETTE,
            TEXTURE,
            0,
            0,
            0.0F,
            0.0F,
            graphics.guiWidth(),
            graphics.guiHeight(),
            256,
            256,
            256,
            256,
            0xFF000000 | brightness << 16 | brightness << 8 | brightness
        );
    }
}
