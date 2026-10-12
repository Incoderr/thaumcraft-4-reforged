package thaumcraft.client.render.entity.taint;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import thaumcraft.Thaumcraft;

public class TaintSheepFurModel extends EntityModel<TaintMobRenderState> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(Thaumcraft.id("taint_sheep_fur"), "main");

    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart frontLeftLeg;
    private final ModelPart frontRightLeg;
    private final ModelPart rearLeftLeg;
    private final ModelPart rearRightLeg;

    public TaintSheepFurModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.frontLeftLeg = root.getChild("front_left_leg");
        this.frontRightLeg = root.getChild("front_right_leg");
        this.rearLeftLeg = root.getChild("rear_left_leg");
        this.rearRightLeg = root.getChild("rear_right_leg");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -4.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, 6.0F, -8.0F));
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(28, 8).addBox(-4.0F, -10.0F, -7.0F, 8.0F, 16.0F, 6.0F, new CubeDeformation(1.75F)), PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, (float) (Math.PI / 2.0), 0.0F, 0.0F));
        CubeListBuilder leg = CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F));
        root.addOrReplaceChild("rear_left_leg", leg, PartPose.offset(-3.0F, 12.0F, 7.0F));
        root.addOrReplaceChild("rear_right_leg", leg, PartPose.offset(3.0F, 12.0F, 7.0F));
        root.addOrReplaceChild("front_left_leg", leg, PartPose.offset(-3.0F, 12.0F, -5.0F));
        root.addOrReplaceChild("front_right_leg", leg, PartPose.offset(3.0F, 12.0F, -5.0F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(TaintMobRenderState state) {
        super.setupAnim(state);
        float walk = state.walkAnimationPos * 0.6662F;
        this.head.xRot = state.xRot * Mth.DEG_TO_RAD;
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.frontLeftLeg.xRot = Mth.cos(walk) * 1.4F * state.walkAnimationSpeed;
        this.rearRightLeg.xRot = this.frontLeftLeg.xRot;
        this.frontRightLeg.xRot = Mth.cos(walk + (float) Math.PI) * 1.4F * state.walkAnimationSpeed;
        this.rearLeftLeg.xRot = this.frontRightLeg.xRot;
    }
}
