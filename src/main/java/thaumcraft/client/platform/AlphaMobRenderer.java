package thaumcraft.client.platform;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.world.entity.Mob;

public abstract class AlphaMobRenderer<T extends Mob, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends MobRenderer<T, S, M> {
    protected AlphaMobRenderer(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    protected abstract float getEntityAlpha(S state);

    @Override
    protected int getModelTint(S state) {
        int alpha = Math.clamp((int) (getEntityAlpha(state) * 255.0F), 0, 255);
        return alpha << 24 | 0xFFFFFF;
    }

    @Override
    protected RenderType getRenderType(S state, boolean bodyVisible, boolean translucent, boolean glowing) {
        if (!bodyVisible || translucent || glowing) {
            return super.getRenderType(state, bodyVisible, translucent, glowing);
        }
        return RenderTypes.entityTranslucent(getTextureLocation(state));
    }
}
