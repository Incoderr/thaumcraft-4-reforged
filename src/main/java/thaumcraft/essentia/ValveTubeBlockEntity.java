package thaumcraft.essentia;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.registry.ModBlockEntities;

public class ValveTubeBlockEntity extends TubeBlockEntity {
    public ValveTubeBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.VALVE_TUBE.get(), pos, state);
    }

    protected ValveTubeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
