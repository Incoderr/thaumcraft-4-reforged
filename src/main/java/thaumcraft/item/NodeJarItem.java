package thaumcraft.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class NodeJarItem extends BlockItem {
    public NodeJarItem(Block block, Properties properties) {
        super(block, properties);
    }

    public static Properties properties(Properties properties) {
        return properties.stacksTo(1);
    }
}
