package thaumcraft.taint;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import thaumcraft.Thaumcraft;
import thaumcraft.platform.BiomeEdit;

public final class TaintBiomes {
    public static final ResourceKey<Biome> TAINTED_LAND = ResourceKey.create(
        Registries.BIOME,
        Identifier.fromNamespaceAndPath(Thaumcraft.MODID, "tainted_land")
    );
    public static final ResourceKey<Biome> EERIE = key("eerie");
    public static final ResourceKey<Biome> MAGICAL_FOREST = key("magical_forest");

    private TaintBiomes() {
    }

    private static ResourceKey<Biome> key(String name) {
        return ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Thaumcraft.MODID, name));
    }

    public static boolean isTainted(ServerLevel level, BlockPos pos) {
        return level.getBiome(pos).is(TAINTED_LAND);
    }

    public static boolean set(ServerLevel level, BlockPos pos, ResourceKey<Biome> key) {
        Holder<Biome> biome = level.registryAccess().lookupOrThrow(Registries.BIOME).getOrThrow(key);
        return BiomeEdit.setColumn(level, pos, biome);
    }

    public static boolean spread(ServerLevel level, BlockPos pos, int adjacentTaint, int spreadRate) {
        if (spreadRate <= 0 || adjacentTaint < 2 || level.getRandom().nextInt(spreadRate * 5) != 0) {
            return false;
        }
        int xOffset = level.getRandom().nextInt(3) - 1;
        int zOffset = level.getRandom().nextInt(3) - 1;
        BlockPos target = pos.offset(xOffset, 0, zOffset);
        if (isTainted(level, target)) {
            return false;
        }
        return set(level, target, TAINTED_LAND);
    }

    public static boolean purify(ServerLevel level, BlockPos pos) {
        if (!level.hasChunkAt(pos)) {
            return false;
        }
        if (isTainted(level, pos)) {
            return set(level, pos, Biomes.PLAINS);
        }
        if (level.getBiome(pos).is(EERIE) || level.getBiome(pos).is(MAGICAL_FOREST)) {
            return true;
        }
        return false;
    }
}
