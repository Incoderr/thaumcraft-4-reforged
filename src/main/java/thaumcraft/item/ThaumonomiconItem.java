package thaumcraft.item;

import net.minecraft.world.item.Item;

public class ThaumonomiconItem extends Item {
    private final boolean cheat;

    public ThaumonomiconItem(Properties properties, boolean cheat) {
        super(properties);
        this.cheat = cheat;
    }

    public boolean isCheat() {
        return cheat;
    }

    public static Properties properties(Properties properties) {
        return properties.stacksTo(1);
    }
}
