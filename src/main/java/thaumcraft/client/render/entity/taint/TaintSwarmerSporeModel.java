package thaumcraft.client.render.entity.taint;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import thaumcraft.Thaumcraft;
import thaumcraft.client.platform.TcRenderTypes;

public class TaintSwarmerSporeModel extends EntityModel<TaintMobRenderState> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(Thaumcraft.id("taint_swarmer_spore"), "main");

    private final ModelPart sporeScale;
    private final ModelPart sporeOffset;
    private final ModelPart spore;

    public TaintSwarmerSporeModel(ModelPart root) {
        super(root, TcRenderTypes::entityTranslucent);
        this.sporeScale = root.getChild("spore_transform").getChild("spore_scale");
        this.sporeOffset = this.sporeScale.getChild("spore_offset");
        this.spore = this.sporeOffset.getChild("spore");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition transform = root.addOrReplaceChild(
            "spore_transform",
            CubeListBuilder.create(),
            PartPose.offset(0.0F, 25.6F, 0.0F)
        );
        PartDefinition scale = transform.addOrReplaceChild("spore_scale", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition offset = scale.addOrReplaceChild("spore_offset", CubeListBuilder.create(), PartPose.ZERO);
        offset.addOrReplaceChild(
            "spore",
            CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, 0.0F, -8.0F, 16.0F, 16.0F, 16.0F),
            PartPose.ZERO
        );
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(TaintMobRenderState state) {
        super.setupAnim(state);
        float scaleX = -0.07F * state.displaySize - 0.025F * Mth.sin(state.ageInTicks * 0.075F);
        float scaleY = -0.07F * state.displaySize + 0.025F * Mth.sin(state.ageInTicks * 0.075F);
        this.sporeScale.xScale = scaleX;
        this.sporeScale.yScale = scaleY;
        this.sporeScale.zScale = scaleX;
        this.sporeOffset.y = -scaleY * 8.0F;
        if (state.hurt) {
            this.spore.xRot = 0.04F * Mth.sin(state.ageInTicks * 0.05F);
            this.spore.zRot = 0.04F * Mth.sin(state.ageInTicks * 0.1F);
        } else {
            this.spore.xRot = 0.02F * Mth.sin(state.ageInTicks * 0.05F);
            this.spore.zRot = 0.02F * Mth.sin(state.ageInTicks * 0.1F);
        }
    }
}
