package thaumcraft.essentia;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import thaumcraft.aspect.AspectProvidingItem;

public class FilledJarItem extends Item implements AspectProvidingItem {
    private final Block jar;

    public FilledJarItem(Block jar, Properties properties) {
        super(properties);
        this.jar = jar;
    }

    public Block jar() {
        return jar;
    }
}
