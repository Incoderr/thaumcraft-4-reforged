package thaumcraft.registry;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import thaumcraft.Thaumcraft;
import thaumcraft.alchemy.AdvancedAlchemicalFurnaceBlockEntity;
import thaumcraft.alchemy.AdvancedAlchemicalFurnaceNozzleBlockEntity;
import thaumcraft.alchemy.AlchemicalFurnaceBlockEntity;
import thaumcraft.alchemy.AlembicBlockEntity;
import thaumcraft.alchemy.BellowsBlockEntity;
import thaumcraft.alchemy.CrucibleBlockEntity;
import thaumcraft.alchemy.FluxScrubberBlockEntity;
import thaumcraft.alchemy.MnemonicMatrixBlockEntity;
import thaumcraft.alchemy.ThaumatoriumBlockEntity;
import thaumcraft.alchemy.ThaumatoriumTopBlockEntity;
import thaumcraft.arcane.ArcaneWorkbenchBlockEntity;
import thaumcraft.arcane.DeconstructionTableBlockEntity;
import thaumcraft.arcane.VisChargeRelayBlockEntity;
import thaumcraft.arcane.FocalManipulatorBlockEntity;
import thaumcraft.blockentity.HoleBlockEntity;
import thaumcraft.blockentity.ManaPodBlockEntity;
import thaumcraft.blockentity.NodeJarBlockEntity;
import thaumcraft.blockentity.ResearchTableBlockEntity;
import thaumcraft.blockentity.WandPedestalBlockEntity;
import thaumcraft.blockentity.WardedBlockEntity;
import thaumcraft.blockentity.WardingStoneBlockEntity;
import thaumcraft.device.BannerBlockEntity;
import thaumcraft.device.BrainJarBlockEntity;
import thaumcraft.device.HungryChestBlockEntity;
import thaumcraft.device.OwnedBlockEntity;
import thaumcraft.device.ArcaneBoreBaseBlockEntity;
import thaumcraft.device.ArcaneBoreBlockEntity;
import thaumcraft.device.ArcaneSpaBlockEntity;
import thaumcraft.device.InfernalFurnaceBlockEntity;
import thaumcraft.device.InfernalFurnaceNozzleBlockEntity;
import thaumcraft.essentia.CentrifugeBlockEntity;
import thaumcraft.essentia.EssentiaBufferBlockEntity;
import thaumcraft.essentia.EssentiaCrystallizerBlockEntity;
import thaumcraft.essentia.EssentiaReservoirBlockEntity;
import thaumcraft.essentia.FilterTubeBlockEntity;
import thaumcraft.essentia.JarBlockEntity;
import thaumcraft.essentia.OnewayTubeBlockEntity;
import thaumcraft.essentia.RestrictTubeBlockEntity;
import thaumcraft.essentia.TubeBlockEntity;
import thaumcraft.essentia.ValveTubeBlockEntity;
import thaumcraft.essentia.VoidJarBlockEntity;
import thaumcraft.infusion.InfusionMatrixBlockEntity;
import thaumcraft.infusion.InfusionPillarBlockEntity;
import thaumcraft.infusion.PedestalBlockEntity;
import thaumcraft.node.EnergizedNodeBlockEntity;
import thaumcraft.node.NodeBlockEntity;
import thaumcraft.node.NodeStabilizerBlockEntity;
import thaumcraft.node.NodeTransducerBlockEntity;
import thaumcraft.vis.VisRelayBlockEntity;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Thaumcraft.MODID);

    public static final Supplier<BlockEntityType<ManaPodBlockEntity>> MANA_POD = BLOCK_ENTITIES.register(
        "mana_pod",
        () -> new BlockEntityType<>(ManaPodBlockEntity::new, ModBlocks.MANA_POD.get())
    );
    public static final Supplier<BlockEntityType<WardingStoneBlockEntity>> WARDING_STONE = BLOCK_ENTITIES.register(
        "warding_stone",
        () -> new BlockEntityType<>(WardingStoneBlockEntity::new, ModBlocks.WARDING_PAVING_STONE.get())
    );
    public static final Supplier<BlockEntityType<NodeBlockEntity>> NODE = BLOCK_ENTITIES.register(
        "node",
        () -> new BlockEntityType<>(NodeBlockEntity::new, ModBlocks.AURA_NODE.get(), ModBlocks.CHARGED_OBSIDIAN_TOTEM.get(), ModBlocks.SILVERWOOD_KNOT.get())
    );
    public static final Supplier<BlockEntityType<EnergizedNodeBlockEntity>> ENERGIZED_NODE = BLOCK_ENTITIES.register(
        "energized_node",
        () -> new BlockEntityType<>(EnergizedNodeBlockEntity::new, ModBlocks.ENERGIZED_AURA_NODE.get())
    );
    public static final Supplier<BlockEntityType<NodeStabilizerBlockEntity>> NODE_STABILIZER = BLOCK_ENTITIES.register(
        "node_stabilizer",
        () -> new BlockEntityType<>(NodeStabilizerBlockEntity::new, ModBlocks.NODE_STABILIZER.get(), ModBlocks.ADVANCED_NODE_STABILIZER.get())
    );
    public static final Supplier<BlockEntityType<NodeTransducerBlockEntity>> NODE_TRANSDUCER = BLOCK_ENTITIES.register(
        "node_transducer",
        () -> new BlockEntityType<>(NodeTransducerBlockEntity::new, ModBlocks.NODE_TRANSDUCER.get())
    );
    public static final Supplier<BlockEntityType<VisRelayBlockEntity>> VIS_RELAY = BLOCK_ENTITIES.register(
        "vis_relay",
        () -> new BlockEntityType<>(VisRelayBlockEntity::new, ModBlocks.VIS_RELAY.get())
    );
    public static final Supplier<BlockEntityType<WandPedestalBlockEntity>> WAND_PEDESTAL = BLOCK_ENTITIES.register(
        "wand_pedestal",
        () -> new BlockEntityType<>(WandPedestalBlockEntity::new, ModBlocks.WAND_RECHARGE_PEDESTAL.get())
    );
    public static final Supplier<BlockEntityType<NodeJarBlockEntity>> NODE_JAR = BLOCK_ENTITIES.register(
        "node_jar",
        () -> new BlockEntityType<>(NodeJarBlockEntity::new, ModBlocks.NODE_JAR.get())
    );
    public static final Supplier<BlockEntityType<ResearchTableBlockEntity>> RESEARCH_TABLE = BLOCK_ENTITIES.register(
        "research_table",
        () -> new BlockEntityType<>(ResearchTableBlockEntity::new, ModBlocks.RESEARCH_TABLE.get())
    );

    public static final Supplier<BlockEntityType<JarBlockEntity>> JAR = BLOCK_ENTITIES.register(
        "jar",
        () -> new BlockEntityType<>(JarBlockEntity::new, ModBlocks.WARDED_JAR.get())
    );
    public static final Supplier<BlockEntityType<VoidJarBlockEntity>> VOID_JAR = BLOCK_ENTITIES.register(
        "void_jar",
        () -> new BlockEntityType<>(VoidJarBlockEntity::new, ModBlocks.VOID_JAR.get())
    );
    public static final Supplier<BlockEntityType<TubeBlockEntity>> TUBE = BLOCK_ENTITIES.register(
        "essentia_tube",
        () -> new BlockEntityType<>(TubeBlockEntity::new, ModBlocks.ESSENTIA_TUBE.get())
    );
    public static final Supplier<BlockEntityType<ValveTubeBlockEntity>> VALVE_TUBE = BLOCK_ENTITIES.register(
        "essentia_valve",
        () -> new BlockEntityType<>(ValveTubeBlockEntity::new, ModBlocks.ESSENTIA_VALVE.get())
    );
    public static final Supplier<BlockEntityType<FilterTubeBlockEntity>> FILTER_TUBE = BLOCK_ENTITIES.register(
        "filtered_essentia_tube",
        () -> new BlockEntityType<>(FilterTubeBlockEntity::new, ModBlocks.FILTERED_ESSENTIA_TUBE.get())
    );
    public static final Supplier<BlockEntityType<RestrictTubeBlockEntity>> RESTRICT_TUBE = BLOCK_ENTITIES.register(
        "restricted_essentia_tube",
        () -> new BlockEntityType<>(RestrictTubeBlockEntity::new, ModBlocks.RESTRICTED_ESSENTIA_TUBE.get())
    );
    public static final Supplier<BlockEntityType<OnewayTubeBlockEntity>> ONEWAY_TUBE = BLOCK_ENTITIES.register(
        "directional_essentia_tube",
        () -> new BlockEntityType<>(OnewayTubeBlockEntity::new, ModBlocks.DIRECTIONAL_ESSENTIA_TUBE.get())
    );
    public static final Supplier<BlockEntityType<EssentiaBufferBlockEntity>> ESSENTIA_BUFFER = BLOCK_ENTITIES.register(
        "essentia_buffer",
        () -> new BlockEntityType<>(EssentiaBufferBlockEntity::new, ModBlocks.ESSENTIA_BUFFER.get())
    );
    public static final Supplier<BlockEntityType<CentrifugeBlockEntity>> CENTRIFUGE = BLOCK_ENTITIES.register(
        "alchemical_centrifuge",
        () -> new BlockEntityType<>(CentrifugeBlockEntity::new, ModBlocks.ALCHEMICAL_CENTRIFUGE.get())
    );
    public static final Supplier<BlockEntityType<EssentiaCrystallizerBlockEntity>> ESSENTIA_CRYSTALLIZER = BLOCK_ENTITIES.register(
        "essentia_crystallizer",
        () -> new BlockEntityType<>(EssentiaCrystallizerBlockEntity::new, ModBlocks.ESSENTIA_CRYSTALLIZER.get())
    );
    public static final Supplier<BlockEntityType<EssentiaReservoirBlockEntity>> ESSENTIA_RESERVOIR = BLOCK_ENTITIES.register(
        "essentia_reservoir",
        () -> new BlockEntityType<>(EssentiaReservoirBlockEntity::new, ModBlocks.ESSENTIA_RESERVOIR.get())
    );
    public static final Supplier<BlockEntityType<CrucibleBlockEntity>> CRUCIBLE = BLOCK_ENTITIES.register(
        "crucible",
        () -> new BlockEntityType<>(CrucibleBlockEntity::new, ModBlocks.CRUCIBLE.get())
    );
    public static final Supplier<BlockEntityType<AlembicBlockEntity>> ALEMBIC = BLOCK_ENTITIES.register(
        "alembic",
        () -> new BlockEntityType<>(AlembicBlockEntity::new, ModBlocks.ALEMBIC.get())
    );
    public static final Supplier<BlockEntityType<AlchemicalFurnaceBlockEntity>> ALCHEMICAL_FURNACE = BLOCK_ENTITIES.register(
        "alchemical_furnace",
        () -> new BlockEntityType<>(AlchemicalFurnaceBlockEntity::new, ModBlocks.ALCHEMICAL_FURNACE.get())
    );
    public static final Supplier<BlockEntityType<AdvancedAlchemicalFurnaceBlockEntity>> ADVANCED_ALCHEMICAL_FURNACE = BLOCK_ENTITIES.register(
        "advanced_alchemical_furnace",
        () -> new BlockEntityType<>(AdvancedAlchemicalFurnaceBlockEntity::new, ModBlocks.ADVANCED_ALCHEMICAL_FURNACE.get())
    );
    public static final Supplier<BlockEntityType<AdvancedAlchemicalFurnaceNozzleBlockEntity>> ADVANCED_ALCHEMICAL_FURNACE_NOZZLE = BLOCK_ENTITIES.register(
        "advanced_alchemical_furnace_nozzle",
        () -> new BlockEntityType<>(AdvancedAlchemicalFurnaceNozzleBlockEntity::new, ModBlocks.ADVANCED_ALCHEMICAL_FURNACE.get())
    );
    public static final Supplier<BlockEntityType<ThaumatoriumBlockEntity>> THAUMATORIUM = BLOCK_ENTITIES.register(
        "thaumatorium",
        () -> new BlockEntityType<>(ThaumatoriumBlockEntity::new, ModBlocks.THAUMATORIUM.get())
    );
    public static final Supplier<BlockEntityType<ThaumatoriumTopBlockEntity>> THAUMATORIUM_TOP = BLOCK_ENTITIES.register(
        "thaumatorium_top",
        () -> new BlockEntityType<>(ThaumatoriumTopBlockEntity::new, ModBlocks.THAUMATORIUM_TOP.get())
    );
    public static final Supplier<BlockEntityType<MnemonicMatrixBlockEntity>> MNEMONIC_MATRIX = BLOCK_ENTITIES.register(
        "mnemonic_matrix",
        () -> new BlockEntityType<>(MnemonicMatrixBlockEntity::new, ModBlocks.MNEMONIC_MATRIX.get())
    );
    public static final Supplier<BlockEntityType<FluxScrubberBlockEntity>> FLUX_SCRUBBER = BLOCK_ENTITIES.register(
        "flux_scrubber",
        () -> new BlockEntityType<>(FluxScrubberBlockEntity::new, ModBlocks.FLUX_SCRUBBER.get())
    );
    public static final Supplier<BlockEntityType<FocalManipulatorBlockEntity>> FOCAL_MANIPULATOR = BLOCK_ENTITIES.register(
        "focal_manipulator",
        () -> new BlockEntityType<>(FocalManipulatorBlockEntity::new, ModBlocks.FOCAL_MANIPULATOR.get())
    );
    public static final Supplier<BlockEntityType<HoleBlockEntity>> HOLE = BLOCK_ENTITIES.register(
        "hole",
        () -> new BlockEntityType<>(HoleBlockEntity::new, ModBlocks.HOLE.get())
    );
    public static final Supplier<BlockEntityType<WardedBlockEntity>> WARDED = BLOCK_ENTITIES.register(
        "warded_block",
        () -> new BlockEntityType<>(WardedBlockEntity::new, ModBlocks.WARDED_BLOCK.get())
    );
    public static final Supplier<BlockEntityType<BellowsBlockEntity>> BELLOWS = BLOCK_ENTITIES.register(
        "arcane_bellows",
        () -> new BlockEntityType<>(BellowsBlockEntity::new, ModBlocks.ARCANE_BELLOWS.get())
    );
    public static final Supplier<BlockEntityType<ArcaneWorkbenchBlockEntity>> ARCANE_WORKBENCH = BLOCK_ENTITIES.register(
        "arcane_workbench",
        () -> new BlockEntityType<>(ArcaneWorkbenchBlockEntity::new, ModBlocks.ARCANE_WORKTABLE.get())
    );
    public static final Supplier<BlockEntityType<VisChargeRelayBlockEntity>> VIS_CHARGE_RELAY = BLOCK_ENTITIES.register(
        "vis_charge_relay",
        () -> new BlockEntityType<>(VisChargeRelayBlockEntity::new, ModBlocks.VIS_CHARGE_RELAY.get())
    );
    public static final Supplier<BlockEntityType<DeconstructionTableBlockEntity>> DECONSTRUCTION_TABLE = BLOCK_ENTITIES.register(
        "deconstruction_table",
        () -> new BlockEntityType<>(DeconstructionTableBlockEntity::new, ModBlocks.DECONSTRUCTION_TABLE.get())
    );
    public static final Supplier<BlockEntityType<InfusionMatrixBlockEntity>> INFUSION_MATRIX = BLOCK_ENTITIES.register(
        "infusion_matrix",
        () -> new BlockEntityType<>(InfusionMatrixBlockEntity::new, ModBlocks.RUNIC_MATRIX.get())
    );
    public static final Supplier<BlockEntityType<PedestalBlockEntity>> PEDESTAL = BLOCK_ENTITIES.register(
        "arcane_pedestal",
        () -> new BlockEntityType<>(PedestalBlockEntity::new, ModBlocks.ARCANE_PEDESTAL.get())
    );
    public static final Supplier<BlockEntityType<InfusionPillarBlockEntity>> INFUSION_PILLAR = BLOCK_ENTITIES.register(
        "infusion_pillar",
        () -> new BlockEntityType<>(InfusionPillarBlockEntity::new, ModBlocks.INFUSION_PILLAR.get())
    );

    public static final Supplier<BlockEntityType<thaumcraft.device.NitorLightBlockEntity>> NITOR_LIGHT = BLOCK_ENTITIES.register(
        "nitor_light",
        () -> new BlockEntityType<>(thaumcraft.device.NitorLightBlockEntity::new, ModBlocks.NITOR_LIGHT.get())
    );
    public static final Supplier<BlockEntityType<thaumcraft.device.WardingAuraBlockEntity>> WARDING_AURA = BLOCK_ENTITIES.register(
        "warding_aura",
        () -> new BlockEntityType<>(thaumcraft.device.WardingAuraBlockEntity::new, ModBlocks.WARDING_AURA.get())
    );
    public static final Supplier<BlockEntityType<thaumcraft.device.ArcaneLampBlockEntity>> ARCANE_LAMP = BLOCK_ENTITIES.register(
        "arcane_lamp",
        () -> new BlockEntityType<>(thaumcraft.device.ArcaneLampBlockEntity::new, ModBlocks.ARCANE_LAMP.get())
    );
    public static final Supplier<BlockEntityType<thaumcraft.device.LampOfGrowthBlockEntity>> LAMP_OF_GROWTH = BLOCK_ENTITIES.register(
        "lamp_of_growth",
        () -> new BlockEntityType<>(thaumcraft.device.LampOfGrowthBlockEntity::new, ModBlocks.LAMP_OF_GROWTH.get())
    );
    public static final Supplier<BlockEntityType<thaumcraft.device.LampOfFertilityBlockEntity>> LAMP_OF_FERTILITY = BLOCK_ENTITIES.register(
        "lamp_of_fertility",
        () -> new BlockEntityType<>(thaumcraft.device.LampOfFertilityBlockEntity::new, ModBlocks.LAMP_OF_FERTILITY.get())
    );
    public static final Supplier<BlockEntityType<thaumcraft.device.ItemGrateBlockEntity>> ITEM_GRATE = BLOCK_ENTITIES.register(
        "item_grate",
        () -> new BlockEntityType<>(thaumcraft.device.ItemGrateBlockEntity::new, ModBlocks.ITEM_GRATE.get())
    );
    public static final Supplier<BlockEntityType<thaumcraft.device.ArcaneEarBlockEntity>> ARCANE_EAR = BLOCK_ENTITIES.register(
        "arcane_ear",
        () -> new BlockEntityType<>(thaumcraft.device.ArcaneEarBlockEntity::new, ModBlocks.ARCANE_EAR.get())
    );
    public static final Supplier<BlockEntityType<thaumcraft.device.ArcaneLevitatorBlockEntity>> ARCANE_LEVITATOR = BLOCK_ENTITIES.register(
        "arcane_levitator",
        () -> new BlockEntityType<>(thaumcraft.device.ArcaneLevitatorBlockEntity::new, ModBlocks.ARCANE_LEVITATOR.get())
    );
    public static final Supplier<BlockEntityType<thaumcraft.device.CrystalClusterBlockEntity>> CRYSTAL_CLUSTER = BLOCK_ENTITIES.register(
        "crystal_cluster",
        () -> new BlockEntityType<>(thaumcraft.device.CrystalClusterBlockEntity::new, ModBlocks.AIR_CRYSTAL_CLUSTER.get(), ModBlocks.FIRE_CRYSTAL_CLUSTER.get(), ModBlocks.WATER_CRYSTAL_CLUSTER.get(), ModBlocks.EARTH_CRYSTAL_CLUSTER.get(), ModBlocks.ORDER_CRYSTAL_CLUSTER.get(), ModBlocks.ENTROPY_CRYSTAL_CLUSTER.get(), ModBlocks.MIXED_CRYSTAL_CLUSTER.get(), ModBlocks.STRANGE_CRYSTALS.get())
    );
    public static final Supplier<BlockEntityType<OwnedBlockEntity>> OWNED = BLOCK_ENTITIES.register(
        "owned",
        () -> new BlockEntityType<>(OwnedBlockEntity::new, ModBlocks.WARDED_GLASS.get(), ModBlocks.ARCANE_DOOR.get(), ModBlocks.ARCANE_PRESSURE_PLATE.get())
    );
    public static final Supplier<BlockEntityType<HungryChestBlockEntity>> HUNGRY_CHEST = BLOCK_ENTITIES.register(
        "hungry_chest",
        () -> new BlockEntityType<>(HungryChestBlockEntity::new, ModBlocks.HUNGRY_CHEST.get())
    );
    public static final Supplier<BlockEntityType<BrainJarBlockEntity>> BRAIN_JAR = BLOCK_ENTITIES.register(
        "brain_jar",
        () -> new BlockEntityType<>(BrainJarBlockEntity::new, ModBlocks.BRAIN_JAR.get())
    );
    public static final Supplier<BlockEntityType<BannerBlockEntity>> BANNER = BLOCK_ENTITIES.register(
        "banner",
        () -> new BlockEntityType<>(BannerBlockEntity::new, ModBlocks.BANNER.get())
    );
    public static final Supplier<BlockEntityType<InfernalFurnaceBlockEntity>> INFERNAL_FURNACE = BLOCK_ENTITIES.register(
        "infernal_furnace",
        () -> new BlockEntityType<>(InfernalFurnaceBlockEntity::new, ModBlocks.INFERNAL_FURNACE.get())
    );
    public static final Supplier<BlockEntityType<InfernalFurnaceNozzleBlockEntity>> INFERNAL_FURNACE_NOZZLE = BLOCK_ENTITIES.register(
        "infernal_furnace_nozzle",
        () -> new BlockEntityType<>(InfernalFurnaceNozzleBlockEntity::new, ModBlocks.INFERNAL_FURNACE.get())
    );
    public static final Supplier<BlockEntityType<ArcaneBoreBaseBlockEntity>> ARCANE_BORE_BASE = BLOCK_ENTITIES.register(
        "arcane_bore_base",
        () -> new BlockEntityType<>(ArcaneBoreBaseBlockEntity::new, ModBlocks.ARCANE_BORE_BASE.get())
    );
    public static final Supplier<BlockEntityType<ArcaneBoreBlockEntity>> ARCANE_BORE = BLOCK_ENTITIES.register(
        "arcane_bore",
        () -> new BlockEntityType<>(ArcaneBoreBlockEntity::new, ModBlocks.ARCANE_BORE.get())
    );
    public static final Supplier<BlockEntityType<ArcaneSpaBlockEntity>> ARCANE_SPA = BLOCK_ENTITIES.register(
        "arcane_spa",
        () -> new BlockEntityType<>(ArcaneSpaBlockEntity::new, ModBlocks.ARCANE_SPA.get())
    );

    private ModBlockEntities() {
    }

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
