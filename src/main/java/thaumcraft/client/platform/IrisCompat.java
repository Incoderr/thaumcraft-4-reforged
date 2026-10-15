package thaumcraft.client.platform;

import net.irisshaders.iris.api.v0.IrisApi;
import net.irisshaders.iris.api.v0.IrisProgram;

public final class IrisCompat {
    private IrisCompat() {
    }

    public static void register() {
        IrisApi iris = IrisApi.getInstance();
        iris.assignPipeline(ModRenderPipelines.ADDITIVE_PARTICLE, IrisProgram.PARTICLES_TRANSLUCENT);
        iris.assignPipeline(ModRenderPipelines.TRANSLUCENT_PARTICLE, IrisProgram.PARTICLES_TRANSLUCENT);
        iris.assignPipeline(ModRenderPipelines.ADDITIVE_NO_DEPTH, IrisProgram.PARTICLES_TRANSLUCENT);
        iris.assignPipeline(ModRenderPipelines.TRANSLUCENT_NO_DEPTH, IrisProgram.PARTICLES_TRANSLUCENT);
        iris.assignPipeline(ModRenderPipelines.ENTITY_TRANSLUCENT_NO_DEPTH, IrisProgram.ENTITIES_TRANSLUCENT);
    }
}
