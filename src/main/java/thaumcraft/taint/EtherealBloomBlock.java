package thaumcraft.taint;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import thaumcraft.block.CustomPlantBlock;
import thaumcraft.blockentity.EtherealBloomBlockEntity;
import thaumcraft.registry.ModBlockEntities;

public class EtherealBloomBlock extends CustomPlantBlock implements EntityBlock {
    public EtherealBloomBlock(Properties properties) {
        super(Kind.ETHEREAL_BLOOM, properties.randomTicks());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EtherealBloomBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() || type != ModBlockEntities.ETHEREAL_BLOOM.get()
            ? null
            : (tickLevel, pos, tickState, entity) -> EtherealBloomBlockEntity.serverTick((ServerLevel) tickLevel, pos, (EtherealBloomBlockEntity) entity);
    }
}
