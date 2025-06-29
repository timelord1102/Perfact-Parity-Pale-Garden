package com.perfectparitypg;

import com.perfectparitypg.config.Config;
import com.perfectparitypg.entity.ModEntities;
import com.perfectparitypg.particle.ModParticles;
import com.perfectparitypg.sound.ModSounds;
import com.perfectparitypg.utils.ModifyLootTables;
import com.perfectparitypg.world.entity.ModBoats;
import com.perfectparitypg.world.item.ModItems;
import com.perfectparitypg.world.level.block.ModBlockFamilies;
import com.perfectparitypg.world.level.block.ModBlockEntities;
import com.perfectparitypg.world.level.block.ModBlocks;
import com.perfectparitypg.worldgen.ModTreeDecoratorType;
import com.perfectparitypg.worldgen.ModVegetationFeatures;
import com.perfectparitypg.worldgen.PaleOakTreeGrower;
import net.fabricmc.api.ModInitializer;
import com.perfectparitypg.event.MobEvents;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.loader.api.FabricLoader;

public class PerfectParityPG implements ModInitializer {
	public static final String MOD_ID = "minecraft";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Config CONFIG = new Config("perfect-parity-pg");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		MobEvents.register();
		ModBlocks.initialize();
		ModItems.initialize();
		ModSounds.registerSounds();
		ModifyLootTables.modifyVanillaLootTables();
		ModBlockFamilies.createBlockFamilies();
		ModBoats.registerBoats();
		ModEntities.registerModEntities();
		ModBlockEntities.registerBlockEntities();
		ModParticles.registerParticles();
		ModVegetationFeatures.registerVegetationFeatures();
		ModTreeDecoratorType.registerTreeDecorators();
		PaleOakTreeGrower.registerTreeGrower();

		StrippableBlockRegistry.register(ModBlocks.PALE_OAK_LOG, ModBlocks.STRIPPED_PALE_OAK_LOG);
		StrippableBlockRegistry.register(ModBlocks.PALE_OAK_WOOD, ModBlocks.STRIPPED_PALE_OAK_WOOD);

		// Register flammability for Pale Oak blocks
		FlammableBlockRegistry registry = FlammableBlockRegistry.getDefaultInstance();
		registry.add(ModBlocks.PALE_OAK_LOG, 5, 5);
		registry.add(ModBlocks.STRIPPED_PALE_OAK_LOG, 5, 5);
		registry.add(ModBlocks.PALE_OAK_WOOD, 5, 5);
		registry.add(ModBlocks.STRIPPED_PALE_OAK_WOOD, 5, 5);
		registry.add(ModBlocks.PALE_OAK_PLANKS, 5, 20);
		registry.add(ModBlocks.PALE_OAK_LEAVES, 30, 60);
		registry.add(ModBlocks.PALE_OAK_SLAB, 5, 20);
		registry.add(ModBlocks.PALE_OAK_STAIRS, 5, 20);
		registry.add(ModBlocks.PALE_OAK_FENCE, 5, 20);
		registry.add(ModBlocks.PALE_OAK_FENCE_GATE, 5, 20);
		registry.add(ModBlocks.PALE_HANGING_MOSS, 5, 100);
		registry.add(ModBlocks.PALE_MOSS_BLOCK, 5, 20);
		registry.add(ModBlocks.PALE_MOSS_CARPET, 5, 100);

		// Optional Moonlight/Every Compat integration (reflection, truly optional)
		if (FabricLoader.getInstance().isModLoaded("moonlight")) {
			try {
				Class<?> compat = Class.forName("com.perfectparitypg.integration.MoonlightCompat");
				compat.getMethod("registerPaleOak").invoke(null);
				LOGGER.info("Moonlight detected: Registered Pale Oak for Every Compat auto-detection.");
			} catch (Throwable t) {
				LOGGER.warn("Moonlight detected but integration failed: " + t);
			}
		}

		LOGGER.info("1.21.4 Features Added!");
	}
}
