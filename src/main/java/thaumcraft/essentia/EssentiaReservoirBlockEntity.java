package thaumcraft.essentia;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.blockentity.TcBlockEntity;
import thaumcraft.registry.ModBlockEntities;

public class EssentiaReservoirBlockEntity extends TcBlockEntity {
    public EssentiaReservoirBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.ESSENTIA_RESERVOIR.get(), pos, state);
    }

    protected EssentiaReservoirBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
