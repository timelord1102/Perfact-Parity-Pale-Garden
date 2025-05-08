package com.perfectparitypg;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerfectParityPG implements ModInitializer {
	public static final String MOD_ID = "minecraft";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

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

		LOGGER.info("1.21.4 Features Added!");


	}
}