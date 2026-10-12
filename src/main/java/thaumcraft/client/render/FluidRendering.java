package thaumcraft.client.render;

import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import thaumcraft.Thaumcraft;
import thaumcraft.registry.ModFluids;

@EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class FluidRendering {
    private FluidRendering() {
    }

    @SubscribeEvent
    static void registerModels(RegisterFluidModelsEvent event) {
        event.register(
            new FluidModel.Unbaked(
                new Material(Thaumcraft.id("block/fluidpure")),
                new Material(Thaumcraft.id("block/fluidpure")),
                null,
                null
            ),
            ModFluids.PURIFYING_FLUID,
            ModFluids.FLOWING_PURIFYING_FLUID
        );
        event.register(
            new FluidModel.Unbaked(
                new Material(Thaumcraft.id("block/fluiddeath")),
                new Material(Thaumcraft.id("block/fluiddeath")),
                null,
                null
            ),
            ModFluids.LIQUID_DEATH,
            ModFluids.FLOWING_LIQUID_DEATH
        );
    }

    @SubscribeEvent
    static void registerExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(new IClientFluidTypeExtensions() {}, ModFluids.PURIFYING_FLUID_TYPE);
        event.registerFluidType(new IClientFluidTypeExtensions() {}, ModFluids.LIQUID_DEATH_TYPE);
    }
}
