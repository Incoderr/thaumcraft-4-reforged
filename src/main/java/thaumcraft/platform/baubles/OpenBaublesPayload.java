package thaumcraft.platform.baubles;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import thaumcraft.Thaumcraft;
import thaumcraft.platform.ModNetwork;

public record OpenBaublesPayload() implements CustomPacketPayload {
    public static final Type<OpenBaublesPayload> TYPE = new Type<>(Thaumcraft.id("open_baubles"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenBaublesPayload> CODEC = StreamCodec.unit(new OpenBaublesPayload());

    public static void register() {
        ModNetwork.toServer(TYPE, CODEC, OpenBaublesPayload::handle);
    }

    private static void handle(OpenBaublesPayload payload, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(new SimpleMenuProvider((containerId, inventory, user) -> new BaublesMenu(containerId, inventory), Component.translatable("container.crafting")));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
