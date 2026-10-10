package thaumcraft.client.platform;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public final class GuiSprites {
    private GuiSprites() {
    }

    public static void blit(GuiGraphicsExtractor graphics, TextureAtlasSprite sprite, int x, int y, int width, int height, int argb) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, y, width, height, argb);
    }
}
