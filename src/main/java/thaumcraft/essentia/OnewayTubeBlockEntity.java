package thaumcraft.essentia;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.registry.ModBlockEntities;

public class OnewayTubeBlockEntity extends TubeBlockEntity {
    public OnewayTubeBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.ONEWAY_TUBE.get(), pos, state);
    }

    protected OnewayTubeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
