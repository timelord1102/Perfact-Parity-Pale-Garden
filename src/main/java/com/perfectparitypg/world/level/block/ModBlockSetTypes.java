package com.perfectparitypg.world.level.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class ModBlockSetTypes {
    public static final BlockSetType PALE_OAK;

    static {
        PALE_OAK = new BlockSetType(ResourceLocation.withDefaultNamespace("pale_oak").toString());
    }
}
