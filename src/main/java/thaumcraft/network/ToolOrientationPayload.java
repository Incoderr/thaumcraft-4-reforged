package thaumcraft.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import thaumcraft.Thaumcraft;
import thaumcraft.item.tool.ElementalShovelItem;

public record ToolOrientationPayload() implements CustomPacketPayload {
    public static final Type<ToolOrientationPayload> TYPE = new Type<>(Thaumcraft.id("tool_orientation"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ToolOrientationPayload> STREAM_CODEC = StreamCodec.unit(new ToolOrientationPayload());

    @Override
    public Type<ToolOrientationPayload> type() {
        return TYPE;
    }

    public static void handle(ToolOrientationPayload payload, Player player) {
        ElementalShovelItem.cycleOrientation(player);
    }
}
