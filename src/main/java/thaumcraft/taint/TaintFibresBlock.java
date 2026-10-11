package thaumcraft.taint;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import thaumcraft.effect.EffectHooks;
import thaumcraft.registry.ModBlocks;

public class TaintFibresBlock extends Block {
    public enum Kind {
        FIBRES,
        GRASS,
        PLANT,
        STALK,
        MATURE_STALK
    }

    private static final VoxelShape SHAPE = Block.box(6.4, 0.0, 6.4, 9.6, 9.6, 9.6);
    private final Kind kind;

    public TaintFibresBlock(Kind kind, Properties properties) {
        super(properties.randomTicks().noOcclusion());
        this.kind = kind;
    }

    public Kind getKind() {
        return kind;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (kind == Kind.FIBRES && TaintSpread.onlyAdjacentToTaint(level, pos) || !TaintBiomes.isTainted(level, pos)) {
            level.removeBlock(pos, false);
            return;
        }
        TaintSpread.tick(level, pos);
        if (kind == Kind.STALK && random.nextInt(10) == 0 && level.isEmptyBlock(pos.above())) {
            level.setBlockAndUpdate(pos, ModBlocks.MATURE_SPORE_STALK.get().defaultBlockState());
            level.addFreshEntity(thaumcraft.entity.monster.taint.TaintSpore.create(level, pos.above()));
        } else if (kind == Kind.MATURE_STALK && level.getEntitiesOfClass(
            thaumcraft.entity.monster.taint.TaintSpore.class,
            new net.minecraft.world.phys.AABB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1)
        ).isEmpty()) {
            level.setBlockAndUpdate(pos, ModBlocks.SPORE_STALK.get().defaultBlockState());
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        if (!level.isClientSide() && entity instanceof LivingEntity living && !(living instanceof thaumcraft.entity.TaintedMob) && level.getRandom().nextInt(living instanceof net.minecraft.world.entity.player.Player ? 1000 : 500) == 0) {
            EffectHooks.applyTaintPoison(living, living instanceof net.minecraft.world.entity.player.Player ? 80 : 160, 0);
        }
        super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 0;
    }
}
