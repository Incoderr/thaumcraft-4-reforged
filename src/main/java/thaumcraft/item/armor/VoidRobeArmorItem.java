package thaumcraft.item.armor;

import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.Nullable;
import thaumcraft.aspect.Aspect;
import thaumcraft.item.gear.Goggles;
import thaumcraft.item.gear.Repairable;
import thaumcraft.item.gear.RunicArmor;
import thaumcraft.item.gear.WarpingGear;
import thaumcraft.node.Revealer;
import thaumcraft.wand.VisDiscountGear;

public class VoidRobeArmorItem extends Item implements Repairable, RunicArmor, VisDiscountGear, Goggles, Revealer, WarpingGear {
    public VoidRobeArmorItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getRunicCharge(ItemStack stack) {
        return 0;
    }

    @Override
    public int getVisDiscount(ItemStack stack, Player player, Aspect aspect) {
        return 5;
    }

    @Override
    public int getWarp(ItemStack stack, Player player) {
        return 2;
    }

    @Override
    public boolean showNodes(ItemStack stack, LivingEntity entity) {
        return ArmorSupport.slotOf(stack) == EquipmentSlot.HEAD;
    }

    @Override
    public boolean showIngamePopups(ItemStack stack, LivingEntity entity) {
        return ArmorSupport.slotOf(stack) == EquipmentSlot.HEAD;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
        ArmorSupport.visDiscountLine(builder, getVisDiscount(stack, null, null));
        super.appendHoverText(stack, context, display, builder, flag);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, level, owner, slot);
        ArmorSupport.selfRepair(stack, owner);
    }
}
