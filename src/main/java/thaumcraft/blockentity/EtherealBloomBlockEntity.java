package thaumcraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.registry.ModBlockEntities;
import thaumcraft.taint.TaintBiomes;

public class EtherealBloomBlockEntity extends TcBlockEntity {
    private int counter;

    public EtherealBloomBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ETHEREAL_BLOOM.get(), pos, state);
    }

    public static void serverTick(ServerLevel level, BlockPos pos, EtherealBloomBlockEntity bloom) {
        if (++bloom.counter % 20 != 0) {
            return;
        }
        int x = level.getRandom().nextInt(8) - level.getRandom().nextInt(8);
        int z = level.getRandom().nextInt(8) - level.getRandom().nextInt(8);
        BlockPos target = pos.offset(x, 0, z);
        if (pos.distToCenterSqr(target.getX() + 0.5, pos.getY(), target.getZ() + 0.5) <= 81.0) {
            TaintBiomes.purify(level, target);
        }
    }
}
