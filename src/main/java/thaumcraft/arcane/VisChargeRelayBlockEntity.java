package thaumcraft.arcane;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.blockentity.TcBlockEntity;
import thaumcraft.registry.ModBlockEntities;

public class VisChargeRelayBlockEntity extends TcBlockEntity {
    public VisChargeRelayBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.VIS_CHARGE_RELAY.get(), pos, state);
    }

    protected VisChargeRelayBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
