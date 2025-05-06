package com.perfectparitypg.entity;

import com.perfectparitypg.entity.creaking.Creaking;
import com.perfectparitypg.entity.monster.CreakingRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {

    private static final ResourceLocation CREAKING_ID = ResourceLocation.withDefaultNamespace("creaking");
    private static final ResourceKey<EntityType<?>> CREAKING_KEY = ResourceKey.create(Registries.ENTITY_TYPE, CREAKING_ID);
    private static final EntityType<Creaking> CREAKING_BUILT = EntityType.Builder.of(Creaking::new, MobCategory.CREATURE).sized(0.9f,2.7f).build();
    public static final EntityType<Creaking> CREAKING = Registry.register(BuiltInRegistries.ENTITY_TYPE, CREAKING_KEY, CREAKING_BUILT);

    public static void registerModEntities() {
        FabricDefaultAttributeRegistry.register(CREAKING, Creaking.createAttributes());
        EntityRendererRegistry.register(ModEntities.CREAKING, CreakingRenderer::new);}
}
