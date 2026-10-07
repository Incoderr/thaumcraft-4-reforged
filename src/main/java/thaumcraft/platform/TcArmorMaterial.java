package thaumcraft.platform;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public record TcArmorMaterial(
    int durabilityMultiplier,
    int helmetDefense,
    int chestplateDefense,
    int leggingsDefense,
    int bootsDefense,
    int enchantability,
    Holder<SoundEvent> equipSound,
    float toughness,
    float knockbackResistance,
    TagKey<Item> repairItems,
    String assetName
) {
    public int defense(ArmorSlot slot) {
        return switch (slot) {
            case HELMET -> helmetDefense;
            case CHESTPLATE -> chestplateDefense;
            case LEGGINGS -> leggingsDefense;
            case BOOTS -> bootsDefense;
        };
    }
}
