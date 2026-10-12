package thaumcraft.client.render.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.Identifier;
import thaumcraft.entity.projectile.DartProjectile;

public final class DartRenderer extends ArrowRenderer<DartProjectile, ArrowRenderState> {
    private static final Identifier ARROW = Identifier.withDefaultNamespace("textures/entity/projectiles/arrow.png");

    public DartRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ArrowRenderState createRenderState() {
        return new ArrowRenderState();
    }

    @Override
    protected Identifier getTextureLocation(ArrowRenderState state) {
        return ARROW;
    }
}
