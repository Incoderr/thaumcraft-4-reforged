package thaumcraft.registry;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import thaumcraft.Thaumcraft;
import thaumcraft.entity.monster.FireBat;
import thaumcraft.entity.projectile.Ember;
import thaumcraft.entity.projectile.ExplosiveOrb;
import thaumcraft.entity.projectile.FrostShard;
import thaumcraft.entity.projectile.PechBlast;
import thaumcraft.entity.projectile.PrimalArrow;
import thaumcraft.entity.projectile.PrimalOrb;
import thaumcraft.entity.projectile.ShockOrb;
import thaumcraft.entity.projectile.BottleTaint;
import thaumcraft.entity.monster.taint.FallingTaint;
import thaumcraft.entity.monster.taint.TaintSpider;
import thaumcraft.entity.monster.taint.Taintacle;
import thaumcraft.entity.monster.taint.TaintacleTiny;
import thaumcraft.entity.monster.taint.TaintSpore;
import thaumcraft.entity.monster.taint.TaintSwarmer;
import thaumcraft.entity.monster.taint.TaintSwarm;
import thaumcraft.entity.monster.taint.TaintedChicken;
import thaumcraft.entity.monster.taint.TaintedCow;
import thaumcraft.entity.monster.taint.TaintedCreeper;
import thaumcraft.entity.monster.taint.TaintedPig;
import thaumcraft.entity.monster.taint.TaintedSheep;
import thaumcraft.entity.monster.taint.TaintedVillager;

public final class ModEntities {
    private static final DeferredRegister.Entities ENTITIES = DeferredRegister.createEntities(Thaumcraft.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<FireBat>> FIREBAT = ENTITIES.registerEntityType(
        "firebat",
        FireBat::new,
        MobCategory.MONSTER,
        builder -> builder.sized(0.5F, 0.9F).eyeHeight(0.45F).fireImmune().clientTrackingRange(4).updateInterval(3)
    );
    public static final DeferredHolder<EntityType<?>, EntityType<PrimalOrb>> PRIMAL_ORB = projectile("primal_orb", PrimalOrb::new, 0.25F);
    public static final DeferredHolder<EntityType<?>, EntityType<FrostShard>> FROST_SHARD = projectile("frost_shard", FrostShard::new, 0.25F);
    public static final DeferredHolder<EntityType<?>, EntityType<PrimalArrow>> PRIMAL_ARROW = projectile("primal_arrow", PrimalArrow::new, 0.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<PechBlast>> PECH_BLAST = projectile("pech_blast", PechBlast::new, 0.25F);
    public static final DeferredHolder<EntityType<?>, EntityType<ShockOrb>> SHOCK_ORB = projectile("shock_orb", ShockOrb::new, 0.25F);
    public static final DeferredHolder<EntityType<?>, EntityType<Ember>> EMBER = projectile("ember", Ember::new, 0.25F);
    public static final DeferredHolder<EntityType<?>, EntityType<ExplosiveOrb>> EXPLOSIVE_ORB = projectile("explosive_orb", ExplosiveOrb::new, 0.25F);
    public static final DeferredHolder<EntityType<?>, EntityType<BottleTaint>> BOTTLE_TAINT = projectile("bottle_taint", BottleTaint::new, 0.25F);
    public static final DeferredHolder<EntityType<?>, EntityType<FallingTaint>> FALLING_TAINT = projectile("falling_taint", FallingTaint::new, 0.98F);
    public static final DeferredHolder<EntityType<?>, EntityType<TaintSpider>> TAINT_SPIDER = mob("taint_spider", TaintSpider::new, 0.4F, 0.3F);
    public static final DeferredHolder<EntityType<?>, EntityType<Taintacle>> TAINTACLE = mob("taintacle", Taintacle::new, 0.66F, 3.0F);
    public static final DeferredHolder<EntityType<?>, EntityType<TaintacleTiny>> TAINTACLE_TINY = mob("taintacle_small", TaintacleTiny::new, 0.22F, 1.0F);
    public static final DeferredHolder<EntityType<?>, EntityType<TaintSpore>> TAINT_SPORE = mob("taint_spore", TaintSpore::new, 0.4F, 0.4F);
    public static final DeferredHolder<EntityType<?>, EntityType<TaintSwarmer>> TAINT_SPORE_SWARMER = mob("taint_spore_swarmer", TaintSwarmer::new, 1.0F, 1.0F);
    public static final DeferredHolder<EntityType<?>, EntityType<TaintSwarm>> TAINT_SWARM = mob("taint_swarm", TaintSwarm::new, 2.0F, 2.0F);
    public static final DeferredHolder<EntityType<?>, EntityType<TaintedChicken>> TAINTED_CHICKEN = mob("tainted_chicken", TaintedChicken::new, 0.5F, 0.8F);
    public static final DeferredHolder<EntityType<?>, EntityType<TaintedCow>> TAINTED_COW = mob("tainted_cow", TaintedCow::new, 0.9F, 1.3F);
    public static final DeferredHolder<EntityType<?>, EntityType<TaintedCreeper>> TAINTED_CREEPER = mob("tainted_creeper", TaintedCreeper::new, 0.6F, 1.7F);
    public static final DeferredHolder<EntityType<?>, EntityType<TaintedPig>> TAINTED_PIG = mob("tainted_pig", TaintedPig::new, 0.9F, 0.9F);
    public static final DeferredHolder<EntityType<?>, EntityType<TaintedSheep>> TAINTED_SHEEP = mob("tainted_sheep", TaintedSheep::new, 0.9F, 1.3F);
    public static final DeferredHolder<EntityType<?>, EntityType<TaintedVillager>> TAINTED_VILLAGER = mob("tainted_villager", TaintedVillager::new, 0.6F, 1.95F);

    private ModEntities() {
    }

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> projectile(String name, EntityType.EntityFactory<T> factory, float size) {
        return ENTITIES.registerEntityType(name, factory, MobCategory.MISC, builder -> builder.sized(size, size).clientTrackingRange(4).updateInterval(20));
    }

    private static <T extends net.minecraft.world.entity.Mob> DeferredHolder<EntityType<?>, EntityType<T>> mob(String name, EntityType.EntityFactory<T> factory, float width, float height) {
        return ENTITIES.registerEntityType(name, factory, MobCategory.MONSTER, builder -> builder.sized(width, height).clientTrackingRange(8).updateInterval(3));
    }

    public static void register(IEventBus bus) {
        ENTITIES.register(bus);
        bus.addListener(ModEntities::createAttributes);
        bus.addListener(ModEntities::registerSpawnPlacements);
    }

    private static void createAttributes(EntityAttributeCreationEvent event) {
        event.put(FIREBAT.get(), FireBat.createAttributes().build());
        event.put(TAINT_SPIDER.get(), TaintSpider.createAttributes().build());
        event.put(TAINTACLE.get(), Taintacle.createAttributes().build());
        event.put(TAINTACLE_TINY.get(), TaintacleTiny.createAttributes().build());
        event.put(TAINT_SPORE.get(), TaintSpore.createAttributes().build());
        event.put(TAINT_SPORE_SWARMER.get(), TaintSwarmer.createAttributes().build());
        event.put(TAINT_SWARM.get(), TaintSwarm.createAttributes().build());
        event.put(TAINTED_CHICKEN.get(), TaintedChicken.createAttributes().build());
        event.put(TAINTED_COW.get(), TaintedCow.createAttributes().build());
        event.put(TAINTED_CREEPER.get(), TaintedCreeper.createAttributes().build());
        event.put(TAINTED_PIG.get(), TaintedPig.createAttributes().build());
        event.put(TAINTED_SHEEP.get(), TaintedSheep.createAttributes().build());
        event.put(TAINTED_VILLAGER.get(), TaintedVillager.createAttributes().build());
    }

    private static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(
            TAINTACLE.get(),
            SpawnPlacementType.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            Taintacle::canSpawnNaturally,
            RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
    }
}
