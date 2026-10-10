package thaumcraft.wand;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import thaumcraft.platform.StackKey;

public final class WandKeys {
    public static final StackKey<WandParts> PARTS = StackKey.register("wand_parts", WandParts.CODEC, WandParts.STREAM_CODEC);
    public static final StackKey<WandVis> VIS = StackKey.register("wand_vis", WandVis.CODEC, WandVis.STREAM_CODEC);
    public static final StackKey<ItemStack> FOCUS = StackKey.register("wand_focus", ItemStack.CODEC, ItemStack.STREAM_CODEC);
    public static final StackKey<WandArea> AREA = StackKey.register("wand_area", WandArea.CODEC, WandArea.STREAM_CODEC);
    public static final StackKey<BlockPos> TARGET = StackKey.register("wand_target", BlockPos.CODEC, BlockPos.STREAM_CODEC);

    private WandKeys() {
    }

    public static void init() {
    }
}
