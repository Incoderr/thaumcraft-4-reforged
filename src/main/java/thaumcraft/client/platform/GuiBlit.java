package thaumcraft.client.platform;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public final class GuiBlit {
    private GuiBlit() {
    }

    public static void blit(GuiGraphicsExtractor graphics, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        blit(graphics, texture, x, y, u, v, width, height, textureWidth, textureHeight, -1);
    }

    public static void blit(GuiGraphicsExtractor graphics, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int color) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, textureWidth, textureHeight, color);
    }

    public static void blit(
        GuiGraphicsExtractor graphics,
        Identifier texture,
        int x,
        int y,
        int width,
        int height,
        int u,
        int v,
        int regionWidth,
        int regionHeight,
        int textureWidth,
        int textureHeight,
        int argb
    ) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, (float) u, (float) v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, argb);
    }
}
