package thaumcraft.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.core.Direction;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public final class NodeDeviceSpecialRenderers {
    private NodeDeviceSpecialRenderers() {
    }

    private static int viewerTicks() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.player == null ? 0 : minecraft.player.tickCount;
    }

    private static void cubeExtents(Consumer<Vector3fc> output, float height) {
        for (float x : new float[] {0.0F, 1.0F}) {
            for (float y : new float[] {0.0F, height}) {
                for (float z : new float[] {0.0F, 1.0F}) {
                    output.accept(new Vector3f(x, y, z));
                }
            }
        }
    }

    public record Stabilizer(boolean advanced) implements NoDataSpecialModelRenderer {
        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
            NodeDeviceRendering.stabilizer(collector, poseStack, lightCoords, advanced ? 2 : 1, 0, viewerTicks(), false);
        }

        @Override
        public void getExtents(Consumer<Vector3fc> output) {
            cubeExtents(output, 1.0F);
        }

        public record Unbaked(boolean advanced) implements NoDataSpecialModelRenderer.Unbaked {
            public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.BOOL.optionalFieldOf("advanced", false).forGetter(Unbaked::advanced)
            ).apply(instance, Unbaked::new));

            @Override
            public Stabilizer bake(BakingContext context) {
                return new Stabilizer(advanced);
            }

            @Override
            public MapCodec<Unbaked> type() {
                return MAP_CODEC;
            }
        }
    }

    public record Transducer() implements NoDataSpecialModelRenderer {
        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
            NodeDeviceRendering.transducer(collector, poseStack, lightCoords, 0, 0, viewerTicks(), false);
        }

        @Override
        public void getExtents(Consumer<Vector3fc> output) {
            cubeExtents(output, 1.0F);
        }

        public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
            public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

            @Override
            public Transducer bake(BakingContext context) {
                return new Transducer();
            }

            @Override
            public MapCodec<Unbaked> type() {
                return MAP_CODEC;
            }
        }
    }

    public record Relay() implements NoDataSpecialModelRenderer {
        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
            NodeDeviceRendering.relay(collector, poseStack, lightCoords, Direction.UP, -1, true, viewerTicks());
        }

        @Override
        public void getExtents(Consumer<Vector3fc> output) {
            for (float x : new float[] {0.3F, 0.7F}) {
                for (float y : new float[] {0.0F, 0.5F}) {
                    for (float z : new float[] {0.3F, 0.7F}) {
                        output.accept(new Vector3f(x, y, z));
                    }
                }
            }
        }

        public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
            public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

            @Override
            public Relay bake(BakingContext context) {
                return new Relay();
            }

            @Override
            public MapCodec<Unbaked> type() {
                return MAP_CODEC;
            }
        }
    }
}
