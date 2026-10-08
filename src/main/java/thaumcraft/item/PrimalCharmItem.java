package thaumcraft.item;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class PrimalCharmItem extends Item {
    public PrimalCharmItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
        RandomSource random = RandomSource.create(stack.hashCode() + System.currentTimeMillis() / 6000L);
        if (random.nextInt(200) < 25) {
            builder.accept(Component.translatable("tc.primalcharm." + random.nextInt(5)).withStyle(ChatFormatting.GOLD));
        }
        super.appendHoverText(stack, context, display, builder, flag);
    }
}
