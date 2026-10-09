package thaumcraft.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import thaumcraft.Thaumcraft;

public final class ModSounds {
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, Thaumcraft.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> URNBREAK = register("urnbreak");
    public static final DeferredHolder<SoundEvent, SoundEvent> COINS = register("coins");
    public static final DeferredHolder<SoundEvent, SoundEvent> WAND = register("wand");
    public static final DeferredHolder<SoundEvent, SoundEvent> WANDFAIL = register("wandfail");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZAP = register("zap");
    public static final DeferredHolder<SoundEvent, SoundEvent> CRAFTFAIL = register("craftfail");
    public static final DeferredHolder<SoundEvent, SoundEvent> CRYSTAL = register("crystal");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAMERATICKS = register("cameraticks");
    public static final DeferredHolder<SoundEvent, SoundEvent> JAR = register("jar");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAMERACLACK = register("cameraclack");
    public static final DeferredHolder<SoundEvent, SoundEvent> LEARN = register("learn");
    public static final DeferredHolder<SoundEvent, SoundEvent> WRITE = register("write");
    public static final DeferredHolder<SoundEvent, SoundEvent> ERASE = register("erase");
    public static final DeferredHolder<SoundEvent, SoundEvent> PAGE = register("page");
    public static final DeferredHolder<SoundEvent, SoundEvent> WHISPERS = register("whispers");

    private ModSounds() {
    }

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(Thaumcraft.id(name)));
    }

    public static void register(IEventBus bus) {
        SOUND_EVENTS.register(bus);
    }
}
