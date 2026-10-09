package thaumcraft.client.research;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import thaumcraft.client.platform.BrowserDraw;
import thaumcraft.research.ResearchItem;

public class ResearchToast implements Toast {
    private static final String TITLE = "Research Completed!";
    private static final long DISPLAY_TIME = 3000L;
    private static final float MAX_NAME_WIDTH = 125.0F;

    private final ResearchItem research;
    private final ItemStack icon;
    private final Component name;
    private Visibility visibility = Visibility.SHOW;

    public ResearchToast(ResearchItem research) {
        this.research = research;
        this.icon = research.iconItem() == null ? ItemStack.EMPTY : research.iconItem().create();
        this.name = Component.translatable(research.nameKey());
    }

    @Override
    public Visibility getWantedVisibility() {
        return visibility;
    }

    @Override
    public void update(ToastManager manager, long fullyVisibleForMs) {
        visibility = fullyVisibleForMs >= DISPLAY_TIME ? Visibility.HIDE : Visibility.SHOW;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, Font font, long fullyVisibleForMs) {
        BrowserDraw.toastBackground(graphics, width(), height());
        graphics.text(font, TITLE, 30, 7, 0xFFFFFF00, false);
        int nameWidth = font.width(name);
        if (nameWidth <= MAX_NAME_WIDTH) {
            graphics.text(font, name, 30, 18, 0xFFFFFFFF, false);
        } else {
            float scale = MAX_NAME_WIDTH / nameWidth;
            graphics.pose().pushMatrix();
            graphics.pose().translate(30.0F, 16.0F + 2.0F / scale);
            graphics.pose().scale(scale, scale);
            graphics.text(font, name, 0, 0, 0xFFFFFFFF, false);
            graphics.pose().popMatrix();
        }
        if (!icon.isEmpty()) {
            graphics.fakeItem(icon, 8, 8);
        } else if (research.iconTexture() != null) {
            BrowserDraw.full(graphics, research.iconTexture(), 8, 8, 16, 16, 0xFFFFFFFF);
        }
    }
}
