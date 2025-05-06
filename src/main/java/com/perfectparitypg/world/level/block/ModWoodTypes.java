package com.perfectparitypg.world.level.block;

import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ModWoodTypes {

    private static final WoodTypeBuilder woodTypeBuilder = new WoodTypeBuilder();
    public static final WoodType PALE_OAK;

    static {
        PALE_OAK = woodTypeBuilder.register(ResourceLocation.withDefaultNamespace("pale_oak"), ModBlockSetTypes.PALE_OAK);
    }
}
