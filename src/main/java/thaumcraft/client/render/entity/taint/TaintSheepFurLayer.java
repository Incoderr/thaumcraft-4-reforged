package thaumcraft.client.render.entity.taint;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.Identifier;
import thaumcraft.Thaumcraft;

public class TaintSheepFurLayer extends RenderLayer<TaintMobRenderState, TaintEntityModel> {
    private static final Identifier TEXTURE = Thaumcraft.id("textures/model/sheep_fur.png");
    private final TaintSheepFurModel model;

    public TaintSheepFurLayer(RenderLayerParent<TaintMobRenderState, TaintEntityModel> renderer, TaintSheepFurModel model) {
        super(renderer);
        this.model = model;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, TaintMobRenderState state, float yRot, float xRot) {
        if (!state.sheared) {
            renderColoredCutoutModel(this.model, TEXTURE, poseStack, collector, lightCoords, state, 0xFFFFFFFF, 0);
        }
    }
}
