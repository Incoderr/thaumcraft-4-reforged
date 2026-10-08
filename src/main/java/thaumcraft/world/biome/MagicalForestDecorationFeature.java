package thaumcraft.world.biome;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;

public record MagicalForestDecorationFeature() implements Feature {
    public static final MapCodec<MagicalForestDecorationFeature> CODEC = MapCodec.unit(MagicalForestDecorationFeature::new);

    @Override
    public MapCodec<MagicalForestDecorationFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        return false;
    }
}
