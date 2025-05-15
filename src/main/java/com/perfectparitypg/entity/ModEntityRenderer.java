package com.perfectparitypg.entity;

import com.perfectparitypg.entity.monster.CreakingRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ModEntityRenderer {
    public static void registerEntityRenderers () {
        EntityRendererRegistry.register(ModEntities.CREAKING, CreakingRenderer::new);

    }
}
