package thaumcraft.essentia;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.registry.ModBlockEntities;

public class FilterTubeBlockEntity extends TubeBlockEntity {
    public FilterTubeBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.FILTER_TUBE.get(), pos, state);
    }

    protected FilterTubeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
