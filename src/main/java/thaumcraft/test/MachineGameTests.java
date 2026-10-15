package thaumcraft.test;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import thaumcraft.Thaumcraft;
import thaumcraft.crafting.ArcaneRecipe;
import thaumcraft.crafting.InfusionRecipe;
import thaumcraft.crafting.TcRecipe;
import thaumcraft.crafting.TcRecipes;
import thaumcraft.crafting.TcStack;
import thaumcraft.device.ArcaneBoreBlock;
import thaumcraft.device.ArcaneBoreBlockEntity;
import thaumcraft.device.ArcaneSpaBlockEntity;
import thaumcraft.device.InfernalFurnaceBlock;
import thaumcraft.device.InfernalFurnaceBlockEntity;
import thaumcraft.device.InfernalTiles;
import thaumcraft.device.SmeltingBonuses;
import thaumcraft.registry.ModBlocks;
import thaumcraft.registry.ModItems;
import thaumcraft.research.Knowledge;
import thaumcraft.research.ResearchCategories;
import thaumcraft.wand.WandRegistry;
import thaumcraft.wand.WandTriggers;
import thaumcraft.wand.Wands;

@EventBusSubscriber(modid = Thaumcraft.MODID)
public final class MachineGameTests {
    private static final DeferredRegister<Consumer<GameTestHelper>> FUNCTIONS = DeferredRegister.create(Registries.TEST_FUNCTION, Thaumcraft.MODID);
    private static final String[] TESTS = {
        "infernal_furnace_forms_and_smelts",
        "infernal_furnace_bonus_nuggets",
        "infernal_furnace_collapses",
        "arcane_bore_digs_block",
        "arcane_spa_places_water",
        "device_recipes_resolve"
    };

    static {
        FUNCTIONS.register(TESTS[0], () -> MachineGameTests::furnaceFormsAndSmelts);
        FUNCTIONS.register(TESTS[1], () -> MachineGameTests::furnaceBonusNuggets);
        FUNCTIONS.register(TESTS[2], () -> MachineGameTests::furnaceCollapses);
        FUNCTIONS.register(TESTS[3], () -> MachineGameTests::boreDigs);
        FUNCTIONS.register(TESTS[4], () -> MachineGameTests::spaPlacesWater);
        FUNCTIONS.register(TESTS[5], () -> MachineGameTests::deviceRecipesResolve);
    }

    private MachineGameTests() {
    }

    public static void register(IEventBus bus) {
        FUNCTIONS.register(bus);
    }

    @SubscribeEvent
    static void registerTests(RegisterGameTestsEvent event) {
        Holder<TestEnvironmentDefinition<?>> environment = event.registerEnvironment(Thaumcraft.id("machine_default"), new TestEnvironmentDefinition.AllOf());
        for (String name : TESTS) {
            ResourceKey<Consumer<GameTestHelper>> function = ResourceKey.create(Registries.TEST_FUNCTION, Thaumcraft.id(name));
            event.registerTest(
                Thaumcraft.id(name),
                new FunctionGameTestInstance(function, new TestData<>(environment, Thaumcraft.id("test/platform"), 400, 0, true))
            );
        }
    }

    private static void buildFurnace(GameTestHelper helper, BlockPos origin) {
        for (int layer = 0; layer < 3; layer++) {
            for (int x = 0; x < 3; x++) {
                for (int z = 0; z < 3; z++) {
                    boolean corner = x != 1 && z != 1;
                    Block block = corner ? Blocks.NETHER_BRICKS : Blocks.OBSIDIAN;
                    if (x == 1 && z == 1) {
                        block = layer == 2 ? Blocks.AIR : layer == 1 ? Blocks.LAVA : Blocks.OBSIDIAN;
                    }
                    if (layer == 1 && x == 1 && z == 0) {
                        block = Blocks.IRON_BARS;
                    }
                    helper.setBlock(origin.offset(x, layer, z), block);
                }
            }
        }
    }

    private static boolean form(GameTestHelper helper, BlockPos origin) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        Knowledge.get(player).addResearch("INFERNALFURNACE");
        ItemStack wand = Wands.create(WandRegistry.CAP_GOLD, WandRegistry.ROD_GREATWOOD, false);
        Wands.fill(wand);
        return WandTriggers.perform(helper.getLevel(), wand, player, helper.absolutePos(origin.offset(0, 0, 0)), Direction.UP);
    }

    private static void furnaceFormsAndSmelts(GameTestHelper helper) {
        BlockPos origin = new BlockPos(1, 1, 1);
        buildFurnace(helper, origin);
        helper.assertTrue(form(helper, origin), "wand trigger did not form the furnace");
        BlockPos core = origin.offset(1, 1, 1);
        helper.assertBlockPresent(ModBlocks.INFERNAL_FURNACE.get(), core);
        helper.assertValueEqual(helper.getBlockState(core).getValue(InfernalFurnaceBlock.TILE), InfernalTiles.CORE, "core tile");
        helper.assertValueEqual(helper.getBlockState(core).getValue(InfernalFurnaceBlock.FACING), Direction.NORTH, "bars side");
        helper.assertBlockPresent(ModBlocks.INFERNAL_FURNACE.get(), origin.offset(1, 1, 0));
        helper.assertTrue(helper.getBlockState(origin.offset(1, 1, 0)).getValue(InfernalFurnaceBlock.TILE) == InfernalTiles.BARS, "bars tile");
        InfernalFurnaceBlockEntity furnace = helper.getBlockEntity(core, InfernalFurnaceBlockEntity.class);
        furnace.addSpeedyTime(600);
        helper.assertTrue(furnace.addItemsToInventory(new ItemStack(Items.IRON_ORE)), "ore rejected");
        helper.succeedWhen(() -> {
            int ingots = 0;
            for (ItemEntity entity : helper.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(helper.absolutePos(core)).inflate(4.0))) {
                if (entity.getItem().is(Items.IRON_INGOT)) {
                    ingots += entity.getItem().getCount();
                }
            }
            helper.assertTrue(ingots == 1, "ingot not smelted: " + ingots);
        });
    }

    private static void furnaceBonusNuggets(GameTestHelper helper) {
        BlockPos origin = new BlockPos(1, 1, 1);
        buildFurnace(helper, origin);
        helper.assertTrue(form(helper, origin), "wand trigger did not form the furnace");
        InfernalFurnaceBlockEntity furnace = helper.getBlockEntity(origin.offset(1, 1, 1), InfernalFurnaceBlockEntity.class);
        ServerLevel level = helper.getLevel();
        ItemStack ore = new ItemStack(Items.IRON_ORE);
        Item bonus = SmeltingBonuses.get(ore);
        helper.assertTrue(bonus == Items.IRON_NUGGET, "iron ore bonus");
        helper.assertTrue(SmeltingBonuses.get(new ItemStack(ModItems.NATIVE_GOLD_CLUSTER.get())) == Items.GOLD_NUGGET, "gold cluster bonus");
        helper.assertTrue(SmeltingBonuses.get(new ItemStack(Items.STONE)) == null, "stone has no bonus");
        for (int index = 0; index < 64; index++) {
            furnace.ejectItem(level, new ItemStack(Items.IRON_INGOT), ore);
        }
        int nuggets = 0;
        for (ItemEntity entity : level.getEntitiesOfClass(ItemEntity.class, new AABB(helper.absolutePos(origin)).inflate(6.0))) {
            if (entity.getItem().is(Items.IRON_NUGGET)) {
                nuggets += entity.getItem().getCount();
            }
        }
        helper.assertTrue(nuggets > 0 && nuggets < 64, "bonus nuggets out of bounds: " + nuggets);
        helper.succeed();
    }

    private static void furnaceCollapses(GameTestHelper helper) {
        BlockPos origin = new BlockPos(1, 1, 1);
        buildFurnace(helper, origin);
        helper.assertTrue(form(helper, origin), "wand trigger did not form the furnace");
        helper.getLevel().destroyBlock(helper.absolutePos(origin.offset(0, 0, 0)), false);
        helper.assertBlockPresent(Blocks.AIR, origin.offset(1, 1, 1));
        helper.assertBlockPresent(Blocks.OBSIDIAN, origin.offset(1, 0, 1));
        helper.assertBlockPresent(Blocks.IRON_BARS, origin.offset(1, 1, 0));
        helper.assertBlockPresent(Blocks.NETHER_BRICKS, origin.offset(2, 2, 2));
        helper.assertTrue(helper.getLevel().getEntitiesOfClass(Blaze.class, new AABB(helper.absolutePos(origin)).inflate(5.0)).isEmpty(), "breaking a wall spawned a blaze");
        buildFurnace(helper, origin);
        helper.assertTrue(form(helper, origin), "furnace did not reform");
        helper.getLevel().destroyBlock(helper.absolutePos(origin.offset(1, 1, 1)), false);
        helper.assertTrue(!helper.getLevel().getEntitiesOfClass(Blaze.class, new AABB(helper.absolutePos(origin)).inflate(5.0)).isEmpty(), "breaking the core did not spawn a blaze");
        helper.succeed();
    }

    private static void boreDigs(GameTestHelper helper) {
        BlockPos base = new BlockPos(1, 1, 3);
        BlockPos bore = base.above();
        helper.setBlock(base, ModBlocks.ARCANE_BORE_BASE.get());
        helper.setBlock(bore, ModBlocks.ARCANE_BORE.get().defaultBlockState().setValue(ArcaneBoreBlock.BASE, Direction.UP));
        helper.setBlock(bore.above(), Blocks.REDSTONE_BLOCK);
        for (int distance = 3; distance <= 5; distance++) {
            helper.setBlock(bore.east(distance), Blocks.STONE);
        }
        ArcaneBoreBlockEntity entity = helper.getBlockEntity(bore, ArcaneBoreBlockEntity.class);
        entity.setOrientation(Direction.EAST, true);
        entity.setItem(ArcaneBoreBlockEntity.FOCUS_SLOT, new ItemStack(ModItems.FOCUS_EXCAVATION.get()));
        entity.setItem(ArcaneBoreBlockEntity.PICKAXE_SLOT, new ItemStack(Items.IRON_PICKAXE));
        helper.succeedWhen(() -> {
            helper.assertBlockPresent(Blocks.AIR, bore.east(3));
            int cobble = 0;
            for (ItemEntity item : helper.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(helper.absolutePos(bore)).inflate(6.0))) {
                if (item.getItem().is(Items.COBBLESTONE)) {
                    cobble += item.getItem().getCount();
                }
            }
            helper.assertTrue(cobble > 0, "no cobblestone dropped");
            helper.assertTrue(entity.getItem(ArcaneBoreBlockEntity.PICKAXE_SLOT).getDamageValue() == 1, "pickaxe not damaged");
        });
    }

    private static void spaPlacesWater(GameTestHelper helper) {
        BlockPos pos = new BlockPos(3, 1, 3);
        helper.setBlock(pos, ModBlocks.ARCANE_SPA.get());
        ArcaneSpaBlockEntity spa = helper.getBlockEntity(pos, ArcaneSpaBlockEntity.class);
        spa.toggleMix();
        helper.assertTrue(spa.fillFrom(new FluidStack(Fluids.WATER, 5000)), "tank rejected water");
        helper.succeedWhen(() -> {
            helper.assertBlockPresent(Blocks.WATER, pos.above());
            helper.assertValueEqual(spa.getTank().getAmount(), 4000, "tank after placing");
        });
    }

    private static void deviceRecipesResolve(GameTestHelper helper) {
        for (String key : new String[] {"ArcaneSpa", "ArcaneBoreBase", "ArcaneBore"}) {
            TcRecipe recipe = TcRecipes.get(key);
            helper.assertTrue(recipe != null, "recipe missing: " + key);
            TcStack result = recipe instanceof ArcaneRecipe arcane ? arcane.result() : recipe instanceof InfusionRecipe infusion ? infusion.result() : null;
            helper.assertTrue(result != null && result.exists(), "recipe result unresolved: " + key);
            helper.assertTrue(hasResearch(recipe.research()), "research missing: " + recipe.research());
        }
        helper.succeed();
    }

    private static boolean hasResearch(String key) {
        return ResearchCategories.getResearch(key) != null;
    }
}
