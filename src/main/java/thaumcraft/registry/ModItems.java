package thaumcraft.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import thaumcraft.Thaumcraft;
import thaumcraft.item.AspectContainerItem;
import thaumcraft.item.CrystalEssenceItem;
import thaumcraft.item.LootBagItem;
import thaumcraft.item.PrimalCharmItem;
import thaumcraft.item.ZombieBrainItem;
import thaumcraft.platform.Registration;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Thaumcraft.MODID);

    public static final DeferredItem<Item> ALUMENTUM = ITEMS.registerSimpleItem("alumentum", properties -> Registration.fuel(properties, 6400));
    public static final DeferredItem<Item> NITOR = ITEMS.registerSimpleItem("nitor");
    public static final DeferredItem<Item> THAUMIUM_INGOT = ITEMS.registerSimpleItem("thaumium_ingot");
    public static final DeferredItem<Item> QUICKSILVER = ITEMS.registerSimpleItem("quicksilver");
    public static final DeferredItem<Item> MAGIC_TALLOW = ITEMS.registerSimpleItem("magic_tallow");
    public static final DeferredItem<Item> AMBER = ITEMS.registerSimpleItem("amber");
    public static final DeferredItem<Item> ENCHANTED_FABRIC = ITEMS.registerSimpleItem("enchanted_fabric");
    public static final DeferredItem<Item> VIS_FILTER = ITEMS.registerSimpleItem("vis_filter");
    public static final DeferredItem<Item> KNOWLEDGE_FRAGMENT = ITEMS.registerSimpleItem("knowledge_fragment");
    public static final DeferredItem<Item> MIRRORED_GLASS = ITEMS.registerSimpleItem("mirrored_glass");
    public static final DeferredItem<Item> TAINTED_GOO = ITEMS.registerSimpleItem("tainted_goo");
    public static final DeferredItem<Item> TAINT_TENDRIL = ITEMS.registerSimpleItem("taint_tendril");
    public static final DeferredItem<Item> JAR_LABEL = ITEMS.registerItem("jar_label", properties -> new AspectContainerItem(properties, "", false));
    public static final DeferredItem<Item> SALIS_MUNDUS = ITEMS.registerSimpleItem("salis_mundus");
    public static final DeferredItem<Item> PRIMAL_CHARM = ITEMS.registerItem("primal_charm", PrimalCharmItem::new);
    public static final DeferredItem<Item> VOID_METAL_INGOT = ITEMS.registerSimpleItem("void_metal_ingot");
    public static final DeferredItem<Item> VOID_SEED = ITEMS.registerSimpleItem("void_seed");
    public static final DeferredItem<Item> GOLD_COIN = ITEMS.registerSimpleItem("gold_coin");

    public static final DeferredItem<Item> AIR_SHARD = ITEMS.registerSimpleItem("air_shard");
    public static final DeferredItem<Item> FIRE_SHARD = ITEMS.registerSimpleItem("fire_shard");
    public static final DeferredItem<Item> WATER_SHARD = ITEMS.registerSimpleItem("water_shard");
    public static final DeferredItem<Item> EARTH_SHARD = ITEMS.registerSimpleItem("earth_shard");
    public static final DeferredItem<Item> ORDER_SHARD = ITEMS.registerSimpleItem("order_shard");
    public static final DeferredItem<Item> ENTROPY_SHARD = ITEMS.registerSimpleItem("entropy_shard");
    public static final DeferredItem<Item> BALANCED_SHARD = ITEMS.registerSimpleItem("balanced_shard");

    public static final DeferredItem<Item> COPPER_NUGGET = ITEMS.registerSimpleItem("copper_nugget");
    public static final DeferredItem<Item> TIN_NUGGET = ITEMS.registerSimpleItem("tin_nugget");
    public static final DeferredItem<Item> SILVER_NUGGET = ITEMS.registerSimpleItem("silver_nugget");
    public static final DeferredItem<Item> LEAD_NUGGET = ITEMS.registerSimpleItem("lead_nugget");
    public static final DeferredItem<Item> QUICKSILVER_DROP = ITEMS.registerSimpleItem("quicksilver_drop");
    public static final DeferredItem<Item> THAUMIUM_NUGGET = ITEMS.registerSimpleItem("thaumium_nugget");
    public static final DeferredItem<Item> VOID_METAL_NUGGET = ITEMS.registerSimpleItem("void_metal_nugget");
    public static final DeferredItem<Item> NATIVE_IRON_CLUSTER = ITEMS.registerSimpleItem("native_iron_cluster");
    public static final DeferredItem<Item> NATIVE_COPPER_CLUSTER = ITEMS.registerSimpleItem("native_copper_cluster");
    public static final DeferredItem<Item> NATIVE_TIN_CLUSTER = ITEMS.registerSimpleItem("native_tin_cluster");
    public static final DeferredItem<Item> NATIVE_SILVER_CLUSTER = ITEMS.registerSimpleItem("native_silver_cluster");
    public static final DeferredItem<Item> NATIVE_LEAD_CLUSTER = ITEMS.registerSimpleItem("native_lead_cluster");
    public static final DeferredItem<Item> NATIVE_CINNABAR_CLUSTER = ITEMS.registerSimpleItem("native_cinnabar_cluster");
    public static final DeferredItem<Item> NATIVE_GOLD_CLUSTER = ITEMS.registerSimpleItem("native_gold_cluster");

    public static final DeferredItem<Item> CHICKEN_NUGGET = nugget("chicken_nugget");
    public static final DeferredItem<Item> BEEF_NUGGET = nugget("beef_nugget");
    public static final DeferredItem<Item> PORK_NUGGET = nugget("pork_nugget");
    public static final DeferredItem<Item> FISH_NUGGET = nugget("fish_nugget");
    public static final DeferredItem<Item> TRIPLE_MEAT_TREAT = ITEMS.registerSimpleItem(
        "triple_meat_treat",
        properties -> Registration.food(properties, 6, 0.8F, true, 1.6F, new MobEffectInstance(MobEffects.REGENERATION, 100, 0), 0.66F)
    );
    public static final DeferredItem<Item> ZOMBIE_BRAIN = ITEMS.registerItem(
        "zombie_brain",
        ZombieBrainItem::new,
        properties -> Registration.food(properties, 4, 0.2F, false, 1.6F, new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.8F)
    );

    public static final DeferredItem<Item> GLASS_PHIAL = ITEMS.registerSimpleItem("glass_phial");
    public static final DeferredItem<Item> ESSENTIA_PHIAL = ITEMS.registerItem("essentia_phial", properties -> new AspectContainerItem(properties, " x ", true));
    public static final DeferredItem<Item> WISP_ESSENCE = ITEMS.registerItem("wisp_essence", properties -> new AspectContainerItem(properties, " x", true));
    public static final DeferredItem<Item> CRYSTALLIZED_ESSENCE = ITEMS.registerItem("crystallized_essence", CrystalEssenceItem::new);

    public static final DeferredItem<Item> COMMON_LOOT_BAG = ITEMS.registerItem("common_loot_bag", LootBagItem::new);
    public static final DeferredItem<Item> UNCOMMON_LOOT_BAG = ITEMS.registerItem("uncommon_loot_bag", LootBagItem::new, properties -> properties.rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> RARE_LOOT_BAG = ITEMS.registerItem("rare_loot_bag", LootBagItem::new, properties -> properties.rarity(Rarity.RARE));

    private ModItems() {
    }

    private static DeferredItem<Item> nugget(String name) {
        return ITEMS.registerSimpleItem(name, properties -> Registration.food(properties, 1, 0.3F, false, 0.5F));
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
