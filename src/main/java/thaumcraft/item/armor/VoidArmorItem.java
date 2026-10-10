package thaumcraft.item.armor;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import thaumcraft.item.gear.Repairable;
import thaumcraft.item.gear.RunicArmor;
import thaumcraft.item.gear.WarpingGear;

public class VoidArmorItem extends Item implements Repairable, RunicArmor, WarpingGear {
    public VoidArmorItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getRunicCharge(ItemStack stack) {
        return 0;
    }

    @Override
    public int getWarp(ItemStack stack, Player player) {
        return 1;
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, level, owner, slot);
        ArmorSupport.selfRepair(stack, owner);
    }
}
