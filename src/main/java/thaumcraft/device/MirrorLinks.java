package thaumcraft.device;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.Nullable;

public final class MirrorLinks {
    public static final Component ALREADY_LINKED = Component.translatable("tc.mirror.alreadylinked").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC);

    private MirrorLinks() {
    }

    public static Component dimensionName(ResourceKey<Level> dimension) {
        if (dimension == Level.OVERWORLD) {
            return Component.translatable("tc.mirror.dim.overworld");
        }
        if (dimension == Level.NETHER) {
            return Component.translatable("tc.mirror.dim.nether");
        }
        if (dimension == Level.END) {
            return Component.translatable("tc.mirror.dim.end");
        }
        return Component.literal(dimension.identifier().toString());
    }

    public static Component describe(GlobalPos link) {
        BlockPos pos = link.pos();
        return Component.translatable("tc.mirror.linkdesc", pos.getX(), pos.getY(), pos.getZ(), dimensionName(link.dimension()));
    }

    public static void appendLink(GlobalPos link, Consumer<Component> builder) {
        builder.accept(Component.translatable("tc.handmirrorlinkedto").append(" ").append(describe(link)).withStyle(ChatFormatting.GRAY));
    }

    public static @Nullable ServerLevel levelOf(ServerLevel from, GlobalPos link) {
        return from.getServer().getLevel(link.dimension());
    }

    public static @Nullable BlockEntity fetch(ServerLevel level, BlockPos pos) {
        if (level.isOutsideBuildHeight(pos)) {
            return null;
        }
        level.getChunkAt(pos);
        return level.getBlockEntity(pos);
    }

    public static ItemEntity ejected(Level level, BlockPos pos, Direction face, ItemStack stack) {
        ItemEntity item = new ItemEntity(
            level,
            pos.getX() + 0.5 - face.getStepX() * 0.3,
            pos.getY() + 0.5 - face.getStepY() * 0.3,
            pos.getZ() + 0.5 - face.getStepZ() * 0.3,
            stack,
            face.getStepX() * 0.15F,
            face.getStepY() * 0.15F,
            face.getStepZ() * 0.15F
        );
        item.setPortalCooldown(20);
        return item;
    }
}
