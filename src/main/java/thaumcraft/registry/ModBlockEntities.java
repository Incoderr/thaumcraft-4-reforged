package thaumcraft.registry;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import thaumcraft.Thaumcraft;
import thaumcraft.blockentity.ManaPodBlockEntity;
import thaumcraft.blockentity.ResearchTableBlockEntity;
import thaumcraft.blockentity.NodeJarBlockEntity;
import thaumcraft.blockentity.WandPedestalBlockEntity;
import thaumcraft.blockentity.WardingStoneBlockEntity;
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

    private ModBlockEntities() {
    }

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
