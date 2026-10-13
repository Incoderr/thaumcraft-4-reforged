package thaumcraft.client.render.entity.golem;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public final class GolemModel extends EntityModel<GolemRenderState> {
    private final ModelPart head;
    private final ModelPart headAdvanced;
    private final ModelPart headSmart;
    private final ModelPart headObserver;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightArmStrong;
    private final ModelPart leftArmStrong;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart rightLegFast;
    private final ModelPart leftLegFast;

    public GolemModel(ModelPart root) {
        super(root, RenderTypes::entityCutoutNoCull);
        ModelPart body = root.getChild("golem");
        head = body.getChild("head");
        headAdvanced = body.getChild("head_advanced");
        headSmart = body.getChild("head_smart");
        headObserver = body.getChild("head_observer");
        rightArm = body.getChild("right_arm");
        leftArm = body.getChild("left_arm");
        rightArmStrong = body.getChild("right_arm_strong");
        leftArmStrong = body.getChild("left_arm_strong");
        rightLeg = body.getChild("right_leg");
        leftLeg = body.getChild("left_leg");
        rightLegFast = body.getChild("right_leg_strong");
        leftLegFast = body.getChild("left_leg_strong");
    }

    public static float triangle(float value, float period) {
        return (Math.abs(value % period - period * 0.5F) - period * 0.25F) / (period * 0.25F);
    }

    public static float armAngle(GolemRenderState state, boolean left, float current) {
        if (state.core < 0 || state.bootup < 0.0F) {
            return 0.0F;
        }
        if (state.actionActive) {
            return -2.0F + 1.5F * triangle(state.actionTimer, 5.0F);
        }
        if (left && state.leftArmActive) {
            return -2.0F + 1.5F * triangle(state.leftArm, 20.0F);
        }
        if (!left && state.rightArmActive) {
            return -2.0F + 1.5F * triangle(state.rightArm, 20.0F);
        }
        if (state.carrying || state.fishing) {
            return -1.0F;
        }
        float swing = 1.5F * triangle(state.walkAnimationPos, 13.0F);
        return (left ? -0.2F - swing : -0.2F + swing) * state.walkAnimationSpeed;
    }

    @Override
    public void setupAnim(GolemRenderState state) {
        super.setupAnim(state);
        float yaw = state.yRot * ((float) Math.PI / 180.0F);
        float pitch = state.xRot * ((float) Math.PI / 180.0F);
        boolean sleepingHead = state.core < 0 || state.inactive;
        headAdvanced.visible = state.advanced;
        head.visible = !state.advanced && state.core != 2 && state.core != 3;
        headSmart.visible = !state.advanced && state.core == 2;
        headObserver.visible = !state.advanced && state.core == 3;
        ModelPart visibleHead = state.advanced ? headAdvanced : state.core == 2 ? headSmart : state.core == 3 ? headObserver : head;
        visibleHead.yRot = sleepingHead || state.bootup > 0 ? 0.0F : yaw;
        visibleHead.xRot = sleepingHead ? 0.57595867F : state.bootup > 0 ? state.bootup / (180.0F / (float) Math.PI) : pitch;
        float legSwing = state.core < 0 ? 0.0F : 1.5F * triangle(state.walkAnimationPos, 13.0F) * state.walkAnimationSpeed;
        rightLeg.xRot = -legSwing;
        leftLeg.xRot = legSwing;
        boolean strong = state.core == 4;
        rightLeg.visible = !strong;
        leftLeg.visible = !strong;
        rightLegFast.visible = strong;
        leftLegFast.visible = strong;
        rightLegFast.xRot = rightLeg.xRot;
        leftLegFast.xRot = leftLeg.xRot;
        rightArm.xRot = armAngle(state, false, rightArm.xRot);
        leftArm.xRot = armAngle(state, true, leftArm.xRot);
        if (state.core == 6) {
            float angle = (1.0F - (0.5F + Math.min(64, state.carryLimit) / 128.0F)) * 25.0F * ((float) Math.PI / 180.0F);
            leftArm.zRot = angle;
            rightArm.zRot = -angle;
        } else {
            leftArm.zRot = 0.0F;
            rightArm.zRot = 0.0F;
        }
        boolean heavy = state.core == 4;
        rightArm.visible = !heavy;
        leftArm.visible = !heavy;
        rightArmStrong.visible = heavy;
        leftArmStrong.visible = heavy;
        rightArmStrong.xRot = rightArm.xRot;
        leftArmStrong.xRot = leftArm.xRot;
    }
}
