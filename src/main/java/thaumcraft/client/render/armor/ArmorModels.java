package thaumcraft.client.render.armor;

import java.util.EnumMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jspecify.annotations.Nullable;
import thaumcraft.Thaumcraft;
import thaumcraft.registry.ModItems;

@EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class ArmorModels {
    private static final Map<ArmorKind, Map<EquipmentSlot, TcArmorModel>> MODELS = new EnumMap<>(ArmorKind.class);

    private ArmorModels() {
    }

    @SubscribeEvent
    static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        for (ArmorKind kind : ArmorKind.values()) {
            event.registerLayerDefinition(kind.outer(), kind::createOuter);
            event.registerLayerDefinition(kind.inner(), kind::createInner);
        }
    }

    @SubscribeEvent
    static void addLayers(EntityRenderersEvent.AddLayers event) {
        bake(event.getEntityModels());
    }

    private static void bake(EntityModelSet models) {
        MODELS.clear();
        for (ArmorKind kind : ArmorKind.values()) {
            Map<EquipmentSlot, TcArmorModel> bySlot = new EnumMap<>(EquipmentSlot.class);
            bySlot.put(EquipmentSlot.HEAD, new TcArmorModel(models.bakeLayer(kind.inner()), kind, EquipmentSlot.HEAD));
            bySlot.put(EquipmentSlot.CHEST, new TcArmorModel(models.bakeLayer(kind.outer()), kind, EquipmentSlot.CHEST));
            bySlot.put(EquipmentSlot.LEGS, new TcArmorModel(models.bakeLayer(kind.inner()), kind, EquipmentSlot.LEGS));
            MODELS.put(kind, bySlot);
        }
    }

    private static @Nullable TcArmorModel get(ArmorKind kind, ItemStack stack) {
        if (MODELS.isEmpty()) {
            bake(Minecraft.getInstance().getEntityModels());
        }
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        return equippable == null ? null : MODELS.get(kind).get(equippable.slot());
    }

    private static IClientItemExtensions extensions(ArmorKind kind) {
        return new IClientItemExtensions() {
            @Override
            public Model getHumanoidArmorModel(ItemStack stack, EquipmentClientInfo.LayerType layerType, Model original) {
                if (layerType == EquipmentClientInfo.LayerType.HUMANOID_BABY) {
                    return original;
                }
                TcArmorModel model = get(kind, stack);
                return model == null ? original : model;
            }
        };
    }

    @SubscribeEvent
    static void registerExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(extensions(ArmorKind.FORTRESS), ModItems.FORTRESS_HELMET.get(), ModItems.FORTRESS_CHESTPLATE.get(), ModItems.FORTRESS_LEGGINGS.get());
        event.registerItem(
            extensions(ArmorKind.ROBE),
            ModItems.CRIMSON_CULT_HOOD.get(),
            ModItems.CRIMSON_CULT_ROBE.get(),
            ModItems.CRIMSON_CULT_LEGGINGS.get(),
            ModItems.VOID_ROBE_HELMET.get(),
            ModItems.VOID_ROBE_CHESTPLATE.get(),
            ModItems.VOID_ROBE_LEGGINGS.get()
        );
        event.registerItem(extensions(ArmorKind.KNIGHT), ModItems.CRIMSON_CULT_HELM.get(), ModItems.CRIMSON_CULT_CHESTPLATE.get(), ModItems.CRIMSON_CULT_GREAVES.get());
        event.registerItem(
            extensions(ArmorKind.LEADER),
            ModItems.CRIMSON_PRAETOR_HELM.get(),
            ModItems.CRIMSON_PRAETOR_CHESTPLATE.get(),
            ModItems.CRIMSON_PRAETOR_GREAVES.get()
        );
    }
}
