package com.perfectparitypg.world.biome;

import net.minecraft.resources.ResourceLocation;
import terrablender.api.Regions;
import terrablender.api.TerraBlenderApi;

public class ModTerrablenderAPI implements TerraBlenderApi {
    @Override
    public void onTerraBlenderInitialized() {
        Regions.register(new ModOverworldRegion(ResourceLocation.fromNamespaceAndPath("perfect-parity", "overworld"), 2));
    }
}
