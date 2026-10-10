package thaumcraft.wand.focus;

import com.mojang.serialization.Codec;
import java.util.List;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.ItemStack;
import thaumcraft.platform.StackKey;

public final class FocusKeys {
    public static final StackKey<List<Integer>> UPGRADES = StackKey.register(
        "focus_upgrades",
        Codec.INT.listOf(),
        ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.list())
    );
    public static final StackKey<List<ItemStack>> POUCH = StackKey.register(
        "focus_pouch",
        ItemStack.OPTIONAL_CODEC.listOf(),
        ItemStack.OPTIONAL_LIST_STREAM_CODEC
    );

    private FocusKeys() {
    }

    public static void init() {
    }
}
