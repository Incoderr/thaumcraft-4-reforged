package thaumcraft.client.render.entity.taint;

import java.util.ArrayList;
import java.util.List;
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

public class TaintEntityModel extends EntityModel<TaintMobRenderState> {
    public enum Kind {
        SPIDER("taint_spider", "textures/model/taint_spider.png"),
        TENTACLE("taintacle", "textures/model/taintacle.png"),
        TENTACLE_TINY("taintacle_small", "textures/model/taintacle.png"),
        SPORE("taint_spore", "textures/model/taint_spore.png"),
        SWARMER("taint_spore_swarmer", "textures/model/taint_spore.png"),
        CHICKEN("tainted_chicken", "textures/model/chicken.png"),
        COW("tainted_cow", "textures/model/cow.png"),
        CREEPER("tainted_creeper", "textures/model/creeper.png"),
        PIG("tainted_pig", "textures/model/pig.png"),
        SHEEP("tainted_sheep", "textures/model/sheep.png"),
        VILLAGER("tainted_villager", "textures/model/villager.png");

        private final ModelLayerLocation layer;
        private final net.minecraft.resources.Identifier texture;

        Kind(String layer, String texture) {
            this.layer = new ModelLayerLocation(Thaumcraft.id(layer), "main");
            this.texture = Thaumcraft.id(texture);
        }

        public ModelLayerLocation layer() {
            return this.layer;
        }

        public net.minecraft.resources.Identifier texture() {
            return this.texture;
        }
    }

    private final Kind kind;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart rearLeftLeg;
    private final ModelPart rearRightLeg;
    private final ModelPart leftWing;
    private final ModelPart rightWing;
    private final List<ModelPart> spiderLegs = new ArrayList<>();
    private final List<ModelPart> tentacleSegments = new ArrayList<>();

    public TaintEntityModel(ModelPart root, Kind kind) {
        super(root);
        this.kind = kind;
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.leftLeg = root.getChild("left_leg");
        this.rightLeg = root.getChild("right_leg");
        this.leftArm = root.getChild("left_arm");
        this.rightArm = root.getChild("right_arm");
        this.rearLeftLeg = root.getChild("rear_left_leg");
        this.rearRightLeg = root.getChild("rear_right_leg");
        this.leftWing = root.getChild("left_wing");
        this.rightWing = root.getChild("right_wing");
        if (kind == Kind.SPIDER) {
            for (int index = 0; index < 4; index++) {
                this.spiderLegs.add(this.leftLeg.getChild("leg_" + index));
                this.spiderLegs.add(this.rightLeg.getChild("leg_" + index));
            }
        }
        if (kind == Kind.TENTACLE || kind == Kind.TENTACLE_TINY) {
            ModelPart part = root.getChild("tentacle_base");
            int segmentCount = kind == Kind.TENTACLE ? 9 : 5;
            for (int index = 0; index < segmentCount; index++) {
                part = part.getChild("tentacle_" + index);
                this.tentacleSegments.add(part);
            }
            this.tentacleSegments.getLast().getChild("tentacle_tip").xScale = 0.88F;
            this.tentacleSegments.getLast().getChild("tentacle_tip").yScale = 0.88F;
            this.tentacleSegments.getLast().getChild("tentacle_tip").zScale = 0.88F;
            this.tentacleSegments.getLast().getChild("orb").xScale = 0.88F;
            this.tentacleSegments.getLast().getChild("orb").yScale = 0.88F;
            this.tentacleSegments.getLast().getChild("orb").zScale = 0.88F;
        }
    }

    public static LayerDefinition createLayer(Kind kind) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition leftLeg = root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition rightLeg = root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition rearLeftLeg = root.addOrReplaceChild("rear_left_leg", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition rearRightLeg = root.addOrReplaceChild("rear_right_leg", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_wing", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_wing", CubeListBuilder.create(), PartPose.ZERO);
        switch (kind) {
            case TENTACLE, TENTACLE_TINY -> addTentacle(root, kind == Kind.TENTACLE ? 10 : 6);
            case SPORE -> {
                body.addOrReplaceChild("inner", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, 2.0F, -6.0F, 12.0F, 12.0F, 12.0F), PartPose.offset(0.0F, 24.0F, 0.0F));
                body.addOrReplaceChild("spore", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, 0.0F, -8.0F, 16.0F, 16.0F, 16.0F), PartPose.offset(0.0F, 24.0F, 0.0F));
            }
            case SWARMER -> {
                body.addOrReplaceChild("spore", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, 0.0F, -8.0F, 16.0F, 16.0F, 16.0F), PartPose.ZERO);
                body.addOrReplaceChild("core", CubeListBuilder.create().texOffs(0, 32).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F), PartPose.offset(0.0F, 16.0F, 0.0F));
            }
            case SPIDER -> addSpider(body, head, leftLeg, rightLeg);
            case CHICKEN -> addChicken(body, head, leftLeg, rightLeg, root);
            case COW -> addCow(body, head, leftLeg, rightLeg, rearLeftLeg, rearRightLeg);
            case PIG -> addPig(body, head, leftLeg, rightLeg, rearLeftLeg, rearRightLeg);
            case SHEEP -> addSheep(body, head, leftLeg, rightLeg, rearLeftLeg, rearRightLeg);
            case CREEPER -> addCreeper(body, head, leftLeg, rightLeg, rearLeftLeg, rearRightLeg);
            case VILLAGER -> addVillager(body, head, leftLeg, rightLeg, root);
        }
        int textureHeight = kind == Kind.TENTACLE || kind == Kind.TENTACLE_TINY || kind == Kind.SPORE || kind == Kind.SWARMER || kind == Kind.VILLAGER ? 64 : 32;
        return LayerDefinition.create(mesh, 64, textureHeight);
    }

    private static void addTentacle(PartDefinition root, int length) {
        PartDefinition base = root.addOrReplaceChild(
            "tentacle_base",
            CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F),
            PartPose.offset(0.0F, 12.0F, 0.0F)
        );
        PartDefinition part = base;
        for (int index = 0; index < length - 1; index++) {
            part = part.addOrReplaceChild(
                "tentacle_" + index,
                CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F),
                PartPose.offset(0.0F, -8.0F, 0.0F)
            );
        }
        part.addOrReplaceChild(
            "tentacle_tip",
            CubeListBuilder.create().texOffs(0, 32).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F),
            PartPose.offset(0.0F, -8.0F, 0.0F)
        );
        part.addOrReplaceChild("orb", CubeListBuilder.create().texOffs(0, 56).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F), PartPose.offset(0.0F, -8.0F, 0.0F));
    }

    private static void addSpider(PartDefinition body, PartDefinition head, PartDefinition leftLeg, PartDefinition rightLeg) {
        body.addOrReplaceChild("abdomen", CubeListBuilder.create().texOffs(0, 12).addBox(-5.0F, -4.0F, -6.0F, 10.0F, 8.0F, 12.0F), PartPose.offset(0.0F, 15.0F, 9.0F));
        body.addOrReplaceChild("thorax", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F), PartPose.offset(0.0F, 15.0F, 0.0F));
        head.addOrReplaceChild("head_cube", CubeListBuilder.create().texOffs(32, 4).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F), PartPose.offset(0.0F, 15.0F, -3.0F));
        for (int index = 0; index < 4; index++) {
            float z = 15.0F;
            float legZ = 2.0F - index;
            leftLeg.addOrReplaceChild("leg_" + index, CubeListBuilder.create().texOffs(18, 0).addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F), PartPose.offset(-4.0F, z, legZ));
            rightLeg.addOrReplaceChild("leg_" + index, CubeListBuilder.create().texOffs(18, 0).mirror().addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F), PartPose.offset(4.0F, z, legZ));
        }
    }

    private static void addChicken(PartDefinition body, PartDefinition head, PartDefinition leftLeg, PartDefinition rightLeg, PartDefinition root) {
        body.addOrReplaceChild("body_cube", CubeListBuilder.create().texOffs(0, 9).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F), PartPose.offset(0.0F, 16.0F, 0.0F));
        head.addOrReplaceChild("head_cube", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 3.0F).texOffs(14, 0).addBox(-2.0F, -4.0F, -4.0F, 4.0F, 2.0F, 2.0F).texOffs(14, 4).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 15.0F, -4.0F));
        leftLeg.addOrReplaceChild("leg", CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F), PartPose.offset(-2.0F, 19.0F, 1.0F));
        rightLeg.addOrReplaceChild("leg", CubeListBuilder.create().texOffs(26, 0).mirror().addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F), PartPose.offset(2.0F, 19.0F, 1.0F));
        root.getChild("left_wing").addOrReplaceChild("wing", CubeListBuilder.create().texOffs(24, 13).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F), PartPose.offset(4.0F, 13.0F, 0.0F));
        root.getChild("right_wing").addOrReplaceChild("wing", CubeListBuilder.create().texOffs(24, 13).mirror().addBox(0.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F), PartPose.offset(-4.0F, 13.0F, 0.0F));
    }

    private static void addCow(PartDefinition body, PartDefinition head, PartDefinition leftLeg, PartDefinition rightLeg, PartDefinition rearLeftLeg, PartDefinition rearRightLeg) {
        body.addOrReplaceChild("body_cube", CubeListBuilder.create().texOffs(18, 4).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F).texOffs(52, 0).addBox(-2.0F, 2.0F, -8.0F, 4.0F, 6.0F, 1.0F), PartPose.offset(0.0F, 5.0F, 2.0F));
        head.addOrReplaceChild("head_cube", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F).texOffs(22, 0).addBox(-5.0F, -5.0F, -4.0F, 1.0F, 3.0F, 1.0F).texOffs(22, 0).mirror().addBox(4.0F, -5.0F, -4.0F, 1.0F, 3.0F, 1.0F), PartPose.offset(0.0F, 4.0F, -8.0F));
        addQuadrupedLegs(leftLeg, rightLeg, rearLeftLeg, rearRightLeg, 12.0F, 0.0F, 12.0F, 7.0F, -5.0F);
    }

    private static void addPig(PartDefinition body, PartDefinition head, PartDefinition leftLeg, PartDefinition rightLeg, PartDefinition rearLeftLeg, PartDefinition rearRightLeg) {
        body.addOrReplaceChild("body_cube", CubeListBuilder.create().texOffs(28, 8).addBox(-5.0F, -10.0F, -7.0F, 10.0F, 16.0F, 8.0F), PartPose.offset(0.0F, 11.0F, 2.0F));
        head.addOrReplaceChild("head_cube", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F).texOffs(16, 16).addBox(-2.0F, 0.0F, -9.0F, 4.0F, 3.0F, 1.0F), PartPose.offset(0.0F, 12.0F, -6.0F));
        addQuadrupedLegs(leftLeg, rightLeg, rearLeftLeg, rearRightLeg, 6.0F, 0.0F, 18.0F, 7.0F, -5.0F);
    }

    private static void addSheep(PartDefinition body, PartDefinition head, PartDefinition leftLeg, PartDefinition rightLeg, PartDefinition rearLeftLeg, PartDefinition rearRightLeg) {
        body.addOrReplaceChild("body_cube", CubeListBuilder.create().texOffs(28, 8).addBox(-4.0F, -10.0F, -7.0F, 8.0F, 16.0F, 6.0F), PartPose.offset(0.0F, 5.0F, 2.0F));
        head.addOrReplaceChild("head_cube", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -4.0F, 6.0F, 6.0F, 6.0F), PartPose.offset(0.0F, 6.0F, -8.0F));
        addQuadrupedLegs(leftLeg, rightLeg, rearLeftLeg, rearRightLeg, 12.0F, 0.0F, 12.0F, 7.0F, -5.0F);
    }

    private static void addQuadrupedLegs(PartDefinition leftFront, PartDefinition rightFront, PartDefinition leftRear, PartDefinition rightRear, float length, float inflation, float y, float zFront, float zRear) {
        CubeListBuilder first = CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, length, 4.0F, new net.minecraft.client.model.geom.builders.CubeDeformation(inflation));
        leftFront.addOrReplaceChild("leg", first, PartPose.offset(-3.0F, y, zFront));
        rightFront.addOrReplaceChild("leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, length, 4.0F, new net.minecraft.client.model.geom.builders.CubeDeformation(inflation)), PartPose.offset(3.0F, y, zFront));
        leftRear.addOrReplaceChild("leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, length, 4.0F, new net.minecraft.client.model.geom.builders.CubeDeformation(inflation)), PartPose.offset(-3.0F, y, zRear));
        rightRear.addOrReplaceChild("leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, length, 4.0F, new net.minecraft.client.model.geom.builders.CubeDeformation(inflation)), PartPose.offset(3.0F, y, zRear));
    }

    private static void addCreeper(PartDefinition body, PartDefinition head, PartDefinition leftLeg, PartDefinition rightLeg, PartDefinition rearLeftLeg, PartDefinition rearRightLeg) {
        body.addOrReplaceChild("body_cube", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F), PartPose.offset(0.0F, 4.0F, 0.0F));
        head.addOrReplaceChild("head_cube", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.offset(0.0F, 4.0F, 0.0F));
        addQuadrupedLegs(leftLeg, rightLeg, rearLeftLeg, rearRightLeg, 6.0F, 0.0F, 16.0F, 4.0F, -4.0F);
    }

    private static void addVillager(PartDefinition body, PartDefinition head, PartDefinition leftLeg, PartDefinition rightLeg, PartDefinition root) {
        body.addOrReplaceChild("body_cube", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        head.addOrReplaceChild("head_cube", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F).texOffs(24, 0).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F), PartPose.ZERO);
        leftLeg.addOrReplaceChild("leg", CubeListBuilder.create().texOffs(0, 20).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(2.0F, 12.0F, 0.0F));
        rightLeg.addOrReplaceChild("leg", CubeListBuilder.create().texOffs(0, 20).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(-2.0F, 12.0F, 0.0F));
        root.getChild("left_arm").addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F), PartPose.offset(0.0F, 3.0F, -1.0F));
        root.getChild("right_arm").addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(44, 22).mirror().addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F), PartPose.offset(0.0F, 3.0F, -1.0F));
        root.addOrReplaceChild("folded_arms", CubeListBuilder.create().texOffs(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F), PartPose.ZERO);
    }

    @Override
    public void setupAnim(TaintMobRenderState state) {
        super.setupAnim(state);
        float walk = state.walkAnimationPos * 0.6662F;
        this.head.xRot = state.xRot * Mth.DEG_TO_RAD;
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.leftLeg.xRot = Mth.cos(walk) * 1.4F * state.walkAnimationSpeed;
        this.rightLeg.xRot = Mth.cos(walk + (float) Math.PI) * 1.4F * state.walkAnimationSpeed;
        this.rearLeftLeg.xRot = this.rightLeg.xRot;
        this.rearRightLeg.xRot = this.leftLeg.xRot;
        if (this.kind == Kind.COW || this.kind == Kind.PIG || this.kind == Kind.SHEEP) {
            this.body.xRot = (float) (Math.PI / 2.0);
        }
        if (this.kind == Kind.VILLAGER) {
            this.leftArm.xRot = -0.75F;
            this.rightArm.xRot = -0.75F;
        }
        if (this.kind == Kind.CHICKEN) {
            this.body.xRot = (float) (Math.PI / 2.0);
        }
        if (this.kind == Kind.SPIDER) {
            float[] baseYaw = { (float) Math.PI / 4.0F, -(float) Math.PI / 4.0F, (float) Math.PI / 8.0F, -(float) Math.PI / 8.0F,
                -(float) Math.PI / 8.0F, (float) Math.PI / 8.0F, -(float) Math.PI / 4.0F, (float) Math.PI / 4.0F };
            float[] stride = {
                0.0F, 0.0F, (float) Math.PI, (float) Math.PI, (float) (Math.PI / 2.0), (float) (Math.PI / 2.0), (float) (Math.PI * 1.5), (float) (Math.PI * 1.5)
            };
            for (int index = 0; index < this.spiderLegs.size(); index++) {
                ModelPart leg = this.spiderLegs.get(index);
                float side = index % 2 == 0 ? -1.0F : 1.0F;
                leg.zRot = side * (float) Math.PI / 4.0F * (index < 2 || index > 5 ? 1.0F : 0.74F);
                leg.yRot = baseYaw[index] + (index % 2 == 0 ? -1.0F : 1.0F) * Mth.cos(walk * 2.0F + stride[index]) * 0.4F * state.walkAnimationSpeed;
                leg.xRot = Mth.abs(Mth.sin(walk + stride[index] * 0.5F) * 0.4F) * state.walkAnimationSpeed;
            }
        }
        if (this.kind == Kind.CHICKEN) {
            float flap = (Mth.sin(state.flap) + 1.0F) * state.flapSpeed;
            this.leftWing.zRot = flap;
            this.rightWing.zRot = -flap;
        } else if (this.kind == Kind.TENTACLE || this.kind == Kind.TENTACLE_TINY) {
            float modulation = 0.0625F * 0.2F;
            float intensity = state.flailIntensity;
            float speed = state.agitated ? 3.0F : 1.0F + (state.agitated ? modulation : -modulation);
            float flail = intensity + (state.tentacleAttacking ? modulation : -modulation);
            for (int index = 0; index < this.tentacleSegments.size(); index++) {
                ModelPart segment = this.tentacleSegments.get(index);
                segment.xScale = 0.88F;
                segment.yScale = 0.88F;
                segment.zScale = 0.88F;
                segment.xRot = 0.15F * flail * Mth.sin(state.ageInTicks * 0.1F * speed - index / 2.0F);
                segment.zRot = 0.1F / flail * Mth.sin(state.ageInTicks * 0.15F - index / 2.0F);
            }
        } else if (this.kind == Kind.SPORE || this.kind == Kind.SWARMER) {
            float intensity = state.hurt ? 0.04F : 0.02F;
            this.body.xRot = intensity * Mth.sin(state.ageInTicks * 0.05F);
            this.body.zRot = intensity * Mth.sin(state.ageInTicks * 0.1F);
        }
    }
}
