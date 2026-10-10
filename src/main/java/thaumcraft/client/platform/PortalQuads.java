package thaumcraft.client.platform;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public final class PortalQuads {
    private PortalQuads() {
    }

    public static void submit(SubmitNodeCollector collector, PoseStack poseStack, float low, float high, float z) {
        collector.submitCustomGeometry(poseStack, RenderTypes.endPortal(), (pose, buffer) -> {
            buffer.addVertex(pose, low, low, z);
            buffer.addVertex(pose, low, high, z);
            buffer.addVertex(pose, high, high, z);
            buffer.addVertex(pose, high, low, z);
        });
    }
}
