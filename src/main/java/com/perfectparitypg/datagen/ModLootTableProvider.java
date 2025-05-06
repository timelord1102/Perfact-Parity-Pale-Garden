package com.perfectparitypg.datagen;

import com.perfectparitypg.world.item.ModItems;
import com.perfectparitypg.world.level.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {

    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(ModBlocks.RESIN_CLUMP);
        dropSelf(ModBlocks.RESIN_BLOCK);
        dropSelf(ModBlocks.RESIN_BRICKS);
        dropSelf(ModBlocks.RESIN_BRICK_STAIRS);
        dropSelf(ModBlocks.RESIN_BRICK_WALL);
        dropSelf(ModBlocks.CHISELED_RESIN_BRICKS);
        dropSelf(ModBlocks.RESIN_BLOCK);
        add(ModBlocks.RESIN_BRICK_SLAB, createSlabItemTable(ModBlocks.RESIN_BRICK_SLAB));

        dropSelf(ModBlocks.PALE_OAK_LOG);
        dropSelf(ModBlocks.STRIPPED_PALE_OAK_LOG);
        dropSelf(ModBlocks.PALE_OAK_WOOD);
        dropSelf(ModBlocks.STRIPPED_PALE_OAK_WOOD);
        createLeavesDrops(ModBlocks.PALE_OAK_LEAVES, ModBlocks.PALE_OAK_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES);
        dropSelf(ModBlocks.PALE_OAK_SAPLING);
        dropSelf(ModBlocks.PALE_OAK_PLANKS);
        dropSelf(ModBlocks.PALE_OAK_SLAB);
        dropSelf(ModBlocks.PALE_OAK_STAIRS);
        dropSelf(ModBlocks.PALE_OAK_FENCE);
        dropSelf(ModBlocks.PALE_OAK_FENCE_GATE);
        createDoorTable(ModBlocks.PALE_OAK_DOOR);
        dropSelf(ModBlocks.PALE_OAK_TRAPDOOR);
        dropSelf(ModBlocks.PALE_OAK_BUTTON);
        dropSelf(ModBlocks.PALE_OAK_PRESSURE_PLATE);
        createPotFlowerItemTable(ModBlocks.POTTED_PALE_OAK_SAPLING);
        dropOther(ModBlocks.PALE_OAK_SIGN, ModItems.PALE_OAK_SIGN);
        dropOther(ModBlocks.PALE_OAK_WALL_SIGN, ModItems.PALE_OAK_SIGN);
        dropOther(ModBlocks.PALE_OAK_HANGING_SIGN, ModItems.PALE_OAK_HANGING_SIGN);
        dropOther(ModBlocks.PALE_OAK_WALL_HANGING_SIGN, ModItems.PALE_OAK_HANGING_SIGN);
    }


}
