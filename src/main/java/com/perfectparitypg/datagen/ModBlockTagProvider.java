package com.perfectparitypg.datagen;

import com.perfectparitypg.world.level.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;


public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public static final TagKey<Block> PALE_OAK_LOGS = create("pale_oak_logs");
    private static TagKey<Block> create(String string) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace(string));
    }

    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        addVanillaTags();
        // Resin Clump
        getOrCreateTagBuilder(BlockTags.COMBINATION_STEP_SOUND_BLOCKS).add(ModBlocks.RESIN_CLUMP);
        // Resin bricks
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.RESIN_BRICKS,
                ModBlocks.CHISELED_RESIN_BRICKS, ModBlocks.RESIN_BRICK_SLAB, ModBlocks.RESIN_BLOCK,
                ModBlocks.RESIN_BRICK_STAIRS, ModBlocks.RESIN_BRICK_WALL);
        getOrCreateTagBuilder(BlockTags.WALLS).add(ModBlocks.RESIN_BRICK_WALL);
        getOrCreateTagBuilder(BlockTags.SLABS).add(ModBlocks.RESIN_BRICK_SLAB);
        getOrCreateTagBuilder(BlockTags.STAIRS).add(ModBlocks.RESIN_BRICK_STAIRS);

        getOrCreateTagBuilder(PALE_OAK_LOGS).add(ModBlocks.PALE_OAK_LOG, ModBlocks.STRIPPED_PALE_OAK_LOG,
                ModBlocks.PALE_OAK_WOOD, ModBlocks.STRIPPED_PALE_OAK_WOOD);
        getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN).addTag(PALE_OAK_LOGS);

        getOrCreateTagBuilder(BlockTags.LEAVES).add(ModBlocks.PALE_OAK_LEAVES);
        getOrCreateTagBuilder(BlockTags.SAPLINGS).add(ModBlocks.PALE_OAK_SAPLING);
        getOrCreateTagBuilder(BlockTags.WOODEN_BUTTONS).add(ModBlocks.PALE_OAK_BUTTON);
        getOrCreateTagBuilder(BlockTags.WOODEN_DOORS).add(ModBlocks.PALE_OAK_DOOR);
        getOrCreateTagBuilder(BlockTags.WOODEN_FENCES).add(ModBlocks.PALE_OAK_FENCE);
        getOrCreateTagBuilder(BlockTags.WOODEN_SLABS).add(ModBlocks.PALE_OAK_SLAB);
        getOrCreateTagBuilder(BlockTags.WOODEN_TRAPDOORS).add(ModBlocks.PALE_OAK_TRAPDOOR);
        getOrCreateTagBuilder(BlockTags.WOODEN_PRESSURE_PLATES).add(ModBlocks.PALE_OAK_PRESSURE_PLATE);
        getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS).add(ModBlocks.PALE_OAK_STAIRS);
        getOrCreateTagBuilder(BlockTags.STANDING_SIGNS).add(ModBlocks.PALE_OAK_SIGN);
        getOrCreateTagBuilder(BlockTags.WALL_SIGNS).add(ModBlocks.PALE_OAK_WALL_SIGN);
        getOrCreateTagBuilder(BlockTags.CEILING_HANGING_SIGNS).add(ModBlocks.PALE_OAK_HANGING_SIGN);
        getOrCreateTagBuilder(BlockTags.WALL_HANGING_SIGNS).add(ModBlocks.PALE_OAK_WALL_HANGING_SIGN);
        getOrCreateTagBuilder(BlockTags.FENCE_GATES).add(ModBlocks.PALE_OAK_FENCE_GATE);
        getOrCreateTagBuilder(BlockTags.PLANKS).add(ModBlocks.PALE_OAK_PLANKS);
    }

    protected void addVanillaTags() {
    }
}
