package thaumcraft.alchemy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import thaumcraft.blockentity.TcBlockEntity;
import thaumcraft.platform.Transfer;
import thaumcraft.registry.ModBlockEntities;

public class CrucibleBlockEntity extends TcBlockEntity {
    public CrucibleBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.CRUCIBLE.get(), pos, state);
    }

    protected CrucibleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public Transfer.@Nullable FluidTank exposedFluids(@Nullable Direction side) {
        return null;
    }
}
