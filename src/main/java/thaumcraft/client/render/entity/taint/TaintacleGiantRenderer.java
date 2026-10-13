package thaumcraft.client.render.entity.taint;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import thaumcraft.entity.boss.TaintacleGiant;

public final class TaintacleGiantRenderer extends TaintMobRenderer<TaintacleGiant> {
    public TaintacleGiantRenderer(EntityRendererProvider.Context context) {
        super(context, TaintEntityModel.Kind.TENTACLE, 1.0F);
    }

    @Override
    protected void scale(TaintMobRenderState state, PoseStack poseStack) {
        super.scale(state, poseStack);
        poseStack.scale(1.33F, 1.33F, 1.33F);
    }
}
