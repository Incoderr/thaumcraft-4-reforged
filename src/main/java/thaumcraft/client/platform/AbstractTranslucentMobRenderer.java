package thaumcraft.client.platform;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Mob;

public abstract class AbstractTranslucentMobRenderer<E extends Mob, S extends LivingEntityRenderState, M extends EntityModel<S>> extends MobRenderer<E, S, M> {
    protected AbstractTranslucentMobRenderer(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Override
    protected net.minecraft.client.renderer.rendertype.RenderType getRenderType(S state, boolean isBodyVisible, boolean forceTransparent, boolean appearsGlowing) {
        return TcRenderTypes.entityTranslucentNoDepth(this.getTextureLocation(state));
    }
}
