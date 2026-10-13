package thaumcraft.entity.golem;

import com.mojang.serialization.Codec;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.ItemStack;
import thaumcraft.infusion.InfusionKeys;
import thaumcraft.platform.StackKey;

public final class GolemKeys {
    public static final StackKey<GolemSnapshot> STATE = StackKey.register("golem_state", GolemSnapshot.CODEC, ByteBufCodecs.fromCodecWithRegistries(GolemSnapshot.CODEC));
    public static final Codec<BellSelection> BELL_CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(instance -> instance.group(
        net.minecraft.core.UUIDUtil.CODEC.fieldOf("entity").forGetter(BellSelection::entity),
        GlobalPos.CODEC.fieldOf("home").forGetter(BellSelection::home),
        Marker.CODEC.listOf().fieldOf("markers").forGetter(BellSelection::markers)
    ).apply(instance, BellSelection::new));
    public static final StackKey<BellSelection> BELL = StackKey.register("golem_bell_selection", BELL_CODEC, ByteBufCodecs.fromCodecWithRegistries(BELL_CODEC));

    private GolemKeys() {
    }

    public record BellSelection(UUID entity, GlobalPos home, java.util.List<Marker> markers) {
        public BellSelection {
            markers = java.util.List.copyOf(markers);
        }
    }

    public static GolemSnapshot snapshot(ItemStack stack) {
        GolemSnapshot state = STATE.getOrDefault(stack, GolemSnapshot.EMPTY);
        return InfusionKeys.TAGS.getOrDefault(stack, java.util.Map.of()).containsKey("advanced") ? state.withAdvanced() : state;
    }

    public static void init() {
    }
}
