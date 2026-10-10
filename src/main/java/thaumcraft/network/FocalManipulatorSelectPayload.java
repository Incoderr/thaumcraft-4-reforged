package thaumcraft.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import thaumcraft.Thaumcraft;
import thaumcraft.menu.FocalManipulatorMenu;

public record FocalManipulatorSelectPayload(int containerId, int upgrade) implements CustomPacketPayload {
    public static final Type<FocalManipulatorSelectPayload> TYPE = new Type<>(Thaumcraft.id("focal_manipulator_select"));
    public static final StreamCodec<RegistryFriendlyByteBuf, FocalManipulatorSelectPayload> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, FocalManipulatorSelectPayload::containerId,
        ByteBufCodecs.VAR_INT, FocalManipulatorSelectPayload::upgrade,
        FocalManipulatorSelectPayload::new
    );

    public static void handle(FocalManipulatorSelectPayload payload, Player player) {
        if (player instanceof ServerPlayer serverPlayer
            && serverPlayer.containerMenu instanceof FocalManipulatorMenu menu
            && menu.containerId == payload.containerId()) {
            menu.startCraft(serverPlayer, payload.upgrade());
        }
    }

    @Override
    public Type<FocalManipulatorSelectPayload> type() {
        return TYPE;
    }
}
