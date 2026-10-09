package thaumcraft.infusion;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.blockentity.TcBlockEntity;
import thaumcraft.registry.ModBlockEntities;

public class InfusionPillarBlockEntity extends TcBlockEntity {
    public InfusionPillarBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.INFUSION_PILLAR.get(), pos, state);
    }

    protected InfusionPillarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
