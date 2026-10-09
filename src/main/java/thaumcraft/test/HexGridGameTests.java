package thaumcraft.test;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import thaumcraft.Thaumcraft;
import thaumcraft.aspect.Aspect;
import thaumcraft.aspect.AspectList;
import thaumcraft.research.HexGrid;
import thaumcraft.research.ResearchItem;
import thaumcraft.research.ResearchNote;

@EventBusSubscriber(modid = Thaumcraft.MODID)
public final class HexGridGameTests {
    private static final DeferredRegister<Consumer<GameTestHelper>> FUNCTIONS = DeferredRegister.create(Registries.TEST_FUNCTION, Thaumcraft.MODID);
    private static final String[] TESTS = {"hex_grid_completion", "hex_grid_generation"};

    static {
        FUNCTIONS.register(TESTS[0], () -> HexGridGameTests::completion);
        FUNCTIONS.register(TESTS[1], () -> HexGridGameTests::generation);
    }

    private HexGridGameTests() {
    }

    public static void register(IEventBus bus) {
        FUNCTIONS.register(bus);
    }

    @SubscribeEvent
    static void registerTests(RegisterGameTestsEvent event) {
        Holder<TestEnvironmentDefinition<?>> environment = event.registerEnvironment(
            Thaumcraft.id("hex_default"),
            new TestEnvironmentDefinition.AllOf()
        );
        for (String name : TESTS) {
            ResourceKey<Consumer<GameTestHelper>> function = ResourceKey.create(Registries.TEST_FUNCTION, Thaumcraft.id(name));
            event.registerTest(
                Thaumcraft.id(name),
                new FunctionGameTestInstance(function, new TestData<>(environment, Thaumcraft.id("test/platform"), 100, 0, true))
            );
        }
    }

    private static ResearchNote.Hex hex(int q, int r, int type, Aspect aspect) {
        return new ResearchNote.Hex(q, r, type, Optional.ofNullable(aspect));
    }

    private static void completion(GameTestHelper helper) {
        List<ResearchNote.Hex> open = List.of(
            hex(0, 0, ResearchNote.Hex.GIVEN, Aspect.DARKNESS),
            hex(1, 0, ResearchNote.Hex.EMPTY, null),
            hex(2, 0, ResearchNote.Hex.GIVEN, Aspect.AIR)
        );
        ResearchNote note = new ResearchNote("TEST", 0, false, 0, open);
        helper.assertTrue(HexGrid.complete(note, aspect -> true) == null, "gap must not complete");

        List<ResearchNote.Hex> bridged = List.of(
            hex(0, 0, ResearchNote.Hex.GIVEN, Aspect.DARKNESS),
            hex(1, 0, ResearchNote.Hex.PLACED, Aspect.LIGHT),
            hex(2, 0, ResearchNote.Hex.GIVEN, Aspect.AIR),
            hex(-2, 2, ResearchNote.Hex.PLACED, Aspect.FIRE)
        );
        ResearchNote done = HexGrid.complete(new ResearchNote("TEST", 0, false, 0, bridged), aspect -> true);
        helper.assertTrue(done != null && done.complete(), "bridge must complete");
        helper.assertValueEqual(done.hexes().size(), 3, "unconnected placed hex removed");
        helper.assertTrue(HexGrid.complete(new ResearchNote("TEST", 0, false, 0, bridged), aspect -> aspect != Aspect.LIGHT) == null, "undiscovered aspect breaks the link");

        List<ResearchNote.Hex> wrong = List.of(
            hex(0, 0, ResearchNote.Hex.GIVEN, Aspect.DARKNESS),
            hex(1, 0, ResearchNote.Hex.PLACED, Aspect.WATER),
            hex(2, 0, ResearchNote.Hex.GIVEN, Aspect.AIR)
        );
        helper.assertTrue(HexGrid.complete(new ResearchNote("TEST", 0, false, 0, wrong), aspect -> true) == null, "unrelated aspect must not link");
        helper.succeed();
    }

    private static void generation(GameTestHelper helper) {
        AspectList tags = new AspectList().add(Aspect.AIR, 1).add(Aspect.FIRE, 1).add(Aspect.LIGHT, 1);
        ResearchItem item = new ResearchItem("TEST", "BASICS", tags, 0, 0, 1, Thaumcraft.id("textures/misc/home.png"));
        List<ResearchNote.Hex> hexes = HexGrid.generateHexes(item, RandomSource.create(7L));
        helper.assertValueEqual(hexes.size(), 19, "radius 2 grid");
        helper.assertValueEqual((int) hexes.stream().filter(hex -> hex.type() == ResearchNote.Hex.GIVEN).count(), 3, "given hexes");
        item.setComplexity(3);
        List<ResearchNote.Hex> complex = HexGrid.generateHexes(item, RandomSource.create(7L));
        helper.assertValueEqual(complex.size(), 61 - 6, "radius 4 grid minus blanks");
        helper.succeed();
    }
}
