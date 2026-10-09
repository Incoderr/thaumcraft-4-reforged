package thaumcraft.essentia;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.blockentity.TcBlockEntity;
import thaumcraft.registry.ModBlockEntities;

public class EssentiaCrystallizerBlockEntity extends TcBlockEntity {
    public EssentiaCrystallizerBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.ESSENTIA_CRYSTALLIZER.get(), pos, state);
    }

    protected EssentiaCrystallizerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
