package thaumcraft.client.render.device;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DynamicBlockStateModel;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import org.jspecify.annotations.Nullable;
import thaumcraft.client.render.ArchitectOverlay;
import thaumcraft.registry.ModBlocks;

public class WardedGlassModel implements DynamicBlockStateModel {
    private final List<BlockStateModel> models;
    private final Material.Baked particle;
    @BakedQuad.MaterialFlags
    private final int flags;

    public WardedGlassModel(List<BlockStateModel> models) {
        this.models = models;
        this.particle = models.get(0).particleMaterial();
        int combined = 0;
        for (BlockStateModel model : models) {
            combined |= model.materialFlags();
        }
        this.flags = combined;
    }

    private static boolean glass(BlockAndTintGetter level, BlockPos pos, int x, int y, int z) {
        return level.getBlockState(pos.offset(x, y, z)).is(ModBlocks.WARDED_GLASS.get());
    }

    static int spriteIndex(BlockAndTintGetter level, BlockPos pos, Direction direction) {
        int side = direction.get3DDataValue();
        boolean[] bits = new boolean[8];
        if (side == 0 || side == 1) {
            bits[0] = glass(level, pos, -1, 0, -1);
            bits[1] = glass(level, pos, 0, 0, -1);
            bits[2] = glass(level, pos, 1, 0, -1);
            bits[3] = glass(level, pos, -1, 0, 0);
            bits[4] = glass(level, pos, 1, 0, 0);
            bits[5] = glass(level, pos, -1, 0, 1);
            bits[6] = glass(level, pos, 0, 0, 1);
            bits[7] = glass(level, pos, 1, 0, 1);
        } else if (side == 2 || side == 3) {
            int before = side == 2 ? 1 : -1;
            int after = side == 3 ? 1 : -1;
            bits[0] = glass(level, pos, before, 1, 0);
            bits[1] = glass(level, pos, 0, 1, 0);
            bits[2] = glass(level, pos, after, 1, 0);
            bits[3] = glass(level, pos, before, 0, 0);
            bits[4] = glass(level, pos, after, 0, 0);
            bits[5] = glass(level, pos, before, -1, 0);
            bits[6] = glass(level, pos, 0, -1, 0);
            bits[7] = glass(level, pos, after, -1, 0);
        } else {
            int before = side == 5 ? 1 : -1;
            int after = side == 4 ? 1 : -1;
            bits[0] = glass(level, pos, 0, 1, before);
            bits[1] = glass(level, pos, 0, 1, 0);
            bits[2] = glass(level, pos, 0, 1, after);
            bits[3] = glass(level, pos, 0, 0, before);
            bits[4] = glass(level, pos, 0, 0, after);
            bits[5] = glass(level, pos, 0, -1, before);
            bits[6] = glass(level, pos, 0, -1, 0);
            bits[7] = glass(level, pos, 0, -1, after);
        }
        int id = 0;
        for (int index = 0; index < 8; index++) {
            if (bits[index]) {
                id |= 1 << index;
            }
        }
        return ArchitectOverlay.CONNECTED[id];
    }

    @Override
    public @Nullable Object createGeometryKey(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
        List<BakedQuad>[] faces = new List[6];
        long seed = random.nextLong();
        List<BlockStateModelPart> scratch = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            scratch.clear();
            random.setSeed(seed);
            models.get(spriteIndex(level, pos, direction)).collectParts(level, pos, state, random, scratch);
            List<BakedQuad> quads = new ArrayList<>();
            for (BlockStateModelPart part : scratch) {
                quads.addAll(part.getQuads(direction));
            }
            faces[direction.get3DDataValue()] = quads;
        }
        parts.add(new Part(faces, particle, flags));
    }

    @Override
    public Material.Baked particleMaterial() {
        return particle;
    }

    @Override
    public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        return particle;
    }

    @Override
    public int materialFlags() {
        return flags;
    }

    @Override
    public int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        return flags;
    }

    private record Part(List<BakedQuad>[] faces, Material.Baked particle, @BakedQuad.MaterialFlags int flags) implements BlockStateModelPart {
        @Override
        public List<BakedQuad> getQuads(@Nullable Direction direction) {
            return direction == null ? List.of() : faces[direction.get3DDataValue()];
        }

        @Override
        public boolean useAmbientOcclusion() {
            return true;
        }

        @Override
        public Material.Baked particleMaterial() {
            return particle;
        }

        @Override
        public @BakedQuad.MaterialFlags int materialFlags() {
            return flags;
        }
    }

    public record Unbaked(List<BlockStateModel.Unbaked> models) implements CustomUnbakedBlockStateModel {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockStateModel.Unbaked.CODEC.listOf(1, Integer.MAX_VALUE).fieldOf("models").forGetter(Unbaked::models)
        ).apply(instance, Unbaked::new));

        @Override
        public BlockStateModel bake(ModelBaker baker) {
            return new WardedGlassModel(models.stream().map(model -> model.bake(baker)).toList());
        }

        @Override
        public void resolveDependencies(Resolver resolver) {
            for (BlockStateModel.Unbaked model : models) {
                model.resolveDependencies(resolver);
            }
        }

        @Override
        public MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
            return MAP_CODEC;
        }
    }
}
