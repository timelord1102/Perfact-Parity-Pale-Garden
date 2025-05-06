package com.perfectparitypg.entity;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation CREAKING = registerLayer("creaking");

    private static ModelLayerLocation registerLayer(String name) {
        // “main” is the layer variant—most models just use “main”
        return new ModelLayerLocation(ResourceLocation.withDefaultNamespace(name), "main");
    }
}
