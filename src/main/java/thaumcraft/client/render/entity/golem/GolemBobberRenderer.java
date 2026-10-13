package thaumcraft.client.render.entity.golem;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import thaumcraft.client.fx.ParticleFx;
import thaumcraft.client.platform.Billboards;
import thaumcraft.client.platform.TcRenderTypes;
import thaumcraft.client.render.WorldRendering;
import thaumcraft.entity.golem.GolemBobber;

public final class GolemBobberRenderer extends EntityRenderer<GolemBobber, GolemBobberRenderState> {
    public GolemBobberRenderer(EntityRendererProvider.Context context) {
        super(context);
        shadowRadius = 0.0F;
    }

    @Override
    public GolemBobberRenderState createRenderState() {
        return new GolemBobberRenderState();
    }

    @Override
    public void extractRenderState(GolemBobber entity, GolemBobberRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.bobberX = Mth.lerp(partialTicks, entity.xo, entity.getX());
        state.bobberY = Mth.lerp(partialTicks, entity.yo, entity.getY());
        state.bobberZ = Mth.lerp(partialTicks, entity.zo, entity.getZ());
        var fisher = entity.getFisher();
        state.hasFisher = fisher != null;
        if (fisher != null) {
            state.fisherX = Mth.lerp(partialTicks, fisher.xo, fisher.getX());
            state.fisherY = Mth.lerp(partialTicks, fisher.yo, fisher.getY()) + fisher.getEyeHeight() - 0.4;
            state.fisherZ = Mth.lerp(partialTicks, fisher.zo, fisher.getZ());
        }
    }

    @Override
    public void submit(GolemBobberRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        Billboards.submit(
            poseStack,
            collector,
            camera,
            TcRenderTypes.entityCutoutNoCull(ParticleFx.PARTICLES),
            1.0F / 16.0F,
            2.0F / 16.0F,
            2.0F / 16.0F,
            3.0F / 16.0F,
            0.25F,
            0xFFFFFFFF,
            0xF000F0
        );
        if (state.hasFisher) {
            WorldRendering.submitFloatyLine(
                collector,
                poseStack,
                net.minecraft.core.BlockPos.containing(state.bobberX, state.bobberY, state.bobberZ),
                state.fisherX,
                state.fisherY,
                state.fisherZ,
                state.bobberX,
                state.bobberY,
                state.bobberZ,
                0xFF202020,
                32.0F,
                System.nanoTime()
            );
        }
        super.submit(state, poseStack, collector, camera);
    }
}
