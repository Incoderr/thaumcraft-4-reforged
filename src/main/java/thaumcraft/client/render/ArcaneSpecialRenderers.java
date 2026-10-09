package thaumcraft.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public final class ArcaneSpecialRenderers {
    private ArcaneSpecialRenderers() {
    }

    private static float viewerTicks() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.player == null ? 0.0F : minecraft.player.tickCount;
    }

    private static void extents(Consumer<Vector3fc> output, float min, float max) {
        for (float x : new float[] {min, max}) {
            for (float y : new float[] {min, max}) {
                for (float z : new float[] {min, max}) {
                    output.accept(new Vector3f(x, y, z));
                }
            }
        }
    }

    public record RunicMatrix() implements NoDataSpecialModelRenderer {
        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
            RunicMatrixRendering.submit(collector, poseStack, lightCoords, false, false, false, 0.0F, 0, 0, viewerTicks());
        }

        @Override
        public void getExtents(Consumer<Vector3fc> output) {
            extents(output, 0.2F, 0.8F);
        }

        public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
            public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

            @Override
            public RunicMatrix bake(BakingContext context) {
                return new RunicMatrix();
            }

            @Override
            public MapCodec<Unbaked> type() {
                return MAP_CODEC;
            }
        }
    }

    public record ChargeRelay() implements NoDataSpecialModelRenderer {
        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
            poseStack.pushPose();
            poseStack.translate(0.5F, 0.5F, 0.5F);
            poseStack.scale(1.5F, 1.5F, 1.5F);
            poseStack.translate(-0.5F, -0.25F, -0.5F);
            NodeDeviceRendering.charger(collector, poseStack, lightCoords, -1, false, viewerTicks());
            poseStack.popPose();
        }

        @Override
        public void getExtents(Consumer<Vector3fc> output) {
            extents(output, 0.2F, 0.8F);
        }

        public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
            public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

            @Override
            public ChargeRelay bake(BakingContext context) {
                return new ChargeRelay();
            }

            @Override
            public MapCodec<Unbaked> type() {
                return MAP_CODEC;
            }
        }
    }
}
