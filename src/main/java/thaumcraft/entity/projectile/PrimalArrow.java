package thaumcraft.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.registry.ModItems;

public class PrimalArrow extends AbstractArrow {
    public PrimalArrow(EntityType<? extends PrimalArrow> type, Level level) {
        super(type, level);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.AIR_ARROW.get());
    }
}
