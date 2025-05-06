package com.perfectparitypg.world.level.block;

import com.google.common.collect.Maps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class ModBlockFamilies {
    private static final Map<Block, BlockFamily> MAP = Maps.newHashMap();
    public static final BlockFamily RESIN_BRICKS;
    public static final BlockFamily PALE_OAK;

    public static BlockFamily.Builder familyBuilder(Block block) {

        BlockFamily.Builder builder = new BlockFamily.Builder(block);
        BlockFamily blockFamily = (BlockFamily)MAP.put(block, builder.getFamily());
        if (blockFamily != null) {
            throw new IllegalStateException("Duplicate family definition for " + String.valueOf(BuiltInRegistries.BLOCK.getKey(block)));
        } else {
            return builder;
        }
    }

    public static void createBlockFamilies() {

    }

    static {
        RESIN_BRICKS = familyBuilder(ModBlocks.RESIN_BRICKS).wall(ModBlocks.RESIN_BRICK_WALL).stairs(ModBlocks.RESIN_BRICK_STAIRS).slab(ModBlocks.RESIN_BRICK_SLAB).chiseled(ModBlocks.CHISELED_RESIN_BRICKS).getFamily();
        PALE_OAK = familyBuilder(ModBlocks.PALE_OAK_PLANKS).button(ModBlocks.PALE_OAK_BUTTON).fence(ModBlocks.PALE_OAK_FENCE).fenceGate(ModBlocks.PALE_OAK_FENCE_GATE).pressurePlate(ModBlocks.PALE_OAK_PRESSURE_PLATE).sign(ModBlocks.PALE_OAK_SIGN, ModBlocks.PALE_OAK_WALL_SIGN).slab(ModBlocks.PALE_OAK_SLAB).stairs(ModBlocks.PALE_OAK_STAIRS).door(ModBlocks.PALE_OAK_DOOR).trapdoor(ModBlocks.PALE_OAK_TRAPDOOR).recipeGroupPrefix("wooden").recipeUnlockedBy("has_planks").getFamily();
    }
}
