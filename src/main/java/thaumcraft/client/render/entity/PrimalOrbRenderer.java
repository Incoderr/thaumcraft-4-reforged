package thaumcraft.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.ARGB;
import org.joml.Quaternionf;
import thaumcraft.client.fx.ParticleFx;
import thaumcraft.client.platform.TcRenderTypes;
import thaumcraft.entity.projectile.PrimalOrb;

public class PrimalOrbRenderer extends EntityRenderer<PrimalOrb, SpriteRenderState> {
    private static final int[] COLORS = {16777215, 16777086, 16727041, 37119, 40960, 15650047, 5592439};

    public PrimalOrbRenderer(EntityRendererProvider.Context context) {
        super(context);
        shadowRadius = 0.0F;
    }

    @Override
    public SpriteRenderState createRenderState() {
        return new SpriteRenderState();
    }

    @Override
    public void extractRenderState(PrimalOrb entity, SpriteRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.seed = entity.getId();
    }

    @Override
    public void submit(SpriteRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        int age = (int) state.ageInTicks;
        float spin = age / 80.0F;
        float grow = Math.min(age, 10) / 10.0F;
        int seed = state.seed;
        collector.submitCustomGeometry(poseStack, RenderTypes.dragonRays(), (pose, buffer) -> {
            Random random = new Random(seed);
            Quaternionf rotation = new Quaternionf();
            for (int index = 0; index < 12; index++) {
                rotation.rotationX(random.nextFloat() * (float) (Math.PI * 2))
                    .rotateY(random.nextFloat() * (float) (Math.PI * 2))
                    .rotateZ(random.nextFloat() * (float) (Math.PI * 2))
                    .rotateX(random.nextFloat() * (float) (Math.PI * 2))
                    .rotateY(random.nextFloat() * (float) (Math.PI * 2))
                    .rotateZ(random.nextFloat() * (float) (Math.PI * 2) + spin * (float) (Math.PI * 2));
                pose.rotate(rotation);
                float length = (random.nextFloat() * 20.0F + 5.0F) / (30.0F / grow);
                float width = (random.nextFloat() * 2.0F + 1.0F) / (30.0F / grow);
                int inner = 0xFFFFFFFF;
                int outer = ARGB.color(0, COLORS[index / 2 + 1]);
                buffer.addVertex(pose, 0.0F, 0.0F, 0.0F).setColor(inner);
                buffer.addVertex(pose, -0.866F * width, length, -0.5F * width).setColor(outer);
                buffer.addVertex(pose, 0.866F * width, length, -0.5F * width).setColor(outer);
                buffer.addVertex(pose, 0.0F, 0.0F, 0.0F).setColor(inner);
                buffer.addVertex(pose, 0.866F * width, length, -0.5F * width).setColor(outer);
                buffer.addVertex(pose, 0.0F, length, width).setColor(outer);
                buffer.addVertex(pose, 0.0F, 0.0F, 0.0F).setColor(inner);
                buffer.addVertex(pose, 0.0F, length, width).setColor(outer);
                buffer.addVertex(pose, -0.866F * width, length, -0.5F * width).setColor(outer);
            }
        });
        float u0 = age % 13 / 16.0F;
        float v0 = 0.125F;
        Billboards.submit(poseStack, collector, camera, TcRenderTypes.additive(ParticleFx.PARTICLES), u0, u0 + 0.0624375F, v0, v0 + 0.0624375F, 0.5F, 0xCCFFFFFF, 0xF000F0);
        super.submit(state, poseStack, collector, camera);
    }
}
