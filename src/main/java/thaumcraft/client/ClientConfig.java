package thaumcraft.client;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue COLOR_BLIND = BUILDER
        .translation("thaumcraft.configuration.color_blind")
        .define("general.color_blind", false);
    public static final ModConfigSpec.BooleanValue SHADERS = BUILDER
        .translation("thaumcraft.configuration.shaders")
        .define("general.shaders", true);
    public static final ModConfigSpec.BooleanValue CROOKED = BUILDER
        .translation("thaumcraft.configuration.crooked")
        .define("general.crooked", true);
    public static final ModConfigSpec.BooleanValue SHOW_TAGS = BUILDER
        .translation("thaumcraft.configuration.display_aspects")
        .define("general.display_aspects", false);
    public static final ModConfigSpec.BooleanValue DIAL_BOTTOM = BUILDER
        .translation("thaumcraft.configuration.wand_dial_bottom")
        .define("general.wand_dial_bottom", false);
    public static final ModConfigSpec.IntValue NOTIFICATION_DELAY = BUILDER
        .translation("thaumcraft.configuration.notification_delay")
        .defineInRange("general.notification_delay", 5000, 500, 60000);
    public static final ModConfigSpec.IntValue NOTIFICATION_MAX = BUILDER
        .translation("thaumcraft.configuration.notification_max")
        .defineInRange("general.notification_max", 15, 1, 50);
    public static final ModConfigSpec.BooleanValue GLOWY_TAINT = BUILDER
        .translation("thaumcraft.configuration.glowing_taint")
        .define("general.glowing_taint", true);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private ClientConfig() {
    }

    private static boolean flag(ModConfigSpec.BooleanValue value) {
        return SPEC.isLoaded() ? value.getAsBoolean() : value.getDefault();
    }

    private static int number(ModConfigSpec.IntValue value) {
        return SPEC.isLoaded() ? value.getAsInt() : value.getDefault();
    }

    public static boolean colorBlind() {
        return flag(COLOR_BLIND);
    }

    public static boolean shaders() {
        return flag(SHADERS);
    }

    public static boolean crooked() {
        return flag(CROOKED);
    }

    public static boolean showTags() {
        return flag(SHOW_TAGS);
    }

    public static boolean dialBottom() {
        return flag(DIAL_BOTTOM);
    }

    public static int notificationDelay() {
        return number(NOTIFICATION_DELAY);
    }

    public static int notificationMax() {
        return number(NOTIFICATION_MAX);
    }

    public static boolean glowyTaint() {
        return flag(GLOWY_TAINT);
    }
}
