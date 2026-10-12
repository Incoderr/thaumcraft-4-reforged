package thaumcraft.client.render.entity.taint;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import thaumcraft.entity.monster.taint.TaintSpore;
import thaumcraft.entity.monster.taint.Taintacle;
import thaumcraft.entity.monster.taint.TaintedChicken;
import thaumcraft.entity.monster.taint.TaintedCreeper;
import thaumcraft.entity.monster.taint.TaintedSheep;
import thaumcraft.Thaumcraft;
import thaumcraft.client.platform.TcRenderTypes;

public class TaintMobRenderer<T extends Mob> extends MobRenderer<T, TaintMobRenderState, TaintEntityModel> {
    private final TaintEntityModel.Kind kind;
    private final Identifier texture;

    public TaintMobRenderer(Context context, TaintEntityModel.Kind kind, float shadowRadius) {
        super(context, new TaintEntityModel(context.bakeLayer(kind.layer()), kind), shadowRadius);
        this.kind = kind;
        this.texture = kind.texture();
        if (kind == TaintEntityModel.Kind.SPIDER) {
            this.addLayer(new EyesLayer<>(this) {
                @Override
                public net.minecraft.client.renderer.rendertype.RenderType renderType() {
                    return TcRenderTypes.eyes(Thaumcraft.id("textures/model/taint_spider_eyes.png"));
                }
            });
        }
        if (kind == TaintEntityModel.Kind.SHEEP) {
            this.addLayer(new TaintSheepFurLayer(this, new TaintSheepFurModel(context.bakeLayer(TaintSheepFurModel.LAYER))));
        }
        if (kind == TaintEntityModel.Kind.SWARMER) {
            this.addLayer(new TaintSwarmerSporeLayer(this, new TaintSwarmerSporeModel(context.bakeLayer(TaintSwarmerSporeModel.LAYER))));
        }
    }

    public static <T extends Mob> EntityRendererProvider<T> provider(TaintEntityModel.Kind kind, float shadowRadius) {
        return context -> new TaintMobRenderer<>(context, kind, shadowRadius);
    }

    @Override
    public TaintMobRenderState createRenderState() {
        return new TaintMobRenderState();
    }

    @Override
    public void extractRenderState(T entity, TaintMobRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.partialTick = partialTicks;
        state.hurt = entity.hurtTime > 0;
        if (entity instanceof TaintedSheep sheep) {
            state.sheared = sheep.isSheared();
            state.grazeTicks = sheep.getGrazeTicks();
        }
        if (entity instanceof TaintedChicken chicken) {
            state.flap = Mth.lerp(partialTicks, chicken.oldFlap, chicken.flap);
            state.flapSpeed = Mth.lerp(partialTicks, chicken.oldFlapSpeed, chicken.flapSpeed);
        }
        if (entity instanceof TaintSpore spore) {
            state.sporeSize = spore.getSporeSize();
            state.displaySize = spore.displaySize + (spore.displaySize < spore.getSporeSize() ? 0.02F * partialTicks : 0.0F);
        }
        if (entity instanceof Taintacle tentacle) {
            state.flailIntensity = tentacle.flailIntensity;
            state.agitated = tentacle.isAgitatedForRender();
            state.tentacleAttacking = tentacle.isAttackingForRender();
        }
        if (entity instanceof TaintedCreeper creeper) {
            state.swelling = creeper.getSwelling(partialTicks);
            float pulse = 1.0F + Mth.sin(state.swelling * 100.0F) * state.swelling * 0.01F;
            float charge = Mth.clamp(state.swelling, 0.0F, 1.0F);
            charge *= charge;
            charge *= charge;
            state.scale = (1.0F + charge * 0.4F) * pulse;
            state.ageScale = (1.0F + charge * 0.1F) / pulse;
        }
    }

    @Override
    public Identifier getTextureLocation(TaintMobRenderState state) {
        return this.texture;
    }

    @Override
    protected void scale(TaintMobRenderState state, PoseStack poseStack) {
        if (this.kind == TaintEntityModel.Kind.TENTACLE || this.kind == TaintEntityModel.Kind.TENTACLE_TINY) {
            float scale = state.boundingBoxHeight / 3.0F;
            float riseTime = state.boundingBoxHeight * 10.0F;
            float rise = state.ageInTicks < riseTime
                ? (riseTime - state.ageInTicks) / riseTime * state.boundingBoxHeight
                : 0.0F;
            poseStack.translate(0.0F, (state.boundingBoxHeight == 3.0F ? 0.6F : 1.2F) + rise, 0.0F);
            poseStack.scale(scale, scale, scale);
        } else if (this.kind == TaintEntityModel.Kind.SPORE) {
            float size = state.displaySize;
            float pulse = 0.025F * Mth.sin(state.ageInTicks * 0.075F);
            poseStack.scale(-0.12F * size - pulse, -0.12F * size + pulse, -0.12F * size - pulse);
        } else if (this.kind == TaintEntityModel.Kind.CREEPER) {
            poseStack.scale(state.scale, state.ageScale, state.scale);
        } else if (this.kind == TaintEntityModel.Kind.SPIDER) {
            poseStack.scale(0.4F, 0.5F, 0.4F);
        } else if (this.kind == TaintEntityModel.Kind.VILLAGER) {
            poseStack.scale(0.9375F, 0.9375F, 0.9375F);
        }
    }

    @Override
    protected float getFlipDegrees() {
        if (this.kind == TaintEntityModel.Kind.SPORE || this.kind == TaintEntityModel.Kind.SWARMER) {
            return 0.0F;
        }
        return this.kind == TaintEntityModel.Kind.SPIDER ? 180.0F : super.getFlipDegrees();
    }

    @Override
    protected int getModelTint(TaintMobRenderState state) {
        if (this.kind != TaintEntityModel.Kind.CREEPER || (int) (state.swelling * 10.0F) % 2 == 0) {
            return -1;
        }
        int alpha = Mth.clamp((int) (Mth.clamp(state.swelling, 0.0F, 1.0F) * 0.2F * 255.0F), 0, 255);
        return alpha << 24 | 0x00FFFFFF;
    }

}
