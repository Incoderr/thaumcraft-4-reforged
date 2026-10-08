package thaumcraft.registry;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import thaumcraft.Thaumcraft;
import thaumcraft.blockentity.ManaPodBlockEntity;
import thaumcraft.blockentity.WardingStoneBlockEntity;

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

    private ModBlockEntities() {
    }

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
