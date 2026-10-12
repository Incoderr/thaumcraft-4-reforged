package thaumcraft.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.resources.Identifier;
import thaumcraft.Thaumcraft;

public final class ThaumicSlimeRenderer extends SlimeRenderer {
    private static final Identifier TEXTURE = Thaumcraft.id("textures/model/tslime.png");

    public ThaumicSlimeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Identifier getTextureLocation(SlimeRenderState state) {
        return TEXTURE;
    }
}
