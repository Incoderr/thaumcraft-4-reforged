package thaumcraft.client.platform;

import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;
import thaumcraft.Thaumcraft;

public final class ModRenderPipelines {
    public static final RenderPipeline ELDRITCH_PORTAL = RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
        .withLocation(Thaumcraft.id("pipeline/eldritch_portal"))
        .withCull(false)
        .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
        .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
        .build();
    public static final RenderPipeline ELDRITCH_SURFACE = RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
        .withLocation(Thaumcraft.id("pipeline/eldritch_surface"))
        .withCull(false)
        .withColorTargetState(ColorTargetState.DEFAULT)
        .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
        .build();
    public static final RenderPipeline ADDITIVE_PARTICLE = RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
        .withLocation(Thaumcraft.id("pipeline/additive_particle"))
        .withCull(false)
        .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
        .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
        .build();
    public static final RenderPipeline TRANSLUCENT_PARTICLE = RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
        .withLocation(Thaumcraft.id("pipeline/translucent_particle"))
        .withCull(false)
        .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
        .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
        .build();
    public static final RenderPipeline ADDITIVE_NO_DEPTH = RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
        .withLocation(Thaumcraft.id("pipeline/additive_no_depth"))
        .withCull(false)
        .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
        .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false))
        .build();
    public static final RenderPipeline TRANSLUCENT_NO_DEPTH = RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
        .withLocation(Thaumcraft.id("pipeline/translucent_no_depth"))
        .withCull(false)
        .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
        .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false))
        .build();
    public static final RenderPipeline ENTITY_TRANSLUCENT_NO_DEPTH = RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
        .withLocation(Thaumcraft.id("pipeline/entity_translucent_no_depth"))
        .withShaderDefine("ALPHA_CUTOUT", 0.1F)
        .withShaderDefine("PER_FACE_LIGHTING")
        .withBindGroupLayout(BindGroupLayouts.SAMPLER1)
        .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
        .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
        .withCull(false)
        .build();
    public static final RenderPipeline GUI_ADDITIVE = RenderPipeline.builder(RenderPipelines.GUI_SNIPPET)
        .withLocation(Thaumcraft.id("pipeline/gui_additive"))
        .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
        .build();

    public static final RenderPipeline GUI_TEXTURED_ADDITIVE = RenderPipeline.builder(RenderPipelines.GUI_TEXTURED_SNIPPET)
        .withLocation(Thaumcraft.id("pipeline/gui_textured_additive"))
        .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
        .build();

    private ModRenderPipelines() {
    }
}
