package thaumcraft.client.render.entity.golem;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public final class GolemAccessoriesModel extends EntityModel<GolemRenderState> {
    private final ModelPart fez;
    private final ModelPart plate;
    private final ModelPart plateLeft;
    private final ModelPart plateRight;
    private final ModelPart hat;
    private final ModelPart glasses;
    private final ModelPart visor;
    private final ModelPart hatRim;
    private final ModelPart dartgun;
    private final ModelPart mace;
    private final ModelPart bowtie;
    private final ModelPart headJar;
    private final ModelPart headBrain;
    private final ModelPart evilHead;

    public GolemAccessoriesModel(ModelPart root) {
        super(root, RenderTypes::entityTranslucent);
        ModelPart body = root.getChild("golem");
        fez = body.getChild("fez");
        plate = body.getChild("plate");
        plateLeft = body.getChild("plate_left");
        plateRight = body.getChild("plate_right");
        hat = body.getChild("hat");
        glasses = body.getChild("glasses");
        visor = body.getChild("visor");
        hatRim = body.getChild("hat_rim");
        dartgun = body.getChild("dartgun");
        mace = body.getChild("mace");
        bowtie = body.getChild("bowtie");
        headJar = body.getChild("head_jar");
        headBrain = body.getChild("head_brain");
        evilHead = body.getChild("evil_head");
    }

    @Override
    public void setupAnim(GolemRenderState state) {
        super.setupAnim(state);
        float yaw = state.core < 0 || state.inactive || state.bootup > 0 ? 0.0F : state.yRot * ((float) Math.PI / 180.0F);
        float pitch = state.core < 0 || state.inactive ? 0.57595867F : state.bootup > 0 ? state.bootup / (180.0F / (float) Math.PI) : state.xRot * ((float) Math.PI / 180.0F);
        for (ModelPart part : new ModelPart[] {fez, glasses, visor, hat, hatRim, headJar, headBrain, evilHead}) {
            part.yRot = yaw;
            part.xRot = pitch;
        }
        dartgun.xRot = GolemModel.armAngle(state, true, dartgun.xRot);
        mace.xRot = GolemModel.armAngle(state, false, mace.xRot);
        String decoration = state.decoration;
        fez.visible = decoration.contains("F");
        hat.visible = decoration.contains("H");
        hatRim.visible = hat.visible;
        bowtie.visible = decoration.contains("B");
        plate.visible = decoration.contains("P");
        plateLeft.visible = plate.visible;
        plateRight.visible = plate.visible;
        glasses.visible = decoration.contains("G");
        visor.visible = decoration.contains("V");
        dartgun.visible = decoration.contains("R");
        mace.visible = decoration.contains("M");
        headJar.visible = state.advanced;
        headBrain.visible = state.advanced;
        evilHead.visible = state.advanced && state.core >= 0;
    }
}
