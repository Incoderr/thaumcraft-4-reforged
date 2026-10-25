package thaumcraft.client.platform;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.FeatureRendererType;
import net.minecraft.client.renderer.feature.RenderTypeFeatureRenderer;
import net.minecraft.client.renderer.feature.submit.TranslucentSubmit;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.util.FormattedCharSequence;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterFeatureRenderersEvent;
import net.neoforged.neoforge.client.submit.RenderPhaseKey;
import thaumcraft.Thaumcraft;

@EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class SeeThroughGeometry extends RenderTypeFeatureRenderer<SeeThroughGeometry.Submit> {
    private static final FeatureRendererType<Submit> TYPE = FeatureRendererType.create("thaumcraft_see_through");
    private static final RenderPhaseKey<TranslucentSubmit> PHASE = new RenderPhaseKey<>(collection -> collection.seeThrough);

    private SeeThroughGeometry() {
    }

    public static void submit(SubmitNodeCollector collector, PoseStack poseStack, RenderType renderType, SubmitNodeCollector.CustomGeometryRenderer renderer) {
        PoseStack.Pose pose = poseStack.last().copy();
        collector.submitSpecial(PHASE, new Submit(pose, renderType, renderer, TranslucentSubmit.computeDistanceToCameraSq(pose.pose())));
    }

    public static void submit(SubmitNodeCollector collector, PoseStack poseStack, RenderType renderType, boolean seeThrough, SubmitNodeCollector.CustomGeometryRenderer renderer) {
        if (seeThrough) {
            submit(collector, poseStack, renderType, renderer);
        } else {
            collector.submitCustomGeometry(poseStack, renderType, renderer);
        }
    }

    public static void submitText(SubmitNodeCollector collector, PoseStack poseStack, float x, float y, FormattedCharSequence text, boolean dropShadow, int light, int color) {
        collector.submitText(poseStack, x, y, text, dropShadow, Font.DisplayMode.SEE_THROUGH, light, color, 0, 0);
    }

    @SubscribeEvent
    static void registerRenderer(RegisterFeatureRenderersEvent event) {
        event.register(TYPE, new SeeThroughGeometry());
    }

    @Override
    protected void buildGroup(FeatureFrameContext context, List<Submit> submits) {
        for (Submit submit : submits) {
            submit.renderer().render(submit.pose(), this.getVertexBuilder(submit.renderType()));
        }
    }

    public record Submit(PoseStack.Pose pose, RenderType renderType, SubmitNodeCollector.CustomGeometryRenderer renderer, float distanceToCameraSq) implements TranslucentSubmit {
        @Override
        public FeatureRendererType<Submit> featureType() {
            return TYPE;
        }
    }
}
