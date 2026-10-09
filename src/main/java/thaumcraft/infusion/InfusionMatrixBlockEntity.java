package thaumcraft.infusion;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.blockentity.TcBlockEntity;
import thaumcraft.registry.ModBlockEntities;

public class InfusionMatrixBlockEntity extends TcBlockEntity {
    public InfusionMatrixBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.INFUSION_MATRIX.get(), pos, state);
    }

    protected InfusionMatrixBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void addSourceFx(int dx, int dy, int dz, int color) {
    }
}
