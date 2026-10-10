package thaumcraft.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import thaumcraft.Thaumcraft;
import thaumcraft.entity.monster.FireBat;

public class FireBatRenderer extends MobRenderer<FireBat, FireBatRenderState, FireBatModel> {
    private static final Identifier FIRE_BAT = Thaumcraft.id("textures/model/firebat.png");
    private static final Identifier VAMPIRE_BAT = Thaumcraft.id("textures/model/vampirebat.png");

    public FireBatRenderer(EntityRendererProvider.Context context) {
        super(context, new FireBatModel(context.bakeLayer(FireBatModel.LAYER)), 0.25F);
    }

    @Override
    public FireBatRenderState createRenderState() {
        return new FireBatRenderState();
    }

    @Override
    public void extractRenderState(FireBat entity, FireBatRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.hanging = entity.isHanging();
        state.large = entity.isDevil() || entity.isVampire();
        state.vampire = entity.isVampire();
    }

    @Override
    public Identifier getTextureLocation(FireBatRenderState state) {
        return state.vampire ? VAMPIRE_BAT : FIRE_BAT;
    }

    @Override
    protected void scale(FireBatRenderState state, PoseStack poseStack) {
        float scale = state.large ? 0.6F : 0.35F;
        poseStack.scale(scale, scale, scale);
    }

    @Override
    protected void setupRotations(FireBatRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
        if (state.hanging) {
            poseStack.translate(0.0F, -0.1F, 0.0F);
        } else {
            poseStack.translate(0.0F, Mth.cos(state.ageInTicks * 0.3F) * 0.1F, 0.0F);
        }
        super.setupRotations(state, poseStack, bodyRot, entityScale);
    }
}
