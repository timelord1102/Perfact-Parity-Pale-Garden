package com.perfectparitypg.datagen;


import com.google.gson.JsonArray;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;


public class ModModelProvider extends FabricModelProvider {

    private static final List<String> armor_materials = List.of("leather", "chainmail", "iron", "gold", "netherite", "diamond");
    private static final List<String> trim_materials = List.of("quartz", "iron", "netherite", "redstone", "copper", "gold", "emerald", "diamond", "lapis", "amethyst");

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

        blockStateModelGenerator.createCrossBlockWithDefaultItem(ModBlocks.OPEN_EYEBLOSSOM, BlockModelGenerators.TintState.NOT_TINTED);
        blockStateModelGenerator.createCrossBlockWithDefaultItem(ModBlocks.CLOSED_EYEBLOSSOM, BlockModelGenerators.TintState.NOT_TINTED);

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(ModItems.PALE_OAK_BOAT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.PALE_OAK_CHEST_BOAT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.RESIN_BRICK, ModelTemplates.FLAT_ITEM);


        generateTrimModels(itemModelGenerator, "resin", 0.113f);
    }

    // Helper that allows datagen to generate cube item files if using default namespace
    public void addBlockItem(BlockModelGenerators blockStateModelGenerator, String string) {
        ResourceLocation blockModel = ResourceLocation.withDefaultNamespace("block/" + string);
        ResourceLocation itemModel = ResourceLocation.withDefaultNamespace("item/" + string);

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

    public static void generateTrimModels (ItemModelGenerators itemModelGenerators, String materialName, float id) {
        List<String> armorTypes = List.of("helmet", "boots", "chestplate", "leggings");
        List<String> trimMaterials = new ArrayList<>(trim_materials.stream().toList());
        trimMaterials.add(materialName);

        for (String material : armor_materials) {
            for (String type: armorTypes) {
                ResourceLocation armorLocation = ResourceLocation.withDefaultNamespace("item/" + material + "_" + type);
                JsonObject json = new JsonObject();
                json.addProperty("parent", "minecraft:item/generated");
                JsonArray overrides = new JsonArray();
                BigDecimal baseId = new BigDecimal("0.1");
                for (String trim : trimMaterials) {
                    JsonObject model = new JsonObject();
                    if (material.equals(trim)) {
                        trim = trim+"_darker";
                    }
                    model.addProperty("model", ("minecraft:item/" + material + "_" + type + "_" + trim + "_trim"));

                    JsonObject trimType = new JsonObject();
                    if (Objects.equals(trim, materialName)) {
                        trimType.addProperty("trim_type", id);
                        trimModelHelper(itemModelGenerators, materialName, type, material);
                    } else {
                        trimType.addProperty("trim_type", baseId.floatValue());
                        baseId = baseId.add(new BigDecimal("0.1"));
                    }

                    model.add("predicate", trimType);
                    overrides.add(model);
                }
                json.add("overrides", overrides);
                JsonObject layer0 = new JsonObject();
                layer0.addProperty("layer0", "minecraft:item/" + material + "_" + type);
                if (material.equals("leather")) {
                    layer0.addProperty("layer1", "minecraft:item/leather_" + type + "_overlay");
                }

                json.add("textures", layer0);

                itemModelGenerators.output.accept(armorLocation, () ->
                        json);
            }
        }
    }

    private static void trimModelHelper(ItemModelGenerators itemModelGenerators, String materialName, String armorType, String armorMaterial) {
        ResourceLocation armorLocation = ResourceLocation.withDefaultNamespace("item/" + armorMaterial + "_" + armorType + "_" + materialName + "_trim");

        JsonObject json = new JsonObject();
        json.addProperty("parent", "minecraft:item/generated");
        JsonObject textures = new JsonObject();
        String layer = "layer1";
        textures.addProperty("layer0" , "minecraft:item/" + armorMaterial + "_" + armorType);
        if (armorMaterial.equals("leather")) {
            textures.addProperty(layer , "minecraft:item/" + armorMaterial + "_" + armorType + "_" + "overlay");
            layer = "layer2";
        }
        textures.addProperty(layer , "minecraft:trims/items/" + armorType + "_trim_" + materialName);

        json.add("textures", textures);

        itemModelGenerators.output.accept(armorLocation, () -> json);
    }
}
