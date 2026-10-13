package thaumcraft.client.render.entity.eldritch;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import thaumcraft.Thaumcraft;
import thaumcraft.client.platform.AlphaMobRenderer;
import thaumcraft.entity.boss.EldritchWarden;

public final class EldritchWardenRenderer extends AlphaMobRenderer<EldritchWarden, GuardianRenderState, EldritchGuardianModel> {
    private static final Identifier TEXTURE = Thaumcraft.id("textures/model/eldritch_warden.png");

    public EldritchWardenRenderer(EntityRendererProvider.Context context) {
        super(context, new EldritchGuardianModel(context.bakeLayer(EldritchGuardianModel.LAYER)), 0.6F);
    }

    @Override
    public GuardianRenderState createRenderState() {
        return new GuardianRenderState();
    }

    @Override
    public void extractRenderState(EldritchWarden entity, GuardianRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.armLiftLeft = entity.getArmLiftLeft();
        state.armLiftRight = entity.getArmLiftRight();
        state.entityAlpha = 1.0F;
    }

    @Override
    public Identifier getTextureLocation(GuardianRenderState state) {
        return TEXTURE;
    }

    @Override
    protected float getEntityAlpha(GuardianRenderState state) {
        return state.entityAlpha;
    }
}
