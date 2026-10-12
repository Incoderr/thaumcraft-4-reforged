package thaumcraft.client.render.entity.eldritch;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Difficulty;
import thaumcraft.Thaumcraft;
import thaumcraft.client.platform.AlphaMobRenderer;
import thaumcraft.taint.TaintBiomes;
import thaumcraft.entity.monster.eldritch.EldritchGuardian;

public class EldritchGuardianRenderer extends AlphaMobRenderer<EldritchGuardian, GuardianRenderState, EldritchGuardianModel> {
    private static final Identifier TEXTURE = Thaumcraft.id("textures/model/eldritch_guardian.png");

    public EldritchGuardianRenderer(EntityRendererProvider.Context context) {
        super(context, new EldritchGuardianModel(context.bakeLayer(EldritchGuardianModel.LAYER)), 0.5F);
    }

    @Override
    public GuardianRenderState createRenderState() {
        return new GuardianRenderState();
    }

    @Override
    public void extractRenderState(EldritchGuardian entity, GuardianRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.armLiftLeft = entity.armLiftLeft;
        state.armLiftRight = entity.armLiftRight;
        if (entity.level().getBiome(entity.blockPosition()).is(TaintBiomes.OUTER_LANDS)) {
            state.entityAlpha = 1.0F;
        } else {
            float endDistance = entity.level().getDifficulty() == Difficulty.HARD ? 576.0F : 1024.0F;
            float distance = (float) state.distanceToCameraSq;
            state.entityAlpha = distance < 256.0F ? 0.6F : 0.6F * Math.clamp((endDistance - distance) / (endDistance - 256.0F), 0.0F, 1.0F);
        }
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
