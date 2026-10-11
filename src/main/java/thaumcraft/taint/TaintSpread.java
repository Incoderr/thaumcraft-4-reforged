package thaumcraft.taint;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import thaumcraft.registry.ModBlocks;

public final class TaintSpread {
    private TaintSpread() {
    }

    public static int adjacentTaint(Level level, BlockPos pos) {
        int count = 0;
        for (Direction direction : Direction.values()) {
            BlockState state = level.getBlockState(pos.relative(direction));
            if (isTaintBlock(state)) {
                count++;
            }
        }
        return count;
    }

    public static boolean spreadFibres(ServerLevel level, BlockPos pos) {
        if (!hasAdjacentSolid(level, pos) || onlyAdjacentToTaint(level, pos)) {
            return false;
        }
        BlockState current = level.getBlockState(pos);
        Block block = current.getBlock();
        if (!current.getFluidState().isEmpty()) {
            return false;
        }
        if (!(current.isAir() || current.canBeReplaced() || block instanceof VegetationBlock || block instanceof LeavesBlock)) {
            return false;
        }
        RandomSource random = level.getRandom();
        Block target = ModBlocks.FIBROUS_TAINT.get();
        if (random.nextInt(10) == 0 && level.isEmptyBlock(pos.above()) && level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP)) {
            if (random.nextInt(10) < 9) {
                target = ModBlocks.TAINTED_GRASS.get();
            } else if (random.nextInt(12) < 10) {
                target = ModBlocks.TAINTED_PLANT.get();
            } else {
                target = ModBlocks.SPORE_STALK.get();
            }
        }
        level.setBlock(pos, target.defaultBlockState(), Block.UPDATE_ALL);
        return true;
    }

    public static boolean taintFromFlux(ServerLevel level, BlockPos pos) {
        if (!level.hasChunkAt(pos)) {
            return false;
        }
        BlockPos surface = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos);
        boolean placed = false;
        if (level.isEmptyBlock(surface) && hasAdjacentSolid(level, surface)) {
            placed = level.setBlockAndUpdate(surface, ModBlocks.FIBROUS_TAINT.get().defaultBlockState());
        } else if (isSoil(level.getBlockState(surface)) && adjacentTaint(level, surface) >= 1) {
            placed = level.setBlockAndUpdate(surface, ModBlocks.TAINTED_SOIL.get().defaultBlockState());
        }
        boolean biomeChanged = TaintBiomes.set(level, surface, TaintBiomes.TAINTED_LAND);
        return placed || biomeChanged;
    }

    public static void tick(ServerLevel level, BlockPos pos) {
        int adjacent = adjacentTaint(level, pos);
        RandomSource random = level.getRandom();
        TaintBiomes.spread(level, pos, adjacent, 200);
        BlockPos target = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
        if (TaintBiomes.isTainted(level, target)) {
            BlockState blockState = level.getBlockState(target);
            if (!spreadFibres(level, target)) {
                int nearby = adjacentTaint(level, target);
                if (nearby >= 2 && isLogGourdOrCactus(blockState)) {
                    level.setBlockAndUpdate(target, ModBlocks.CRUSTED_TAINT.get().defaultBlockState());
                } else if (nearby >= 3 && !blockState.isAir() && isSoil(blockState)) {
                    level.setBlockAndUpdate(target, ModBlocks.TAINTED_SOIL.get().defaultBlockState());
                }
            }
        }
    }

    public static boolean isTaintBlock(BlockState state) {
        return state.is(ModBlocks.CRUSTED_TAINT.get()) || state.is(ModBlocks.TAINTED_SOIL.get()) || state.is(ModBlocks.FLESH_BLOCK.get())
            || state.is(ModBlocks.FIBROUS_TAINT.get()) || state.is(ModBlocks.TAINTED_GRASS.get()) || state.is(ModBlocks.TAINTED_PLANT.get())
            || state.is(ModBlocks.SPORE_STALK.get()) || state.is(ModBlocks.MATURE_SPORE_STALK.get());
    }

    private static boolean isLogGourdOrCactus(BlockState state) {
        return state.is(BlockTags.LOGS) || state.is(Blocks.PUMPKIN) || state.is(Blocks.MELON) || state.is(Blocks.CARVED_PUMPKIN)
            || state.is(Blocks.JACK_O_LANTERN) || state.is(Blocks.CACTUS);
    }

    private static boolean isSoil(BlockState state) {
        return state.is(BlockTags.SAND) || state.is(BlockTags.DIRT) || state.is(Blocks.CLAY) || state.is(Blocks.GRASS_BLOCK);
    }

    public static boolean onlyAdjacentToTaint(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos neighbor = pos.relative(direction);
            BlockState state = level.getBlockState(neighbor);
            if (!state.isAir() && !isTaintBlock(state)) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasAdjacentSolid(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (level.getBlockState(pos.relative(direction)).isFaceSturdy(level, pos.relative(direction), direction.getOpposite())) {
                return true;
            }
        }
        return false;
    }
}
