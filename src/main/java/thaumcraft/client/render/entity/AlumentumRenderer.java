package thaumcraft.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import thaumcraft.entity.projectile.AlumentumProjectile;

public final class AlumentumRenderer extends EntityRenderer<AlumentumProjectile, EntityRenderState> {
    private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/entity/steve.png");

    public AlumentumRenderer(EntityRendererProvider.Context context) {
        super(context);
        shadowRadius = 0.1F;
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    @Override
    public Identifier getTextureLocation(EntityRenderState state) {
        return TEXTURE;
    }
}
