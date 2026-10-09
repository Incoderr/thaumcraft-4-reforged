package thaumcraft.arcane;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import thaumcraft.blockentity.TcBlockEntity;
import thaumcraft.registry.ModBlockEntities;

public class DeconstructionTableBlockEntity extends TcBlockEntity {
    public DeconstructionTableBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.DECONSTRUCTION_TABLE.get(), pos, state);
    }

    protected DeconstructionTableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public @Nullable Container exposedItems(@Nullable Direction side) {
        return null;
    }
}
