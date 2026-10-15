package thaumcraft.client.aspect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import thaumcraft.Thaumcraft;
import thaumcraft.client.ClientConfig;
import thaumcraft.aspect.Aspect;
import thaumcraft.aspect.AspectHelper;
import thaumcraft.aspect.AspectList;

@EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class AspectSlotOverlay {
    private AspectSlotOverlay() {
    }

    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Render.Post event) {
        if (!(event.getScreen() instanceof AbstractContainerScreen<?> screen)) {
            return;
        }
        if (Minecraft.getInstance().hasShiftDown() == ClientConfig.showTags()) {
            return;
        }
        Slot slot = screen.getHoveredSlot();
        if (slot == null || !slot.hasItem()) {
            return;
        }
        ItemStack stack = slot.getItem();
        if (!AspectVisibility.isScanned(stack)) {
            return;
        }
        AspectList aspects = AspectHelper.getBonusTags(stack, AspectHelper.getObjectTags(stack));
        if (aspects.size() == 0) {
            return;
        }
        GuiGraphicsExtractor graphics = event.getGuiGraphics();
        graphics.nextStratum();
        int index = 0;
        for (Aspect aspect : aspects.getAspectsSortedAmount()) {
            int x = event.getMouseX() + 17 + index * 18 - 8;
            int y = event.getMouseY() + 7 - 33 - 8;
            AspectRenderer.drawBack(graphics, x, y);
            if (AspectVisibility.isKnown(aspect)) {
                AspectRenderer.drawTag(graphics, x, y, aspect, aspects.getAmount(aspect));
            } else {
                AspectRenderer.drawUnknown(graphics, x, y);
            }
            index++;
        }
    }
}
