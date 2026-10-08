package thaumcraft.item;

import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class AspectContainerItem extends Item {
    private final String separator;
    private final boolean withAmount;

    public AspectContainerItem(Properties properties, String separator, boolean withAmount) {
        super(properties);
        this.separator = separator;
        this.withAmount = withAmount;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
        AspectTooltips.append(stack, builder, separator, withAmount);
        super.appendHoverText(stack, context, display, builder, flag);
    }
}
