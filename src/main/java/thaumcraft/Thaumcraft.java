package thaumcraft;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import thaumcraft.aspect.AspectKeys;
import thaumcraft.item.NodeJarKeys;
import thaumcraft.network.ModPayloads;
import thaumcraft.platform.Platform;
import thaumcraft.registry.ModBlockEntities;
import thaumcraft.registry.ModBlocks;
import thaumcraft.registry.ModCreativeTabs;
import thaumcraft.registry.ModFeatures;
import thaumcraft.registry.ModItems;
import thaumcraft.registry.ModSounds;
import thaumcraft.registry.ModVillagers;
import thaumcraft.test.ThaumcraftGameTests;
import thaumcraft.wand.WandKeys;

@Mod(Thaumcraft.MODID)
public class Thaumcraft {
    public static final String MODID = "thaumcraft";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Thaumcraft(IEventBus modEventBus, ModContainer modContainer) {
        AspectKeys.init();
        WandKeys.init();
        NodeJarKeys.init();
        Platform.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModSounds.register(modEventBus);
        ModFeatures.register(modEventBus);
        ModVillagers.register(modEventBus);
        ModPayloads.register();
        ThaumcraftGameTests.register(modEventBus);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
