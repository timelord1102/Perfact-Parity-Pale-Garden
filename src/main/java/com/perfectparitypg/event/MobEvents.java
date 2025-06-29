package com.perfectparitypg.event;

import com.perfectparitypg.entity.creaking.Creaking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class MobEvents {
    private static final Set<EntityType<?>> creakingFearEntities = new HashSet<>();

    public static void register() {
        loadCreakingFearConfig();
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof PathfinderMob && creakingFearEntities.contains(entity.getType())) {
                PathfinderMob mob = (PathfinderMob) entity;
                mob.goalSelector.addGoal(0, new AvoidEntityGoal<>(mob, Creaking.class, 12.0F, 1.2D, 1.5D, new Predicate<LivingEntity>() {
                    @Override
                    public boolean test(LivingEntity livingEntity) {
                        return livingEntity instanceof Creaking && !((Creaking) livingEntity).isTearingDown();
                    }
                }));
            }
        });
    }

    private static void loadCreakingFearConfig() {
        try {
            Path configDir = FabricLoader.getInstance().getConfigDir().resolve("perfect-parity-pg");
            Files.createDirectories(configDir);
            Path configFile = configDir.resolve("creaking_fear.json");
            Gson gson = new Gson();
            if (Files.exists(configFile)) {
                String json = Files.readString(configFile, StandardCharsets.UTF_8);
                JsonObject obj = gson.fromJson(json, JsonObject.class);
                if (obj.has("entities") && obj.get("entities").isJsonArray()) {
                    JsonArray arr = obj.getAsJsonArray("entities");
                    for (int i = 0; i < arr.size(); i++) {
                        EntityType.byString(arr.get(i).getAsString()).ifPresent(creakingFearEntities::add);
                    }
                }
            } else {
                // Default config
                creakingFearEntities.add(EntityType.VINDICATOR);
                creakingFearEntities.add(EntityType.PILLAGER);
                creakingFearEntities.add(EntityType.EVOKER);
                JsonObject obj = new JsonObject();
                JsonArray arr = new JsonArray();
                arr.add("minecraft:vindicator");
                arr.add("minecraft:pillager");
                arr.add("minecraft:evoker");
                obj.add("entities", arr);
                Files.writeString(configFile, gson.toJson(obj), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            // Handle exception
        }
    }
}
