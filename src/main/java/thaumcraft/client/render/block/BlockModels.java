package thaumcraft.client.render.block;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterBlockStateModels;
import thaumcraft.Thaumcraft;
import thaumcraft.client.render.taint.TaintFibresModel;

@EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class BlockModels {
    private BlockModels() {
    }

    @SubscribeEvent
    static void registerModels(RegisterBlockStateModels event) {
        event.registerModel(Thaumcraft.id("warded"), WardedBlockModel.Unbaked.MAP_CODEC);
        event.registerModel(Thaumcraft.id("taint_fibres"), TaintFibresModel.Unbaked.MAP_CODEC);
    }
}
