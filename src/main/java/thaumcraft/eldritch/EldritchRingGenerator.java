package thaumcraft.eldritch;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.registry.EldritchContent;
import thaumcraft.registry.ModBlocks;
import thaumcraft.world.dim.MazeHandler;

public final class EldritchRingGenerator {
    private EldritchRingGenerator() {
    }

    public static boolean generate(WorldGenLevel level, Random random, int chunkX, int chunkZ, int x, int y, int z) {
        int width = 11 + random.nextInt(6) * 2;
        int height = 11 + random.nextInt(6) * 2;
        if (!validSpawn(level, x - 3, y, z - 3)
            || !validSpawn(level, x, y, z)
            || !validSpawn(level, x + 3, y, z)
            || !validSpawn(level, x + 3, y, z + 3)
            || !validSpawn(level, x, y, z + 3)) {
            return false;
        }

        for (int blockX = x - 3; blockX <= x + 3; blockX++) {
            for (int blockZ = z - 3; blockZ <= z + 3; blockZ++) {
                if ((blockX == x - 3 || blockX == x + 3) && (blockZ == z - 3 || blockZ == z + 3)) {
                    continue;
                }
                for (int offsetY = -4; offsetY < 5; offsetY++) {
                    BlockPos pos = new BlockPos(blockX, y + offsetY, blockZ);
                    BlockState previous = level.getBlockState(pos);
                    if (offsetY <= 0 || previous.canBeReplaced() || previous.getCollisionShape(level, pos, net.minecraft.world.phys.shapes.CollisionContext.empty()).isEmpty() || previous.is(BlockTags.LEAVES)) {
                        level.setBlock(pos, random.nextInt(4) == 0 ? Blocks.OBSIDIAN.defaultBlockState() : ModBlocks.OBSIDIAN_TILE.get().defaultBlockState(), 3);
                    }
                    if (offsetY > 0) {
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
                if (blockX == x && blockZ == z) {
                    placeAltar(level, random, new BlockPos(x, y + 1, z));
                    level.setBlock(new BlockPos(x, y, z), ModBlocks.OBSIDIAN_TILE.get().defaultBlockState(), 3);
                    int roll = random.nextInt(10);
                    EldritchBlockEntity altar = (EldritchBlockEntity) level.getBlockEntity(new BlockPos(x, y + 1, z));
                    if (altar != null && roll >= 1 && roll <= 4) {
                        altar.setSpawner(true);
                        altar.setSpawnType((byte) 0);
                        placeCultistBanners(level, x, y + 1, z);
                    } else if (altar != null && (roll == 6 || roll == 7)) {
                        altar.setSpawner(true);
                        altar.setSpawnType((byte) 1);
                    }
                    placeObelisk(level, new BlockPos(x, y + 3, z));
                } else if (((blockX == x - 3 || blockX == x + 3) && Math.abs((blockZ - z) % 2) == 1
                    || (blockZ == z - 3 || blockZ == z + 3) && Math.abs((blockX - x) % 2) == 1)
                    && Math.abs(blockX - x) != Math.abs(blockZ - z)) {
                    level.setBlock(new BlockPos(blockX, y, blockZ), ModBlocks.OBSIDIAN_TILE.get().defaultBlockState(), 3);
                    level.setBlock(new BlockPos(blockX, y + 1, blockZ), EldritchContent.ELDRITCH_CAPSTONE.get().defaultBlockState(), 3);
                }
            }
        }
        MazeHandler.requestMaze(level.getLevel().getServer(), level.getLevel().getSeed(), chunkX, chunkZ, width, height, random.nextLong());
        return true;
    }

    private static void placeCultistBanners(WorldGenLevel level, int x, int y, int z) {
        int[][] banners = {{x, z + 3, 8}, {x, z - 3, 0}, {x + 3, z, 12}, {x - 3, z, 4}};
        for (int[] banner : banners) {
            BlockPos pos = new BlockPos(banner[0], y, banner[1]);
            level.setBlock(pos, thaumcraft.registry.ModBlocks.BANNER.get().defaultBlockState(), 3);
            if (level.getBlockEntity(pos) instanceof thaumcraft.device.BannerBlockEntity blockEntity) {
                blockEntity.setFacing(banner[2]);
            }
        }
    }

    private static void placeAltar(WorldGenLevel level, Random random, BlockPos pos) {
        level.setBlock(pos, EldritchContent.ELDRITCH_ALTAR.get().defaultBlockState(), 3);

    }

    private static void placeObelisk(WorldGenLevel level, BlockPos pos) {
        level.setBlock(pos, EldritchContent.ELDRITCH_OBELISK.get().defaultBlockState().setValue(EldritchBlock.PART, net.minecraft.world.level.block.state.properties.DoubleBlockHalf.LOWER), 3);
        for (int y = 1; y <= 4; y++) {
            level.setBlock(pos.above(y), EldritchContent.ELDRITCH_OBELISK.get().defaultBlockState().setValue(EldritchBlock.PART, net.minecraft.world.level.block.state.properties.DoubleBlockHalf.UPPER), 3);
        }
    }

    private static boolean validSpawn(WorldGenLevel level, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        int distanceToAir = 0;
        while (!level.getBlockState(pos.above(distanceToAir)).isAir()) {
            if (++distanceToAir > 2) {
                return false;
            }
        }
        BlockPos top = pos.above(distanceToAir - 1);
        if (!level.isEmptyBlock(top.above())) {
            return false;
        }
        BlockState ground = level.getBlockState(top);
        if (isValidGround(ground)) {
            return true;
        }
        return (ground.is(Blocks.SNOW) || ground.is(Blocks.TALL_GRASS)) && isValidGround(level.getBlockState(top.below()));
    }

    private static boolean isValidGround(BlockState state) {
        return state.is(Blocks.STONE) || state.is(Blocks.SAND) || state.is(Blocks.PACKED_ICE) || state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.GRAVEL) || state.is(Blocks.DIRT);
    }
}
