package thaumcraft.world.gen;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;

public record ThaumcraftWorldGenFeature() implements Feature {
    public static final MapCodec<ThaumcraftWorldGenFeature> CODEC = MapCodec.unit(ThaumcraftWorldGenFeature::new);

    @Override
    public MapCodec<ThaumcraftWorldGenFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        return false;
    }
}
