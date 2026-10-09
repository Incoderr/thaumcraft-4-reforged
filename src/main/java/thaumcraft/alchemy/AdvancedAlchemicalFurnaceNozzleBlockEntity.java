package thaumcraft.alchemy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.blockentity.TcBlockEntity;
import thaumcraft.registry.ModBlockEntities;

public class AdvancedAlchemicalFurnaceNozzleBlockEntity extends TcBlockEntity {
    public AdvancedAlchemicalFurnaceNozzleBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.ADVANCED_ALCHEMICAL_FURNACE_NOZZLE.get(), pos, state);
    }

    protected AdvancedAlchemicalFurnaceNozzleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
