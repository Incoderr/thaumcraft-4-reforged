package thaumcraft.platform.baubles;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import thaumcraft.Thaumcraft;

public final class BaubleMenus {
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Thaumcraft.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<BaublesMenu>> EXPANDED = MENUS.register("baubles", () -> IMenuTypeExtension.create((containerId, inventory, buffer) -> new BaublesMenu(containerId, inventory)));

    private BaubleMenus() {
    }

    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }
}
