package com.perfectparitypg.world.level.block;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {

    public static final BlockEntityType<CreakingHeartBlockEntity> CREAKING_HEART;

    public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.withDefaultNamespace(path), blockEntityType);
    }

    public static void registerBlockEntities() {

    }

    static {
        CREAKING_HEART = register("creaking_heart", BlockEntityType.Builder.of(CreakingHeartBlockEntity::new, ModBlocks.CREAKING_HEART).build());
    }
}
