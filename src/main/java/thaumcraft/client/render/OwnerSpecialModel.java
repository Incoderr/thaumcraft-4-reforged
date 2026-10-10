package thaumcraft.client.render;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4fc;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

public class OwnerSpecialModel<T> implements ItemModel {
    private final SpecialModelRenderer<T> renderer;
    private final ModelRenderProperties properties;
    private final Supplier<Vector3fc[]> extents;
    private final Matrix4fc transformation;

    public interface OwnerArgument<T> {
        @Nullable T extractArgument(ItemStack stack, @Nullable ItemOwner owner);

        boolean isAnimated(@Nullable T argument);
    }

    public OwnerSpecialModel(SpecialModelRenderer<T> renderer, ModelRenderProperties properties, Matrix4fc transformation) {
        this.renderer = renderer;
        this.properties = properties;
        this.extents = Suppliers.memoize(() -> {
            Set<Vector3fc> results = new HashSet<>();
            renderer.getExtents(results::add);
            return results.toArray(new Vector3fc[0]);
        });
        this.transformation = transformation;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(
        ItemStackRenderState output,
        ItemStack item,
        ItemModelResolver resolver,
        ItemDisplayContext displayContext,
        @Nullable ClientLevel level,
        @Nullable ItemOwner owner,
        int seed
    ) {
        output.appendModelIdentityElement(this);
        ItemStackRenderState.LayerRenderState layer = output.newLayer();
        if (item.hasFoil()) {
            ItemStackRenderState.FoilType foilType = ItemStackRenderState.FoilType.STANDARD;
            layer.setFoilType(foilType);
            output.setAnimated();
            output.appendModelIdentityElement(foilType);
        }
        T argument;
        if (renderer instanceof OwnerArgument<?> ownerArgument) {
            OwnerArgument<T> typed = (OwnerArgument<T>) ownerArgument;
            argument = typed.extractArgument(item, owner);
            if (typed.isAnimated(argument)) {
                output.setAnimated();
            }
        } else {
            argument = renderer.extractArgument(item);
        }
        layer.setExtents(extents);
        layer.setLocalTransform(transformation);
        layer.setupSpecialModel(renderer, argument);
        if (argument != null) {
            output.appendModelIdentityElement(argument);
        }
        properties.applyToLayer(layer, displayContext);
    }

    public record Unbaked(Identifier base, SpecialModelRenderer.Unbaked<?> specialModel) implements ItemModel.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Identifier.CODEC.fieldOf("base").forGetter(Unbaked::base),
                    SpecialModelRenderers.CODEC.fieldOf("model").forGetter(Unbaked::specialModel)
                )
                .apply(instance, Unbaked::new)
        );

        @Override
        public void resolveDependencies(ResolvableModel.Resolver resolver) {
            resolver.markDependency(base);
        }

        @Override
        public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
            SpecialModelRenderer<?> baked = specialModel.bake(context);
            if (baked == null) {
                return context.missingItemModel(transformation);
            }
            ModelBaker baker = context.blockModelBaker();
            ResolvedModel model = baker.getModel(base);
            return new OwnerSpecialModel<>(baked, ModelRenderProperties.fromResolvedModel(baker, model, model.getTopTextureSlots()), transformation);
        }

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
