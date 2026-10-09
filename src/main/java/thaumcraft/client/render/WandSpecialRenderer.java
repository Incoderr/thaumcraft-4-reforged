package thaumcraft.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import java.util.function.Consumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;
import thaumcraft.Thaumcraft;
import thaumcraft.client.geom.WandGeometry;
import thaumcraft.client.platform.ItemGeometry;
import thaumcraft.wand.WandParts;
import thaumcraft.wand.WandRod;
import thaumcraft.wand.Wands;

public class WandSpecialRenderer implements SpecialModelRenderer<WandParts> {
    private static final Identifier SCRIPT = Thaumcraft.id("textures/misc/script.png");
    private static final int BLEND_ADDITIVE = 1;

    @Override
    public void submit(
        @Nullable WandParts parts,
        PoseStack poseStack,
        SubmitNodeCollector collector,
        int lightCoords,
        int overlayCoords,
        boolean hasFoil,
        int outlineColor
    ) {
        WandParts resolved = parts == null ? WandParts.DEFAULT : parts;
        WandRod rod = resolved.getRod();
        boolean staff = rod.staff();
        boolean sceptre = resolved.sceptre();
        float ticks = Util.getMillis() / 50.0F;
        int rodLight = lightCoords;
        if (rod.glowing()) {
            int glow = (int) (200.0F + (float) Math.sin(ticks) * 5.0F + 5.0F);
            rodLight = Math.max(lightCoords & 0xFFFF, glow) | (lightCoords & 0xFFFF0000);
        }
        int light = rodLight;
        ItemGeometry.solid(collector.order(0), poseStack, rod.texture(), false, sink -> WandGeometry.rod(sink, staff, light, 0xFFFFFFFF));
        ItemGeometry.solid(collector.order(0), poseStack, resolved.getCap().texture(), false, sink -> WandGeometry.caps(sink, staff, sceptre, lightCoords));
        if (sceptre || rod.runes()) {
            int runeLight = Math.max(lightCoords & 0xFFFF, 200) | (lightCoords & 0xFFFF0000);
            ItemGeometry.blended(
                collector.order(1),
                poseStack,
                SCRIPT,
                BLEND_ADDITIVE,
                sink -> WandGeometry.runes(sink, staff, sceptre, rod.runes(), ticks, runeLight)
            );
        }
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        for (float x : new float[] {0.35F, 0.65F}) {
            for (float y : new float[] {0.0F, 1.0F}) {
                for (float z : new float[] {0.35F, 0.65F}) {
                    output.accept(new Vector3f(x, y, z));
                }
            }
        }
    }

    @Override
    public @Nullable WandParts extractArgument(ItemStack stack) {
        return Wands.getParts(stack);
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<WandParts> {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<WandParts> bake(SpecialModelRenderer.BakingContext context) {
            return new WandSpecialRenderer();
        }

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
