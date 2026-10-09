package thaumcraft.essentia;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.blockentity.TcBlockEntity;
import thaumcraft.registry.ModBlockEntities;

public class EssentiaBufferBlockEntity extends TcBlockEntity {
    public EssentiaBufferBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.ESSENTIA_BUFFER.get(), pos, state);
    }

    protected EssentiaBufferBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
