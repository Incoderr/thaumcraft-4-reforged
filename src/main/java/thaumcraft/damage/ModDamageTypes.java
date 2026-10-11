package thaumcraft.damage;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import thaumcraft.Thaumcraft;

public final class ModDamageTypes {
    public static final ResourceKey<DamageType> TAINT = ResourceKey.create(Registries.DAMAGE_TYPE, Thaumcraft.id("taint"));
    public static final ResourceKey<DamageType> DISSOLVE = ResourceKey.create(Registries.DAMAGE_TYPE, Thaumcraft.id("dissolve"));

    private ModDamageTypes() {
    }
}
