package thaumcraft;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class StartupConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue GEN_TAINT = BUILDER
        .translation("thaumcraft.configuration.generate_taint")
        .define("world_generation.generate_taint", true);
    public static final ModConfigSpec.IntValue BIOME_TAINT_WEIGHT = BUILDER
        .translation("thaumcraft.configuration.taint_biome_weight")
        .defineInRange("biomes.taint_biome_weight", 1, 1, 100);
    public static final ModConfigSpec.IntValue BIOME_MAGICAL_FOREST_WEIGHT = BUILDER
        .translation("thaumcraft.configuration.magical_forest_biome_weight")
        .defineInRange("biomes.magical_forest_biome_weight", 3, 1, 100);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private StartupConfig() {
    }

    public static boolean genTaint() {
        return SPEC.isLoaded() ? GEN_TAINT.getAsBoolean() : GEN_TAINT.getDefault();
    }

    public static int taintWeight() {
        return SPEC.isLoaded() ? BIOME_TAINT_WEIGHT.getAsInt() : BIOME_TAINT_WEIGHT.getDefault();
    }

    public static int magicalForestWeight() {
        return SPEC.isLoaded() ? BIOME_MAGICAL_FOREST_WEIGHT.getAsInt() : BIOME_MAGICAL_FOREST_WEIGHT.getDefault();
    }
}
