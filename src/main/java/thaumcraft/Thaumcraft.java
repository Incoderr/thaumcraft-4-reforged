package thaumcraft;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import thaumcraft.aspect.AspectKeys;
import thaumcraft.crafting.ConfigRecipes;
import thaumcraft.essentia.EssentiaKeys;
import thaumcraft.infusion.InfusionKeys;
import thaumcraft.item.NodeJarKeys;
import thaumcraft.item.tool.ToolKeys;
import thaumcraft.network.ModPayloads;
import thaumcraft.platform.Platform;
import thaumcraft.registry.ModBlockEntities;
import thaumcraft.registry.ModBlocks;
import thaumcraft.registry.ModCapabilities;
import thaumcraft.registry.ModCreativeTabs;
import thaumcraft.registry.ModEntities;
import thaumcraft.registry.ModFeatures;
import thaumcraft.registry.ModItems;
import thaumcraft.registry.ModMenus;
import thaumcraft.registry.ModSounds;
import thaumcraft.registry.ModVillagers;
import thaumcraft.research.ConfigResearch;
import thaumcraft.research.Knowledge;
import thaumcraft.research.ResearchNoteKeys;
import thaumcraft.item.armor.ArmorKeys;
import thaumcraft.registry.ModRecipes;
import thaumcraft.test.AlchemyGameTests;
import thaumcraft.test.ArmorGameTests;
import thaumcraft.test.ArcaneGameTests;
import thaumcraft.test.EssentiaGameTests;
import thaumcraft.test.GearGameTests;
import thaumcraft.test.HexGridGameTests;
import thaumcraft.test.InfusionGameTests;
import thaumcraft.test.MachineGameTests;
import thaumcraft.test.ThaumcraftGameTests;
import thaumcraft.wand.WandKeys;
import thaumcraft.wand.focus.FocusKeys;

@Mod(Thaumcraft.MODID)
public class Thaumcraft {
    public static final String MODID = "thaumcraft";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Thaumcraft(IEventBus modEventBus, ModContainer modContainer) {
        AspectKeys.init();
        WandKeys.init();
        FocusKeys.init();
        ToolKeys.init();
        NodeJarKeys.init();
        EssentiaKeys.init();
        InfusionKeys.init();
        ArmorKeys.init();
        ResearchNoteKeys.init();
        Knowledge.init();
        Platform.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEntities.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModSounds.register(modEventBus);
        ModFeatures.register(modEventBus);
        ModVillagers.register(modEventBus);
        ModMenus.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModCapabilities.register();
        ModPayloads.register();
        ConfigRecipes.init();
        ConfigResearch.init();
        ThaumcraftGameTests.register(modEventBus);
        HexGridGameTests.register(modEventBus);
        EssentiaGameTests.register(modEventBus);
        AlchemyGameTests.register(modEventBus);
        ArcaneGameTests.register(modEventBus);
        InfusionGameTests.register(modEventBus);
        ArmorGameTests.register(modEventBus);
        GearGameTests.register(modEventBus);
        MachineGameTests.register(modEventBus);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
