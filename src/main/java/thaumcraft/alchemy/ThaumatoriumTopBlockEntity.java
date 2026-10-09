package thaumcraft.alchemy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.blockentity.TcBlockEntity;
import thaumcraft.registry.ModBlockEntities;

public class ThaumatoriumTopBlockEntity extends TcBlockEntity {
    public ThaumatoriumTopBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.THAUMATORIUM_TOP.get(), pos, state);
    }

    protected ThaumatoriumTopBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
