package thaumcraft.client.render.entity.eldritch;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import thaumcraft.Thaumcraft;

public class EldritchGuardianModel extends HumanoidModel<GuardianRenderState> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(Thaumcraft.id("eldritch_guardian"), "main");

    public EldritchGuardianModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.getChild("head");
        head.addOrReplaceChild("hood", CubeListBuilder.create().texOffs(40, 12).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.4F)), PartPose.ZERO);
        head.addOrReplaceChild("hood_eye", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.1F)), PartPose.ZERO);
        PartDefinition body = root.getChild("body");
        body.addOrReplaceChild("chestplate", CubeListBuilder.create().texOffs(56, 45).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 7.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.ZERO);
        body.addOrReplaceChild("backplate", CubeListBuilder.create().texOffs(36, 45).addBox(-4.0F, -6.0F, 2.0F, 8.0F, 11.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.ZERO);
        root.getChild("right_arm").addOrReplaceChild("right_shoulder", CubeListBuilder.create().texOffs(56, 35).addBox(-3.0F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.1F)), PartPose.ZERO);
        root.getChild("left_arm").addOrReplaceChild("left_shoulder", CubeListBuilder.create().texOffs(56, 35).mirror().addBox(-2.0F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.1F)), PartPose.ZERO);
        return LayerDefinition.create(mesh, 128, 64);
    }

    @Override
    public void setupAnim(GuardianRenderState state) {
        super.setupAnim(state);
        this.leftArm.xRot -= state.armLiftLeft;
        this.rightArm.xRot -= state.armLiftRight;
    }
}
