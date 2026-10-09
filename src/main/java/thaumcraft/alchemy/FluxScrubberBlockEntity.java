package thaumcraft.alchemy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.blockentity.TcBlockEntity;
import thaumcraft.registry.ModBlockEntities;

public class FluxScrubberBlockEntity extends TcBlockEntity {
    public FluxScrubberBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.FLUX_SCRUBBER.get(), pos, state);
    }

    protected FluxScrubberBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
