package thaumcraft.client.platform;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.rendertype.RenderType;

public final class ClientLayers {
    private ClientLayers() {
    }

    public static <S extends EntityRenderState, M extends EntityModel<S>> EyesLayer<S, M> fullbrightEyes(RenderLayerParent<S, M> parent, Identifier texture) {
        return new EyesLayer<>(parent) {
            @Override
            public RenderType renderType() {
                return TcRenderTypes.eyes(texture);
            }

            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, S state, float yRot, float xRot) {
                int previousLight = state.lightCoords;
                state.lightCoords = 15728880;
                super.submit(poseStack, collector, state.lightCoords, state, yRot, xRot);
                state.lightCoords = previousLight;
            }
        };
    }
}
