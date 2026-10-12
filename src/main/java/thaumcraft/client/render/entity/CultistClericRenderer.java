package thaumcraft.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import thaumcraft.client.geom.FloatyLineGeometry;
import thaumcraft.client.platform.TcRenderTypes;
import thaumcraft.client.platform.VertexQuadSink;
import thaumcraft.client.render.WorldRendering;
import thaumcraft.entity.monster.CultistCleric;

public final class CultistClericRenderer extends CultistMobRenderer<CultistCleric> {
    public CultistClericRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addLayer(new RitualistBeamLayer(this));
    }

    @Override
    protected void scale(CultistRenderState state, PoseStack poseStack) {
        super.scale(state, poseStack);
        if (state.ritualist) {
            poseStack.translate(0.0, state.ritualBob, 0.0);
        }
    }

    @Override
    public void extractRenderState(CultistCleric entity, CultistRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.ritualist = entity.isRitualist();
        state.ritualBob = state.ritualist ? (float) Math.sin((entity.tickCount + partialTicks + new Random(entity.getId()).nextInt(1000)) / 9.0F) * 0.1F + 0.21F : 0.0F;
        if (state.ritualist && entity.hasHome()) {
            BlockPos home = entity.getHomePosition();
            state.beamEndX = (float) (home.getX() + 0.5 - entity.getX());
            state.beamEndY = (float) (home.getY() + 1.5 - entity.getY() - state.ritualBob);
            state.beamEndZ = (float) (home.getZ() + 0.5 - entity.getZ());
        } else {
            state.ritualist = false;
        }
    }

    private static final class RitualistBeamLayer extends RenderLayer<CultistRenderState, net.minecraft.client.model.HumanoidModel<CultistRenderState>> {
        private static final Identifier TEXTURE = WorldRendering.WISPY;

        private RitualistBeamLayer(net.minecraft.client.renderer.entity.RenderLayerParent<CultistRenderState, net.minecraft.client.model.HumanoidModel<CultistRenderState>> parent) {
            super(parent);
        }

        @Override
        public void submit(PoseStack poseStack, net.minecraft.client.renderer.SubmitNodeCollector collector, int light, CultistRenderState state, float yRot, float xRot) {
            if (!state.ritualist) {
                return;
            }
            float distance = Math.min(state.ageInTicks, 10.0F) / 10.0F;
            FloatyLineGeometry.Params params = new FloatyLineGeometry.Params(
                0.0,
                state.eyeHeight * 1.2,
                0.0,
                state.beamEndX,
                state.beamEndY,
                state.beamEndZ,
                0.0,
                0.0,
                0.0,
                (float) (System.nanoTime() / 30000000L),
                1114129,
                -0.03F,
                distance,
                0.25F
            );
            collector.submitCustomGeometry(poseStack, TcRenderTypes.translucent(TEXTURE), (pose, buffer) -> FloatyLineGeometry.render(params, new VertexQuadSink(pose, buffer)));
        }
    }
}
