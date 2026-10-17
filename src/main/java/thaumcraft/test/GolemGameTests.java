package thaumcraft.test;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import thaumcraft.Thaumcraft;
import thaumcraft.aspect.Aspect;
import thaumcraft.crafting.InfusionCrafting;
import thaumcraft.device.HungryChestBlockEntity;
import thaumcraft.entity.golem.GolemBase;
import thaumcraft.entity.golem.GolemKeys;
import thaumcraft.entity.golem.GolemSnapshot;
import thaumcraft.entity.golem.GolemType;
import thaumcraft.entity.golem.LogisticsGoals;
import thaumcraft.entity.golem.Marker;
import thaumcraft.entity.golem.TravelingTrunk;
import thaumcraft.essentia.JarBlockEntity;
import thaumcraft.menu.GolemMenu;
import thaumcraft.platform.GolemFluids;
import thaumcraft.registry.ModBlocks;
import thaumcraft.registry.ModEntities;

@EventBusSubscriber(modid = Thaumcraft.MODID)
public final class GolemGameTests {
    private static final DeferredRegister<Consumer<GameTestHelper>> FUNCTIONS = DeferredRegister.create(Registries.TEST_FUNCTION, Thaumcraft.MODID);
    private static final Map<String, Consumer<GameTestHelper>> TESTS = Map.ofEntries(
        Map.entry("golem_material_attributes", GolemGameTests::materials),
        Map.entry("golem_place_on_chest", GolemGameTests::placeOnChest),
        Map.entry("golem_bell_rebind_chest", GolemGameTests::bellRebindChest),
        Map.entry("golem_bell_place_cycle", GolemGameTests::bellCycle),
        Map.entry("golem_bell_dismantle_core", GolemGameTests::bellDismantleCore),
        Map.entry("golem_tag_filter_components", GolemGameTests::tagFilter),
        Map.entry("golem_snapshot_roundtrip", GolemGameTests::snapshot),
        Map.entry("golem_save_resources", GolemGameTests::saveResources),
        Map.entry("golem_advanced_infusion", GolemGameTests::advancedInfusion),
        Map.entry("golem_ghost_filter_safe", GolemGameTests::ghostFilter),
        Map.entry("golem_filter_colors", GolemGameTests::filterColors),
        Map.entry("golem_partial_item_transfer", GolemGameTests::partialItems),
        Map.entry("golem_world_fluid_source", GolemGameTests::worldFluid),
        Map.entry("golem_partial_essentia_transfer", GolemGameTests::partialEssentia),
        Map.entry("golem_harvest_wheat", GolemGameTests::harvest),
        Map.entry("traveling_trunk_inventory_roundtrip", GolemGameTests::trunkSave)
    );
    static { TESTS.forEach((name, test) -> FUNCTIONS.register(name, () -> test)); }
    private GolemGameTests() { }
    public static void register(IEventBus bus) { FUNCTIONS.register(bus); }
    @SubscribeEvent
    static void registerTests(RegisterGameTestsEvent event) {
        Holder<TestEnvironmentDefinition<?>> environment = event.registerEnvironment(Thaumcraft.id("golem_default"), new TestEnvironmentDefinition.AllOf());
        for (String name : TESTS.keySet()) event.registerTest(Thaumcraft.id(name), new FunctionGameTestInstance(ResourceKey.create(Registries.TEST_FUNCTION, Thaumcraft.id(name)), new TestData<>(environment, Thaumcraft.id("test/platform"), 600, 0, true)));
    }
    private static GolemSnapshot state(int core) { return new GolemSnapshot(core, false, List.of(), "", List.of(), ItemContainerContents.EMPTY, List.of(), 0); }
    private static GolemBase golem(GameTestHelper helper, GolemType type, int core) {
        GolemBase golem = new GolemBase(ModEntities.GOLEM.get(), helper.getLevel());
        BlockPos home = helper.absolutePos(new BlockPos(3, 2, 2));
        golem.setPos(home.getX() + 0.5, home.getY(), home.getZ() + 0.5);
        golem.setHome(home, Direction.EAST);
        golem.configure(type, state(core));
        golem.setHealth(golem.getMaxHealth());
        return golem;
    }
    private static net.minecraft.world.phys.BlockHitResult golemHit(BlockPos pos, Direction face) {
        return new net.minecraft.world.phys.BlockHitResult(net.minecraft.world.phys.Vec3.atCenterOf(pos).add(face.getStepX() * 0.5, face.getStepY() * 0.5, face.getStepZ() * 0.5), face, pos, false);
    }
    private static void placeOnChest(GameTestHelper helper) {
        var level = helper.getLevel();
        BlockPos chestPos = helper.absolutePos(new BlockPos(2, 2, 2));
        level.setBlockAndUpdate(chestPos, Blocks.CHEST.defaultBlockState());
        var chest = (net.minecraft.world.level.block.entity.ChestBlockEntity) level.getBlockEntity(chestPos);
        var player = (net.minecraft.server.level.ServerPlayer) helper.makeMockServerPlayer(GameType.SURVIVAL);
        player.setPos(chestPos.getX() + 0.5, chestPos.getY(), chestPos.getZ() - 2.0);
        for (Direction face : List.of(Direction.UP, Direction.EAST)) {
            ItemStack packed = new ItemStack(BuiltInRegistries.ITEM.getValue(Thaumcraft.id("wood_golem")));
            GolemKeys.STATE.set(packed, state(2));
            player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, packed);
            var result = player.gameMode.useItemOn(player, level, packed, net.minecraft.world.InteractionHand.MAIN_HAND, golemHit(chestPos, face));
            helper.assertTrue(result.consumesAction(), "chest intercepts golem placement");
            helper.assertTrue(player.containerMenu == player.inventoryMenu, "placing a golem opens the chest");
            var placed = level.getEntitiesOfClass(GolemBase.class, new net.minecraft.world.phys.AABB(chestPos).inflate(3));
            helper.assertValueEqual(placed.size(), 1, "placement must create exactly one golem");
            GolemBase golem = placed.getFirst();
            helper.assertTrue(golem.getHomePosition().equals(chestPos.relative(face)) && golem.getHomeFacing() == face, "placement loses chest attachment");
            helper.assertTrue(player.getMainHandItem().isEmpty(), "placement must consume exactly one packed golem");
            golem.setCarried(new ItemStack(Items.WHEAT, 3));
            GoalSelector goals = new GoalSelector();
            LogisticsGoals.install(golem, goals);
            goals.tick();
            helper.assertTrue(golem.getCarried().isEmpty(), "gather golem cannot deposit into its placement chest");
            golem.discard();
        }
        helper.assertValueEqual(chest.getItem(0).getCount(), 6, "placement chest loses gathered items");
        level.setBlockAndUpdate(chestPos.above(), Blocks.STONE.defaultBlockState());
        ItemStack blocked = new ItemStack(BuiltInRegistries.ITEM.getValue(Thaumcraft.id("wood_golem")));
        player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, blocked);
        var failed = player.gameMode.useItemOn(player, level, blocked, net.minecraft.world.InteractionHand.MAIN_HAND, golemHit(chestPos, Direction.UP));
        helper.assertTrue(failed == net.minecraft.world.InteractionResult.FAIL && blocked.getCount() == 1, "blocked placement consumes a golem");
        helper.assertTrue(level.getEntitiesOfClass(GolemBase.class, new net.minecraft.world.phys.AABB(chestPos).inflate(3)).isEmpty(), "blocked placement spawns a golem inside a block");
        helper.succeed();
    }
    private static void bellRebindChest(GameTestHelper helper) {
        var level = helper.getLevel();
        BlockPos oldChest = helper.absolutePos(new BlockPos(1, 2, 2));
        BlockPos newChest = oldChest.east(3);
        level.setBlockAndUpdate(oldChest, Blocks.CHEST.defaultBlockState());
        level.setBlockAndUpdate(newChest, Blocks.CHEST.defaultBlockState());
        GolemBase golem = golem(helper, GolemType.WOOD, 2);
        golem.setHome(oldChest.above(), Direction.UP);
        golem.setPos(oldChest.getX() + 0.5, oldChest.getY() + 1, oldChest.getZ() + 0.5);
        golem.inventory().setItem(0, new ItemStack(Items.WHEAT));
        golem.setCarried(new ItemStack(Items.WHEAT, 7));
        level.addFreshEntity(golem);
        var before = golem.snapshot();
        var player = (net.minecraft.server.level.ServerPlayer) helper.makeMockServerPlayer(GameType.SURVIVAL);
        player.setPos(oldChest.getX() + 0.5, oldChest.getY(), oldChest.getZ() - 1);
        ItemStack bell = new ItemStack(BuiltInRegistries.ITEM.getValue(Thaumcraft.id("golem_bell")));
        player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, bell);
        var unbound = player.gameMode.useItemOn(player, level, bell, net.minecraft.world.InteractionHand.MAIN_HAND, golemHit(newChest, Direction.WEST));
        helper.assertTrue(unbound instanceof net.minecraft.world.InteractionResult.Success success && success.shouldSwing() && player.containerMenu == player.inventoryMenu, "unbound bell opens chest instead of swinging");
        helper.assertTrue(player.interactOn(golem, net.minecraft.world.InteractionHand.MAIN_HAND, golem.position()).consumesAction(), "bell cannot select the golem");
        helper.assertTrue(GolemKeys.BELL.get(bell).entity().equals(golem.getUUID()), "bell selects the wrong golem");
        player.gameMode.useItemOn(player, level, bell, net.minecraft.world.InteractionHand.MAIN_HAND, golemHit(newChest, Direction.WEST));
        helper.assertValueEqual(golem.getMarkers().size(), 1, "chest intercepts the bell marker");
        helper.assertTrue(player.containerMenu == player.inventoryMenu, "bell opens chest instead of marking it");
        helper.assertTrue(golem.getHomePosition().equals(oldChest.above()), "ordinary marker changes the home");
        var markers = golem.getMarkers();
        player.setShiftKeyDown(true);
        for (int repeat = 0; repeat < 2; repeat++) {
            var result = player.gameMode.useItemOn(player, level, bell, net.minecraft.world.InteractionHand.MAIN_HAND, golemHit(newChest, Direction.WEST));
            helper.assertTrue(result instanceof net.minecraft.world.InteractionResult.Success success && success.shouldSwing(), "rebind must request the bell swing");
            helper.assertTrue(golem.getHomePosition().equals(newChest.west()) && golem.getHomeFacing() == Direction.WEST, "bell does not rebind chest and face");
        }
        helper.assertTrue(golem.getMarkers().equals(markers), "rebind changes work markers");
        helper.assertTrue(GolemKeys.BELL.get(bell).home().pos().equals(newChest.west()), "bell overlay retains the old home");
        helper.assertTrue(golem.getCarried().getCount() == 7 && golem.inventory().getItem(0).getCount() == 1 && golem.getCore() == before.core(), "rebind loses carried items, filters or core");
        helper.assertTrue(bell.getCount() == 1, "rebind consumes the bell");
        golem.setPos(newChest.getX() - 0.5, newChest.getY(), newChest.getZ() + 0.5);
        GoalSelector goals = new GoalSelector();
        LogisticsGoals.install(golem, goals);
        goals.tick();
        var destination = (net.minecraft.world.level.block.entity.ChestBlockEntity) level.getBlockEntity(newChest);
        var original = (net.minecraft.world.level.block.entity.ChestBlockEntity) level.getBlockEntity(oldChest);
        helper.assertTrue(golem.getCarried().isEmpty() && destination.getItem(0).getCount() == 7 && original.isEmpty(), "rebound gather golem deposits into the wrong chest");
        player.setShiftKeyDown(false);
        player.gameMode.useItemOn(player, level, bell, net.minecraft.world.InteractionHand.MAIN_HAND, golemHit(newChest, Direction.WEST));
        helper.assertTrue(golem.getMarkers().isEmpty(), "ordinary bell click cannot remove a chest marker");
        helper.succeed();
    }
    private static void materials(GameTestHelper helper) {
        for (GolemType type : GolemType.values()) {
            GolemBase golem = golem(helper, type, 2);
            helper.assertValueEqual((int) golem.getMaxHealth(), type.health, "material health");
            helper.assertValueEqual(golem.getCarryLimit(), type.carry, "material carry");
            helper.assertTrue(golem.fireImmune() == type.fireResist, "material fire immunity");
        }
        helper.succeed();
    }
    private static void snapshot(GameTestHelper helper) {
        GolemBase original = golem(helper, GolemType.THAUMIUM, 0);
        Marker marker = new Marker(GlobalPos.of(helper.getLevel().dimension(), original.getHomePosition().east(2)), Direction.WEST, 3);
        original.configure(GolemType.THAUMIUM, new GolemSnapshot(0, true, List.of(2, 3, 4), "HGR", List.of(marker), ItemContainerContents.fromItems(List.of(new ItemStack(Items.WHEAT, 12))), List.of(3), 160));
        ItemStack packed = original.toItem(false);
        GolemBase restored = golem(helper, GolemType.THAUMIUM, -1);
        restored.configure(GolemType.THAUMIUM, GolemKeys.snapshot(packed));
        helper.assertTrue(original.snapshot().equals(restored.snapshot()), "packed golem loses settings");
        helper.assertValueEqual(restored.inventory().getItem(0).getCount(), 12, "filter count");
        helper.succeed();
    }
    private static void bellCycle(GameTestHelper helper) {
        GolemBase original = golem(helper, GolemType.WOOD, 2);
        original.inventory().setItem(0, new ItemStack(Items.WHEAT));
        original.setCarried(new ItemStack(Items.WHEAT, 3));
        var expected = original.snapshot();
        helper.getLevel().addFreshEntity(original);
        var player = (net.minecraft.server.level.ServerPlayer) helper.makeMockServerPlayer(GameType.SURVIVAL);
        player.setPos(original.position().add(3, 0, 0));
        ItemStack bell = new ItemStack(BuiltInRegistries.ITEM.getValue(Thaumcraft.id("golem_bell")));
        player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, bell);
        player.attack(original);
        helper.assertTrue(original.isRemoved(), "bell leaves original entity alive");
        var drops = helper.getLevel().getEntitiesOfClass(net.minecraft.world.entity.item.ItemEntity.class, original.getBoundingBox().inflate(2));
        helper.assertFalse(drops.stream().anyMatch(item -> item.getItem().is(thaumcraft.item.golem.GolemItems.core(2).getItem())), "ordinary attack with bell removes core from golem");
        var packed = drops.stream().filter(item -> item.getItem().getItem() instanceof thaumcraft.item.golem.GolemPlacerItem).findFirst().orElseThrow();
        player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, packed.getItem().copy());
        packed.discard();
        BlockPos ground = original.getHomePosition().below();
        helper.getLevel().setBlockAndUpdate(ground, Blocks.STONE.defaultBlockState());
        var context = new net.minecraft.world.item.context.UseOnContext(player, net.minecraft.world.InteractionHand.MAIN_HAND, new net.minecraft.world.phys.BlockHitResult(net.minecraft.world.phys.Vec3.atCenterOf(ground), Direction.UP, ground, false));
        helper.assertTrue(player.getMainHandItem().useOn(context).consumesAction(), "packed golem cannot be placed");
        var placed = helper.getLevel().getEntitiesOfClass(GolemBase.class, original.getBoundingBox().inflate(2));
        helper.assertValueEqual(placed.size(), 1, "bell cycle duplicates golem");
        helper.assertTrue(expected.equals(placed.getFirst().snapshot()), "bell cycle loses configuration");
        helper.assertTrue(player.getMainHandItem().isEmpty(), "placement does not consume item");
        helper.assertValueEqual(drops.stream().filter(item -> item.getItem().is(Items.WHEAT)).mapToInt(item -> item.getItem().getCount()).sum(), 3, "bell loses carried items");
        helper.succeed();
    }
    private static void bellDismantleCore(GameTestHelper helper) {
        GolemBase original = golem(helper, GolemType.THAUMIUM, 2);
        original.configure(GolemType.THAUMIUM, new GolemSnapshot(2, true, List.of(2, 3, 4), "P", List.of(), ItemContainerContents.EMPTY, List.of(), 0));
        original.setCarried(new ItemStack(Items.WHEAT, 3));
        helper.getLevel().addFreshEntity(original);
        var player = (net.minecraft.server.level.ServerPlayer) helper.makeMockServerPlayer(GameType.SURVIVAL);
        player.setPos(original.position().add(1, 0, 0));
        player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, new ItemStack(BuiltInRegistries.ITEM.getValue(Thaumcraft.id("golem_bell"))));
        player.setShiftKeyDown(true);
        player.attack(original);
        helper.assertTrue(original.isRemoved(), "Shift + attack with bell does not dismantle golem");
        var drops = helper.getLevel().getEntitiesOfClass(net.minecraft.world.entity.item.ItemEntity.class, original.getBoundingBox().inflate(2));
        var cores = drops.stream().filter(item -> item.getItem().is(thaumcraft.item.golem.GolemItems.core(2).getItem())).toList();
        helper.assertValueEqual(cores.stream().mapToInt(item -> item.getItem().getCount()).sum(), 1, "bell must return exactly one core");
        var packed = drops.stream().filter(item -> item.getItem().getItem() instanceof thaumcraft.item.golem.GolemPlacerItem).toList();
        helper.assertValueEqual(packed.size(), 1, "bell must return exactly one empty golem");
        GolemSnapshot dismantled = GolemKeys.snapshot(packed.getFirst().getItem());
        helper.assertValueEqual(dismantled.core(), -1, "dismantled golem still contains its core");
        helper.assertTrue(dismantled.advanced(), "dismantling removes advanced infusion");
        helper.assertTrue(dismantled.upgrades().isEmpty() && dismantled.decoration().isEmpty(), "dismantled golem still contains upgrades or decoration");
        helper.assertValueEqual(drops.stream().filter(item -> item.getItem().is(Items.WHEAT)).mapToInt(item -> item.getItem().getCount()).sum(), 3, "dismantling loses carried items");
        player.attack(original);
        var repeated = helper.getLevel().getEntitiesOfClass(net.minecraft.world.entity.item.ItemEntity.class, original.getBoundingBox().inflate(2));
        helper.assertValueEqual(repeated.size(), drops.size(), "attacking dismantled golem duplicates drops");
        helper.succeed();
    }
    private static void tagFilter(GameTestHelper helper) {
        GolemBase golem = golem(helper, GolemType.THAUMIUM, 0);
        golem.configure(GolemType.THAUMIUM, new GolemSnapshot(0, false, List.of(5), "", List.of(), ItemContainerContents.EMPTY, List.of(), 1 << 5));
        ItemStack filter = new ItemStack(Items.OAK_PLANKS);
        ItemStack alternative = new ItemStack(Items.BIRCH_PLANKS);
        helper.assertTrue(golem.matchesFilter(filter, alternative), "tag equivalent rejected");
        alternative.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME, net.minecraft.network.chat.Component.literal("named"));
        helper.assertFalse(golem.matchesFilter(filter, alternative), "component-sensitive filter accepts named item");
        golem.setToggle(7, true);
        helper.assertTrue(golem.matchesFilter(filter, alternative), "ignore-data filter rejects equivalent");
        helper.succeed();
    }
    private static void saveResources(GameTestHelper helper) {
        GolemBase original = golem(helper, GolemType.IRON, 5);
        UUID owner = UUID.randomUUID();
        original.setOwnerUUID(owner);
        original.setFluidCarried(new FluidStack(Fluids.WATER, 3000));
        original.setCarried(new ItemStack(Items.WHEAT, 7));
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, helper.getLevel().registryAccess());
        original.saveWithoutId(output);
        GolemBase loaded = new GolemBase(ModEntities.GOLEM.get(), helper.getLevel());
        loaded.load(TagValueInput.create(ProblemReporter.DISCARDING, helper.getLevel().registryAccess(), output.buildResult()));
        helper.assertValueEqual(loaded.getFluidCarried().getAmount(), 3000, "saved fluid");
        helper.assertValueEqual(loaded.getCarried().getCount(), 7, "saved carried items");
        helper.assertTrue(owner.equals(loaded.getOwnerUUID()) && original.getHomePosition().equals(loaded.getHomePosition()), "saved owner/home");
        helper.succeed();
    }
    private static void advancedInfusion(GameTestHelper helper) {
        ItemStack central = new ItemStack(BuiltInRegistries.ITEM.getValue(Thaumcraft.id("wood_golem")));
        GolemKeys.STATE.set(central, new GolemSnapshot(2, false, List.of(3), "H", List.of(), ItemContainerContents.fromItems(List.of(new ItemStack(Items.WHEAT))), List.of(5), 0));
        ItemStack output = InfusionCrafting.result(helper.getLevel(), InfusionCrafting.TYPE_RECIPE, "AdvancedGolem", central);
        helper.assertTrue(output.is(central.getItem()), "advanced infusion changes material");
        helper.assertTrue(GolemKeys.snapshot(output).advanced(), "advanced infusion flag");
        helper.assertValueEqual(GolemKeys.snapshot(output).core(), 2, "advanced infusion removes core");
        helper.assertTrue(GolemKeys.snapshot(output).filters().equals(GolemKeys.snapshot(central).filters()), "advanced infusion removes filters");
        helper.succeed();
    }
    private static void ghostFilter(GameTestHelper helper) {
        GolemBase golem = golem(helper, GolemType.WOOD, 0);
        helper.getLevel().addFreshEntity(golem);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(golem.position());
        GolemMenu menu = new GolemMenu(1, player.getInventory(), golem.getId());
        menu.setCarried(new ItemStack(Items.WHEAT, 12));
        menu.clicked(0, 0, ContainerInput.PICKUP, player);
        helper.assertValueEqual(menu.getCarried().getCount(), 12, "ghost filter consumes cursor");
        helper.assertValueEqual(golem.inventory().getItem(0).getCount(), 12, "ghost count");
        menu.clicked(0, 0, ContainerInput.QUICK_MOVE, player);
        helper.assertValueEqual(menu.getCarried().getCount(), 12, "shift click duplicates cursor");
        menu.removed(player);
        helper.succeed();
    }
    private static void filterColors(GameTestHelper helper) {
        GolemBase golem = golem(helper, GolemType.WOOD, 2);
        golem.inventory().setItem(0, new ItemStack(Items.WHEAT));
        golem.setColors(0, 4);
        helper.assertTrue(golem.getColorsMatching(new ItemStack(Items.WHEAT)).equals(List.of(4)), "colored filter");
        helper.assertTrue(golem.getColorsMatching(new ItemStack(Items.CARROT)).isEmpty(), "unmatched filter accepted");
        golem.inventory().clearContent();
        helper.assertValueEqual(golem.getColorsMatching(new ItemStack(Items.CARROT)).size(), 6, "empty filters should accept all");
        helper.succeed();
    }
    private static void partialItems(GameTestHelper helper) {
        GolemBase golem = golem(helper, GolemType.IRON, 2);
        BlockPos pos = golem.getHomePosition().west();
        helper.getLevel().setBlockAndUpdate(pos, ModBlocks.HUNGRY_CHEST.get().defaultBlockState());
        HungryChestBlockEntity chest = (HungryChestBlockEntity) helper.getLevel().getBlockEntity(pos);
        for (int slot = 0; slot < chest.getContainerSize(); slot++) chest.setItem(slot, new ItemStack(Items.COBBLESTONE, 64));
        chest.setItem(0, new ItemStack(Items.WHEAT, 60));
        golem.setCarried(new ItemStack(Items.WHEAT, 10));
        GoalSelector goals = new GoalSelector();
        LogisticsGoals.install(golem, goals);
        goals.tick();
        helper.assertValueEqual(chest.getItem(0).getCount(), 64, "partial target amount");
        helper.assertValueEqual(golem.getCarried().getCount(), 6, "partial carry remainder");
        helper.succeed();
    }
    private static void worldFluid(GameTestHelper helper) {
        BlockPos source = helper.absolutePos(new BlockPos(2, 2, 2));
        helper.getLevel().setBlockAndUpdate(source, Blocks.WATER.defaultBlockState());
        helper.assertValueEqual(GolemFluids.pickup(helper.getLevel(), source, true).getAmount(), 1000, "source preview");
        helper.assertTrue(helper.getLevel().getFluidState(source).isSource(), "preview drains source");
        helper.assertValueEqual(GolemFluids.pickup(helper.getLevel(), source, false).getAmount(), 1000, "source pickup");
        helper.assertTrue(helper.getLevel().getFluidState(source).isEmpty(), "source not drained");
        helper.succeed();
    }
    private static void partialEssentia(GameTestHelper helper) {
        GolemBase golem = golem(helper, GolemType.IRON, 6);
        BlockPos pos = golem.getHomePosition().east();
        helper.getLevel().setBlockAndUpdate(pos, ModBlocks.WARDED_JAR.get().defaultBlockState());
        JarBlockEntity jar = (JarBlockEntity) helper.getLevel().getBlockEntity(pos);
        jar.addToContainer(Aspect.AIR, 60);
        golem.setMarkers(List.of(new Marker(GlobalPos.of(helper.getLevel().dimension(), pos), Direction.UP, -1)));
        golem.setEssentia(Aspect.AIR, 8);
        GoalSelector goals = new GoalSelector();
        LogisticsGoals.install(golem, goals);
        goals.tick();
        helper.assertValueEqual(jar.getAmount(), 64, "partial jar amount");
        helper.assertValueEqual(golem.getEssentiaAmount(), 4, "partial essentia remainder");
        helper.succeed();
    }
    private static void harvest(GameTestHelper helper) {
        GolemBase golem = golem(helper, GolemType.STRAW, 3);
        BlockPos crop = golem.getHomePosition().east();
        helper.getLevel().setBlockAndUpdate(crop.below(), Blocks.FARMLAND.defaultBlockState());
        helper.getLevel().setBlockAndUpdate(crop, Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, 7));
        helper.getLevel().addFreshEntity(golem);
        helper.succeedWhen(() -> helper.assertFalse(helper.getLevel().getBlockState(crop).is(Blocks.WHEAT), "straw golem has not harvested mature wheat"));
    }
    private static void trunkSave(GameTestHelper helper) {
        TravelingTrunk trunk = new TravelingTrunk(ModEntities.TRAVELING_TRUNK.get(), helper.getLevel());
        trunk.setUpgrade(1);
        trunk.inventory().setItem(35, new ItemStack(Items.DIAMOND, 17));
        trunk.setStay(true);
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, helper.getLevel().registryAccess());
        trunk.saveWithoutId(output);
        TravelingTrunk loaded = new TravelingTrunk(ModEntities.TRAVELING_TRUNK.get(), helper.getLevel());
        loaded.load(TagValueInput.create(ProblemReporter.DISCARDING, helper.getLevel().registryAccess(), output.buildResult()));
        helper.assertValueEqual(loaded.inventory().getContainerSize(), 36, "upgraded trunk size");
        helper.assertValueEqual(loaded.inventory().getItem(35).getCount(), 17, "trunk inventory save");
        helper.assertTrue(loaded.getStay(), "trunk stay save");
        helper.succeed();
    }
}
