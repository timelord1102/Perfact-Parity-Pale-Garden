package com.perfectparitypg.datagen;

import com.perfectparitypg.world.item.ModItems;
import com.perfectparitypg.world.level.block.ModBlockFamilies;
import com.perfectparitypg.world.level.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.data.recipes.ShapedRecipeBuilder.shaped;
import static net.minecraft.data.recipes.ShapelessRecipeBuilder.shapeless;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        woodRecipes(exporter);

        shapeless(RecipeCategory.MISC, Items.GRAY_DYE)
                .requires(ModBlocks.CLOSED_EYEBLOSSOM)
                        .unlockedBy(getHasName(ModBlocks.CLOSED_EYEBLOSSOM), has(ModBlocks.CLOSED_EYEBLOSSOM))
                                .save(exporter, ResourceLocation.withDefaultNamespace("gray_dye_from_closed_eyeblossom"));

        shapeless(RecipeCategory.MISC, Items.ORANGE_DYE)
                .requires(ModBlocks.OPEN_EYEBLOSSOM)
                .unlockedBy(getHasName(ModBlocks.OPEN_EYEBLOSSOM), has(ModBlocks.OPEN_EYEBLOSSOM))
                .save(exporter, ResourceLocation.withDefaultNamespace("orange_dye_from_closed_eyeblossom"));


        shaped(RecipeCategory.MISC, ModBlocks.CREAKING_HEART)
                .define('#', ModBlocks.PALE_OAK_LOG)
                .define('O', ModBlocks.RESIN_BLOCK)
                .pattern("#")
                .pattern("O")
                .pattern("#")
                .unlockedBy(getHasName(ModBlocks.RESIN_BLOCK), has(ModBlocks.RESIN_BLOCK))
                .save(exporter, ResourceLocation.withDefaultNamespace("creaking_heart"));

        shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BLOCK)
                .define('#', ModBlocks.RESIN_CLUMP)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(ModBlocks.RESIN_CLUMP), has(ModBlocks.RESIN_CLUMP))
                .save(exporter, ResourceLocation.withDefaultNamespace("resin_block"));

        shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BRICKS)
                .define('#', ModItems.RESIN_BRICK)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(ModBlocks.RESIN_CLUMP), has(ModBlocks.RESIN_CLUMP))
                .save(exporter, ResourceLocation.withDefaultNamespace("resin_bricks"));

        shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BRICK_STAIRS, 4)
                .define('#', ModBlocks.RESIN_BRICKS)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy(getHasName(ModBlocks.RESIN_BRICKS), has(ModBlocks.RESIN_CLUMP))
                .save(exporter, ResourceLocation.withDefaultNamespace("resin_brick_stairs"));

        shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BRICK_SLAB, 6)
                .define('#', ModBlocks.RESIN_BRICKS)
                .pattern("###")
                .unlockedBy(getHasName(ModBlocks.RESIN_CLUMP), has(ModBlocks.RESIN_CLUMP))
                .save(exporter, ResourceLocation.withDefaultNamespace("resin_brick_slab"));

        shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BRICK_WALL, 6)
                .define('#', ModBlocks.RESIN_BRICKS)
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(ModBlocks.RESIN_CLUMP), has(ModBlocks.RESIN_CLUMP))
                .save(exporter, ResourceLocation.withDefaultNamespace("resin_brick_wall"));

        shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_RESIN_BRICKS)
                .define('#', ModBlocks.RESIN_BRICK_SLAB)
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(ModBlocks.RESIN_CLUMP), has(ModBlocks.RESIN_CLUMP))
                .save(exporter, ResourceLocation.withDefaultNamespace("chiseled_resin_bricks"));

        shapeless(RecipeCategory.MISC, ModBlocks.RESIN_CLUMP, 9)
                .requires(ModBlocks.RESIN_BLOCK)
                .unlockedBy(getHasName(ModBlocks.RESIN_BLOCK), has(ModBlocks.RESIN_BLOCK))
                .save(exporter, ResourceLocation.withDefaultNamespace("resin_clump_from_block"));

        smeltingResultFromBase(exporter, ModItems.RESIN_BRICK, ModItems.RESIN_CLUMP);

        stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS,
                ModBlocks.RESIN_BRICK_SLAB, ModBlocks.RESIN_BRICKS, 2);
        stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS,
                ModBlocks.RESIN_BRICK_WALL, ModBlocks.RESIN_BRICKS);
        stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS,
                ModBlocks.RESIN_BRICK_STAIRS, ModBlocks.RESIN_BRICKS);
        stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS,
                ModBlocks.CHISELED_RESIN_BRICKS, ModBlocks.RESIN_BRICKS);
    }

    private void woodRecipes(RecipeOutput exporter) {
        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PALE_OAK_PLANKS, 4)
                .requires(ModItemTagProvider.PALE_OAK_LOGS)
                .unlockedBy(ModItemTagProvider.PALE_OAK_LOGS.getTranslationKey(), has(ModItemTagProvider.PALE_OAK_LOGS))
                .save(exporter, ResourceLocation.withDefaultNamespace("pale_oak_planks"));

        shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PALE_OAK_STAIRS, 4)
                .define('#', ModBlocks.PALE_OAK_PLANKS)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy("has_planks", has(ModBlocks.PALE_OAK_PLANKS))
                .save(exporter, ResourceLocation.withDefaultNamespace("pale_oak_stairs"));

        shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PALE_OAK_SLAB, 6)
                .define('#', ModBlocks.PALE_OAK_PLANKS)
                .pattern("###")
                .unlockedBy("has_planks", has(ModBlocks.PALE_OAK_PLANKS))
                .save(exporter, ResourceLocation.withDefaultNamespace("pale_oak_slab"));

        shaped(RecipeCategory.MISC, ModBlocks.PALE_OAK_FENCE, 3)
                .define('#', ModBlocks.PALE_OAK_PLANKS)
                .define('S', ConventionalItemTags.WOODEN_RODS)
                .pattern("#S#")
                .pattern("#S#")
                .unlockedBy("has_planks", has(ModBlocks.PALE_OAK_PLANKS))
                .save(exporter, ResourceLocation.withDefaultNamespace("pale_oak_fence"));

        shaped(RecipeCategory.REDSTONE, ModBlocks.PALE_OAK_FENCE_GATE)
                .define('#', ModBlocks.PALE_OAK_PLANKS)
                .define('S', ConventionalItemTags.WOODEN_RODS)
                .pattern("S#S")
                .pattern("S#S")
                .unlockedBy("has_planks", has(ModBlocks.PALE_OAK_PLANKS))
                .save(exporter, ResourceLocation.withDefaultNamespace("pale_oak_fence_gate"));

        shaped(RecipeCategory.REDSTONE, ModItems.PALE_OAK_SIGN, 3)
                .define('#', ModBlocks.PALE_OAK_PLANKS)
                .define('S', ConventionalItemTags.WOODEN_RODS)
                .pattern("###")
                .pattern("###")
                .pattern(" S ")
                .unlockedBy("has_planks", has(ModBlocks.PALE_OAK_PLANKS))
                .save(exporter, ResourceLocation.withDefaultNamespace("pale_oak_sign"));

        shaped(RecipeCategory.REDSTONE, ModBlocks.PALE_OAK_DOOR, 3)
                .define('#', ModBlocks.PALE_OAK_PLANKS)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_planks", has(ModBlocks.PALE_OAK_PLANKS))
                .save(exporter, ResourceLocation.withDefaultNamespace("pale_oak_door"));

        shaped(RecipeCategory.REDSTONE, ModBlocks.PALE_OAK_TRAPDOOR, 2)
                .define('#', ModBlocks.PALE_OAK_PLANKS)
                .pattern("###")
                .pattern("###")
                .unlockedBy("has_planks", has(ModBlocks.PALE_OAK_PLANKS))
                .save(exporter, ResourceLocation.withDefaultNamespace("pale_oak_trapdoor"));

        shaped(RecipeCategory.DECORATIONS, ModItems.PALE_OAK_HANGING_SIGN, 6)
                .define('#', ModBlocks.STRIPPED_PALE_OAK_LOG)
                .define('C', ConventionalItemTags.CHAINS)
                .pattern("C C")
                .pattern("###")
                .pattern("###")
                .unlockedBy("has_log", has(ModBlocks.STRIPPED_PALE_OAK_LOG))
                .save(exporter, ResourceLocation.withDefaultNamespace("pale_oak_hanging_sign"));

        shaped(RecipeCategory.TRANSPORTATION, ModItems.PALE_OAK_BOAT)
                .define('#', ModBlocks.PALE_OAK_PLANKS)
                .pattern("# #")
                .pattern("###")
                .unlockedBy("in_water", insideOf(Blocks.WATER))
                .save(exporter, ResourceLocation.withDefaultNamespace("pale_oak_boat"));

        shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STRIPPED_PALE_OAK_WOOD, 3)
                .define('#', ModBlocks.STRIPPED_PALE_OAK_LOG)
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_log", has(ModBlocks.STRIPPED_PALE_OAK_LOG))
                .save(exporter, ResourceLocation.withDefaultNamespace("stripped_pale_oak_wood"));

        shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PALE_OAK_WOOD, 3)
                .define('#', ModBlocks.PALE_OAK_LOG)
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_log", has(ModBlocks.PALE_OAK_PLANKS))
                .save(exporter, ResourceLocation.withDefaultNamespace("pale_oak_wood"));

        shapeless(RecipeCategory.DECORATIONS, ModItems.PALE_OAK_CHEST_BOAT)
                .requires(ModItems.PALE_OAK_BOAT)
                .requires(ConventionalItemTags.WOODEN_CHESTS)
                .unlockedBy("has_boat", has(ModItems.PALE_OAK_BOAT))
                .save(exporter, ResourceLocation.withDefaultNamespace("pale_oak_chest_boat"));

        shapeless(RecipeCategory.REDSTONE, ModBlocks.PALE_OAK_BUTTON)
                .requires(ModBlocks.PALE_OAK_PLANKS)
                .unlockedBy("has_planks", has(ModBlocks.PALE_OAK_PLANKS))
                .save(exporter, ResourceLocation.withDefaultNamespace("pale_oak_button"));

        shaped(RecipeCategory.REDSTONE, ModBlocks.PALE_OAK_PRESSURE_PLATE)
                .define('#', ModBlocks.PALE_OAK_PLANKS)
                .pattern("##")
                .unlockedBy("has_planks", has(ModBlocks.PALE_OAK_PLANKS))
                .save(exporter, ResourceLocation.withDefaultNamespace("pale_oak_pressure_plate"));

        generateRecipes(exporter, ModBlockFamilies.PALE_OAK, FeatureFlagSet.of());
    }

    @Override
    public String getName() {
        return "Recipes";
    }
}
