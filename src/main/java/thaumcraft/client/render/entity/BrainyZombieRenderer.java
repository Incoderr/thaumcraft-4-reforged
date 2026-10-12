package thaumcraft.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.Identifier;
import thaumcraft.Thaumcraft;

public class BrainyZombieRenderer extends ZombieRenderer {
    private static final Identifier TEXTURE = Thaumcraft.id("textures/model/bzombie.png");

    public BrainyZombieRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Identifier getTextureLocation(ZombieRenderState state) {
        return TEXTURE;
    }
}
