package thaumcraft.wand;

import net.minecraft.core.BlockPos;
import thaumcraft.platform.StackKey;

public final class WandKeys {
    public static final StackKey<WandParts> PARTS = StackKey.register("wand_parts", WandParts.CODEC, WandParts.STREAM_CODEC);
    public static final StackKey<WandVis> VIS = StackKey.register("wand_vis", WandVis.CODEC, WandVis.STREAM_CODEC);
    public static final StackKey<BlockPos> TARGET = StackKey.register("wand_target", BlockPos.CODEC, BlockPos.STREAM_CODEC);

    private WandKeys() {
    }

    public static void init() {
    }
}
