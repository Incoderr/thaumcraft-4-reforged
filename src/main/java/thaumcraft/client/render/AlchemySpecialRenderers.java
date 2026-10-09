package thaumcraft.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import java.util.function.Consumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public final class AlchemySpecialRenderers {
    private AlchemySpecialRenderers() {
    }

    public record Alembic() implements NoDataSpecialModelRenderer {
        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
            AlchemyRendering.alembic(collector, poseStack, lightCoords, false, Direction.NORTH, false, false, null, new boolean[6]);
        }

        @Override
        public void getExtents(Consumer<Vector3fc> output) {
            for (float x : new float[] {0.0F, 1.0F}) {
                for (float y : new float[] {0.0F, 1.0F}) {
                    for (float z : new float[] {0.0F, 1.0F}) {
                        output.accept(new Vector3f(x, y, z));
                    }
                }
            }
        }

        public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
            public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

            @Override
            public Alembic bake(BakingContext context) {
                return new Alembic();
            }

            @Override
            public MapCodec<Unbaked> type() {
                return MAP_CODEC;
            }
        }
    }

    public record FluxScrubber() implements NoDataSpecialModelRenderer {
        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
            AlchemyRendering.fluxScrubber(collector, poseStack, lightCoords, Direction.DOWN, EssentiaRendering.viewerTicks());
        }

        @Override
        public void getExtents(Consumer<Vector3fc> output) {
            for (float x : new float[] {0.0F, 1.0F}) {
                for (float y : new float[] {0.0F, 1.0F}) {
                    for (float z : new float[] {0.0F, 1.0F}) {
                        output.accept(new Vector3f(x, y, z));
                    }
                }
            }
        }

        public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
            public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

            @Override
            public FluxScrubber bake(BakingContext context) {
                return new FluxScrubber();
            }

            @Override
            public MapCodec<Unbaked> type() {
                return MAP_CODEC;
            }
        }
    }

    public record Bellows() implements NoDataSpecialModelRenderer {
        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
            AlchemyRendering.bellows(collector, poseStack, lightCoords, Direction.NORTH, Mth.sin(EssentiaRendering.viewerTicks() / 8.0F) * 0.3F + 0.7F);
        }

        @Override
        public void getExtents(Consumer<Vector3fc> output) {
            for (float x : new float[] {0.0F, 1.0F}) {
                for (float y : new float[] {0.0F, 1.0F}) {
                    for (float z : new float[] {0.0F, 1.0F}) {
                        output.accept(new Vector3f(x, y, z));
                    }
                }
            }
        }

        public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
            public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

            @Override
            public Bellows bake(BakingContext context) {
                return new Bellows();
            }

            @Override
            public MapCodec<Unbaked> type() {
                return MAP_CODEC;
            }
        }
    }
}
