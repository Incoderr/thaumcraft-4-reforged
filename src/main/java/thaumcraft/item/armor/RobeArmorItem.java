package thaumcraft.item.armor;

import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import thaumcraft.aspect.Aspect;
import thaumcraft.item.gear.Repairable;
import thaumcraft.item.gear.RunicArmor;
import thaumcraft.wand.VisDiscountGear;

public class RobeArmorItem extends Item implements Repairable, VisDiscountGear, RunicArmor {
    public RobeArmorItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getRunicCharge(ItemStack stack) {
        return 0;
    }

    @Override
    public int getVisDiscount(ItemStack stack, Player player, Aspect aspect) {
        return ArmorSupport.slotOf(stack) == EquipmentSlot.FEET ? 1 : 2;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
        ArmorSupport.visDiscountLine(builder, getVisDiscount(stack, null, null));
    }
}
