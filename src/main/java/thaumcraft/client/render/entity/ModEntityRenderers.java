package thaumcraft.client.render.entity;

import net.minecraft.client.renderer.entity.NoopRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import thaumcraft.Thaumcraft;
import thaumcraft.registry.ModEntities;
import thaumcraft.registry.ModBlockEntities;
import thaumcraft.client.render.entity.taint.TaintEntityModel;
import thaumcraft.client.render.entity.taint.TaintMobRenderer;
import thaumcraft.client.render.entity.taint.TaintSheepFurModel;
import thaumcraft.client.render.entity.taint.TaintSwarmerSporeModel;
import thaumcraft.client.render.taint.EtherealBloomRenderer;
import thaumcraft.client.render.entity.eldritch.EldritchGuardianModel;
import thaumcraft.client.render.entity.eldritch.EldritchGuardianRenderer;
import thaumcraft.client.render.entity.eldritch.EldritchOrbRenderer;
import thaumcraft.client.render.entity.eldritch.MindSpiderRenderer;
import thaumcraft.client.render.entity.eldritch.EldritchCrabModel;
import thaumcraft.client.render.entity.eldritch.EldritchCrabRenderer;

@EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class ModEntityRenderers {
    private ModEntityRenderers() {
    }

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.FIREBAT.get(), FireBatRenderer::new);
        event.registerEntityRenderer(ModEntities.BRAINY_ZOMBIE.get(), BrainyZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.GIANT_BRAINY_ZOMBIE.get(), GiantBrainyZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.INHABITED_ZOMBIE.get(), InhabitedZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.PECH.get(), PechRenderer::new);
        event.registerEntityRenderer(ModEntities.WISP.get(), WispRenderer::new);
        event.registerEntityRenderer(ModEntities.THAUMIC_SLIME.get(), ThaumicSlimeRenderer::new);
        event.registerEntityRenderer(ModEntities.EMBER.get(), EmberRenderer::new);
        event.registerEntityRenderer(ModEntities.SHOCK_ORB.get(), ShockOrbRenderer::new);
        event.registerEntityRenderer(ModEntities.GOLEM_ORB.get(), GolemOrbRenderer::new);
        event.registerEntityRenderer(ModEntities.EXPLOSIVE_ORB.get(), ExplosiveOrbRenderer::new);
        event.registerEntityRenderer(ModEntities.PRIMAL_ORB.get(), PrimalOrbRenderer::new);
        event.registerEntityRenderer(ModEntities.FROST_SHARD.get(), FrostShardRenderer::new);
        event.registerEntityRenderer(ModEntities.PECH_BLAST.get(), NoopRenderer::new);
        event.registerEntityRenderer(ModEntities.ALUMENTUM_PROJECTILE.get(), AlumentumRenderer::new);
        event.registerEntityRenderer(ModEntities.DART.get(), DartRenderer::new);
        event.registerEntityRenderer(ModEntities.PRIMAL_ARROW.get(), PrimalArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.MIND_SPIDER.get(), MindSpiderRenderer::new);
        event.registerEntityRenderer(ModEntities.ELDRITCH_GUARDIAN.get(), EldritchGuardianRenderer::new);
        event.registerEntityRenderer(ModEntities.ELDRITCH_CRAB.get(), EldritchCrabRenderer::new);
        event.registerEntityRenderer(ModEntities.CULTIST_KNIGHT.get(), CultistKnightRenderer::new);
        event.registerEntityRenderer(ModEntities.CULTIST_CLERIC.get(), CultistClericRenderer::new);
        event.registerEntityRenderer(ModEntities.ELDRITCH_ORB.get(), EldritchOrbRenderer::new);
        event.registerEntityRenderer(ModEntities.TAINT_SPIDER.get(), TaintMobRenderer.provider(TaintEntityModel.Kind.SPIDER, 0.3F));
        event.registerEntityRenderer(ModEntities.TAINTACLE.get(), TaintMobRenderer.provider(TaintEntityModel.Kind.TENTACLE, 0.5F));
        event.registerEntityRenderer(ModEntities.TAINTACLE_TINY.get(), TaintMobRenderer.provider(TaintEntityModel.Kind.TENTACLE_TINY, 0.2F));
        event.registerEntityRenderer(ModEntities.TAINT_SPORE.get(), TaintMobRenderer.provider(TaintEntityModel.Kind.SPORE, 0.3F));
        event.registerEntityRenderer(ModEntities.TAINT_SPORE_SWARMER.get(), TaintMobRenderer.provider(TaintEntityModel.Kind.SWARMER, 0.7F));
        event.registerEntityRenderer(ModEntities.TAINT_SWARM.get(), NoopRenderer::new);
        event.registerEntityRenderer(ModEntities.TAINTED_CHICKEN.get(), TaintMobRenderer.provider(TaintEntityModel.Kind.CHICKEN, 0.3F));
        event.registerEntityRenderer(ModEntities.TAINTED_COW.get(), TaintMobRenderer.provider(TaintEntityModel.Kind.COW, 0.7F));
        event.registerEntityRenderer(ModEntities.TAINTED_CREEPER.get(), TaintMobRenderer.provider(TaintEntityModel.Kind.CREEPER, 0.5F));
        event.registerEntityRenderer(ModEntities.TAINTED_PIG.get(), TaintMobRenderer.provider(TaintEntityModel.Kind.PIG, 0.7F));
        event.registerEntityRenderer(ModEntities.TAINTED_SHEEP.get(), TaintMobRenderer.provider(TaintEntityModel.Kind.SHEEP, 0.7F));
        event.registerEntityRenderer(ModEntities.TAINTED_VILLAGER.get(), TaintMobRenderer.provider(TaintEntityModel.Kind.VILLAGER, 0.5F));
        event.registerBlockEntityRenderer(ModBlockEntities.ETHEREAL_BLOOM.get(), EtherealBloomRenderer::new);
    }

    @SubscribeEvent
    static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(FireBatModel.LAYER, FireBatModel::createBodyLayer);
        event.registerLayerDefinition(PechModel.LAYER, PechModel::createBodyLayer);
        event.registerLayerDefinition(EldritchGuardianModel.LAYER, EldritchGuardianModel::createBodyLayer);
        event.registerLayerDefinition(EldritchCrabModel.LAYER, EldritchCrabModel::createBodyLayer);
        for (TaintEntityModel.Kind kind : TaintEntityModel.Kind.values()) {
            event.registerLayerDefinition(kind.layer(), () -> TaintEntityModel.createLayer(kind));
        }
        event.registerLayerDefinition(TaintSheepFurModel.LAYER, TaintSheepFurModel::createLayer);
        event.registerLayerDefinition(TaintSwarmerSporeModel.LAYER, TaintSwarmerSporeModel::createLayer);
    }

}
