package com.perfectparitypg.integration;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.mehvahdjukaar.moonlight.api.set.BlockSetAPI;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import com.perfectparitypg.world.level.block.ModBlocks;
import java.util.Optional;

public class MoonlightCompat {
    static {
        try {
            // Use the compat blocks under perfect_parity_pg namespace
            Block planks = ModBlocks.PALE_OAK_PLANKS;
            Block log = ModBlocks.PALE_OAK_LOG;
            System.out.println("[PerfectParityPG] Registering Compat Pale Oak: planks=" + planks + " (" + planks.getClass() + "), log=" + log + " (" + log.getClass() + ")");
            System.out.println("[PerfectParityPG] Planks registry name: " + BuiltInRegistries.BLOCK.getKey(planks));
            System.out.println("[PerfectParityPG] Log registry name: " + BuiltInRegistries.BLOCK.getKey(log));
            if (planks != null && log != null && planks != net.minecraft.world.level.block.Blocks.AIR && log != net.minecraft.world.level.block.Blocks.AIR) {
                WoodType paleOak = new WoodType(ResourceLocation.fromNamespaceAndPath("perfect_parity_pg", "pale_oak"), planks, log);
                paleOak.initializeChildrenBlocks();
                paleOak.initializeChildrenItems();
                paleOak.addChild("fence", ModBlocks.PALE_OAK_FENCE);
                paleOak.addChild("stairs", ModBlocks.PALE_OAK_STAIRS);
                paleOak.addChild("slab", ModBlocks.PALE_OAK_SLAB);
                paleOak.addChild("door", ModBlocks.PALE_OAK_DOOR);
                paleOak.addChild("trapdoor", ModBlocks.PALE_OAK_TRAPDOOR);
                paleOak.addChild("button", ModBlocks.PALE_OAK_BUTTON);
                paleOak.addChild("pressure_plate", ModBlocks.PALE_OAK_PRESSURE_PLATE);
                paleOak.addChild("fence_gate", ModBlocks.PALE_OAK_FENCE_GATE);
                // Add more children if you add more compat blocks
                WoodTypeRegistry.INSTANCE.registerBlockType(paleOak);
                System.out.println("[PerfectParityPG] Directly registered Compat Pale Oak WoodType with MoonlightLib, with children.");
            } else {
                System.err.println("[PerfectParityPG] Could not find compat pale_oak_planks or pale_oak_log in registry for direct registration.");
            }

            // Custom finder registration with logging
            BlockSetAPI.addBlockTypeFinder(WoodType.class, () -> {
                Block planks2 = ModBlocks.PALE_OAK_PLANKS;
                Block log2 = ModBlocks.PALE_OAK_LOG;
                System.out.println("[PerfectParityPG] Custom finder called: planks=" + planks2 + ", log=" + log2);
                if (planks2 != null && log2 != null && planks2 != net.minecraft.world.level.block.Blocks.AIR && log2 != net.minecraft.world.level.block.Blocks.AIR) {
                    WoodType paleOak2 = new WoodType(ResourceLocation.fromNamespaceAndPath("perfect_parity_pg", "pale_oak"), planks2, log2);
                    paleOak2.initializeChildrenBlocks();
                    paleOak2.initializeChildrenItems();
                    paleOak2.addChild("fence", ModBlocks.PALE_OAK_FENCE);
                    paleOak2.addChild("stairs", ModBlocks.PALE_OAK_STAIRS);
                    paleOak2.addChild("slab", ModBlocks.PALE_OAK_SLAB);
                    paleOak2.addChild("door", ModBlocks.PALE_OAK_DOOR);
                    paleOak2.addChild("trapdoor", ModBlocks.PALE_OAK_TRAPDOOR);
                    paleOak2.addChild("button", ModBlocks.PALE_OAK_BUTTON);
                    paleOak2.addChild("pressure_plate", ModBlocks.PALE_OAK_PRESSURE_PLATE);
                    paleOak2.addChild("fence_gate", ModBlocks.PALE_OAK_FENCE_GATE);
                    System.out.println("[PerfectParityPG] Custom finder returning Compat Pale Oak WoodType with children.");
                    return Optional.of(paleOak2);
                } else {
                    System.err.println("[PerfectParityPG] Custom finder: Could not find compat pale_oak_planks or pale_oak_log in registry.");
                    return Optional.empty();
                }
            });
            System.out.println("[PerfectParityPG] Registered custom finder for Compat Pale Oak WoodType.");
        } catch (Throwable t) {
            System.err.println("[PerfectParityPG] MoonlightCompat static registration failed: " + t);
        }
    }

    public static void registerPaleOak() {
        // No-op: registration is now handled in the static block
    }
}
