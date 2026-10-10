package thaumcraft.item.armor;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import thaumcraft.item.gear.Repairable;
import thaumcraft.item.gear.RunicArmor;

public class CultistPlateArmorItem extends Item implements Repairable, RunicArmor {
    public CultistPlateArmorItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getRunicCharge(ItemStack stack) {
        return 0;
    }
}
