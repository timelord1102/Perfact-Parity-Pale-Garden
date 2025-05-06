package com.perfectparitypg.datagen;


import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.perfectparitypg.world.item.ModItems;
import com.perfectparitypg.world.level.block.ModBlockFamilies;
import com.perfectparitypg.world.level.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.Util;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.Condition;
import net.minecraft.data.models.blockstates.MultiPartGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.function.Function;


public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        createMultiface(blockStateModelGenerator, ModBlocks.RESIN_CLUMP, ModBlocks.RESIN_CLUMP.asItem());
        blockStateModelGenerator.createTrivialCube(ModBlocks.RESIN_BLOCK);
        addBlockItem(blockStateModelGenerator, "resin_block");
        BlockModelGenerators.BlockFamilyProvider resinBrickPool = blockStateModelGenerator.family(ModBlocks.RESIN_BRICKS);
        resinBrickPool.generateFor(ModBlockFamilies.RESIN_BRICKS);
        addBlockItem(blockStateModelGenerator, "resin_bricks");
        addBlockItem(blockStateModelGenerator, "chiseled_resin_bricks");

        // Pale Oak Blocks
        blockStateModelGenerator.woodProvider(ModBlocks.PALE_OAK_LOG)
                .log(ModBlocks.PALE_OAK_LOG)
                .wood(ModBlocks.PALE_OAK_WOOD);

        blockStateModelGenerator.woodProvider(ModBlocks.STRIPPED_PALE_OAK_LOG)
                .log(ModBlocks.STRIPPED_PALE_OAK_LOG)
                .wood(ModBlocks.STRIPPED_PALE_OAK_WOOD);

        blockStateModelGenerator.createTrivialCube(ModBlocks.PALE_OAK_LEAVES);
        addBlockItem(blockStateModelGenerator, "pale_oak_leaves");

        blockStateModelGenerator.createCrossBlockWithDefaultItem(ModBlocks.PALE_OAK_SAPLING, BlockModelGenerators.TintState.NOT_TINTED);
        blockStateModelGenerator.createHangingSign(ModBlocks.STRIPPED_PALE_OAK_LOG, ModBlocks.PALE_OAK_HANGING_SIGN, ModBlocks.PALE_OAK_WALL_HANGING_SIGN);

        blockStateModelGenerator.family(ModBlockFamilies.PALE_OAK.getBaseBlock()).generateFor(ModBlockFamilies.PALE_OAK);
        addBlockItem(blockStateModelGenerator, "pale_oak_planks");
        addBlockItem(blockStateModelGenerator, "pale_oak_log");
        addBlockItem(blockStateModelGenerator, "pale_oak_pressure_plate");
        addBlockItem(blockStateModelGenerator, "pale_oak_fence_gate");
        addBlockItem(blockStateModelGenerator, "pale_oak_wood");
        addBlockItem(blockStateModelGenerator, "stripped_pale_oak_wood");
        addBlockItem(blockStateModelGenerator, "stripped_pale_oak_log");
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(ModItems.PALE_OAK_BOAT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.PALE_OAK_CHEST_BOAT, ModelTemplates.FLAT_ITEM);
    }

    // Helper that allows datagen to generate cube item files if using default namespace
    public void addBlockItem(BlockModelGenerators blockStateModelGenerator, String string) {
        ResourceLocation blockModel = ResourceLocation.withDefaultNamespace("block/" + string);
        ResourceLocation itemModel = ResourceLocation.withDefaultNamespace("item/" + string);

        // Write the model: parent = block model
        blockStateModelGenerator.modelOutput.accept(itemModel, () -> {
            JsonObject json = new JsonObject();
            json.addProperty("parent", blockModel.toString());
            return json;
        });
    }

    // Recreation for createMultiface that allows separate item models
    public final void createMultiface(BlockModelGenerators blockStateModelGenerator, Block block, Item item) {
        blockStateModelGenerator.createSimpleFlatItemModel(item);
        createMultiface(blockStateModelGenerator, block);
    }

    public final void createMultiface(BlockModelGenerators blockStateModelGenerator, Block block) {
        ResourceLocation resourceLocation = ModelLocationUtils.getModelLocation(block);
        MultiPartGenerator multiPartGenerator = MultiPartGenerator.multiPart(block);
        Condition.TerminalCondition terminalCondition = (Condition.TerminalCondition) Util.make(Condition.condition(), (terminalConditionx) -> BlockModelGenerators.MULTIFACE_GENERATOR.stream().map(Pair::getFirst).forEach((booleanProperty) -> {
            if (block.defaultBlockState().hasProperty(booleanProperty)) {
                terminalConditionx.term(booleanProperty, false);
            }

        }));

        for(Pair<BooleanProperty, Function<ResourceLocation, Variant>> pair : BlockModelGenerators.MULTIFACE_GENERATOR) {
            BooleanProperty booleanProperty = (BooleanProperty)pair.getFirst();
            Function<ResourceLocation, Variant> function = (Function)pair.getSecond();
            if (block.defaultBlockState().hasProperty(booleanProperty)) {
                multiPartGenerator.with(Condition.condition().term(booleanProperty, true), (Variant)function.apply(resourceLocation));
                multiPartGenerator.with(terminalCondition, (Variant)function.apply(resourceLocation));
            }
        }

        blockStateModelGenerator.blockStateOutput.accept(multiPartGenerator);
    }
}
