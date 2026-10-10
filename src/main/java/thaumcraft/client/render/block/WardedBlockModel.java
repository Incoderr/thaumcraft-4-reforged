package thaumcraft.client.render.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DynamicBlockStateModel;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import org.jspecify.annotations.Nullable;
import thaumcraft.blockentity.WardedBlockEntity;

public class WardedBlockModel implements DynamicBlockStateModel {
    private final BlockStateModel fallback;

    public WardedBlockModel(BlockStateModel fallback) {
        this.fallback = fallback;
    }

    private @Nullable BlockState disguise(BlockAndTintGetter level, BlockPos pos) {
        BlockState disguise = level.getModelData(pos).get(WardedBlockEntity.DISGUISE);
        return disguise == null || disguise.getBlock() instanceof thaumcraft.block.WardedBlock ? null : disguise;
    }

    private BlockStateModel model(@Nullable BlockState disguise) {
        return disguise == null ? fallback : Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(disguise);
    }

    @Override
    public @Nullable Object createGeometryKey(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
        return null;
    }

    @Override
    public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
        BlockState disguise = disguise(level, pos);
        model(disguise).collectParts(level, pos, disguise == null ? state : disguise, random, parts);
    }

    @Override
    public Material.Baked particleMaterial() {
        return fallback.particleMaterial();
    }

    @Override
    public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        BlockState disguise = disguise(level, pos);
        return model(disguise).particleMaterial(level, pos, disguise == null ? state : disguise);
    }

    @Override
    public int materialFlags() {
        return fallback.materialFlags();
    }

    @Override
    public int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        BlockState disguise = disguise(level, pos);
        return model(disguise).materialFlags(level, pos, disguise == null ? state : disguise);
    }

    public record Unbaked(BlockStateModel.Unbaked fallback) implements CustomUnbakedBlockStateModel {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockStateModel.Unbaked.CODEC.fieldOf("fallback").forGetter(Unbaked::fallback)
        ).apply(instance, Unbaked::new));

        @Override
        public BlockStateModel bake(ModelBaker baker) {
            return new WardedBlockModel(fallback.bake(baker));
        }

        @Override
        public void resolveDependencies(Resolver resolver) {
            fallback.resolveDependencies(resolver);
        }

        @Override
        public MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
            return MAP_CODEC;
        }
    }
}
