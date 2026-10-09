package thaumcraft.essentia;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.blockentity.TcBlockEntity;
import thaumcraft.registry.ModBlockEntities;

public class CentrifugeBlockEntity extends TcBlockEntity {
    public CentrifugeBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.CENTRIFUGE.get(), pos, state);
    }

    protected CentrifugeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
