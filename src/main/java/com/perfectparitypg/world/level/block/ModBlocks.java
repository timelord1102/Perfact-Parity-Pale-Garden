package com.perfectparitypg.world.level.block;

import com.perfectparitypg.sound.ModSounds;
import com.terraformersmc.terraform.sign.api.block.TerraformHangingSignBlock;
import com.terraformersmc.terraform.sign.api.block.TerraformSignBlock;
import com.terraformersmc.terraform.sign.api.block.TerraformWallHangingSignBlock;
import com.terraformersmc.terraform.sign.api.block.TerraformWallSignBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class ModBlocks {
    private static final ResourceLocation PALE_OAK_SIGN_TEXTURE = ResourceLocation.withDefaultNamespace("entity/signs/pale_oak");
    private static final ResourceLocation PALE_OAK_HANGING_SIGN_TEXTURE = ResourceLocation.withDefaultNamespace("entity/signs/hanging/pale_oak");
    private static final ResourceLocation PALE_OAK_HANGING_SIGN_GUI_TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/hanging_signs/pale_oak");



    public static final Block RESIN_CLUMP;
    public static final Block RESIN_BLOCK;
    public static final Block RESIN_BRICKS;
    public static final Block RESIN_BRICK_STAIRS;
    public static final Block RESIN_BRICK_SLAB;
    public static final Block RESIN_BRICK_WALL;
    public static final Block CHISELED_RESIN_BRICKS;

    public static final Block PALE_OAK_WOOD;
    public static final Block PALE_OAK_PLANKS;
    public static final Block PALE_OAK_SAPLING;
    public static final Block PALE_OAK_LOG;
    public static final Block STRIPPED_PALE_OAK_LOG;
    public static final Block STRIPPED_PALE_OAK_WOOD;
    public static final Block PALE_OAK_LEAVES;
    public static final Block PALE_OAK_SIGN;
    public static final Block PALE_OAK_WALL_SIGN;
    public static final Block PALE_OAK_HANGING_SIGN;
    public static final Block PALE_OAK_WALL_HANGING_SIGN;
    public static final Block PALE_OAK_PRESSURE_PLATE;
    public static final Block PALE_OAK_TRAPDOOR;
    public static final Block POTTED_PALE_OAK_SAPLING;
    public static final Block PALE_OAK_BUTTON;
    public static final Block PALE_OAK_STAIRS;
    public static final Block PALE_OAK_SLAB;
    public static final Block PALE_OAK_FENCE_GATE;
    public static final Block PALE_OAK_FENCE;
    public static final Block PALE_OAK_DOOR;

    // public static final Block PALE_MOSS_BLOCK;
    // public static final Block PALE_MOSS_CARPET;
    // public static final Block PALE_HANGING_MOSS;

    public static final Block CREAKING_HEART;

    static {
        RESIN_CLUMP = register("resin_clump", new ResinClumpBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_ORANGE).replaceable().noCollission().sound(ModSounds.RESIN).ignitedByLava().pushReaction(PushReaction.DESTROY)), true);
        RESIN_BRICKS = register("resin_bricks", new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_ORANGE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().sound(ModSounds.RESIN_BRICKS).strength(1.5F, 6.0F)), true);
        RESIN_BLOCK = register("resin_block", new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_ORANGE).instrument(NoteBlockInstrument.BASEDRUM).sound(ModSounds.RESIN)), true);
        RESIN_BRICK_STAIRS = register("resin_brick_stairs", legacyStair(RESIN_BRICKS), true);
        RESIN_BRICK_SLAB = register("resin_brick_slab", new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_ORANGE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().sound(ModSounds.RESIN_BRICKS).strength(1.5F, 6.0F)), true);
        RESIN_BRICK_WALL = register("resin_brick_wall", new WallBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_ORANGE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().sound(ModSounds.RESIN_BRICKS).strength(1.5F, 6.0F)), true);
        CHISELED_RESIN_BRICKS = register("chiseled_resin_bricks",new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_ORANGE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().sound(ModSounds.RESIN_BRICKS).strength(1.5F, 6.0F)), true);

        PALE_OAK_WOOD = register("pale_oak_wood", new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()), true);
        PALE_OAK_PLANKS = register("pale_oak_planks", new Block(BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava()), true);
        PALE_OAK_SAPLING = register("pale_oak_sapling", new SaplingBlock(null, BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)), true);
        PALE_OAK_LOG = register("pale_oak_log", Blocks.log(PALE_OAK_PLANKS.defaultMapColor(), PALE_OAK_WOOD.defaultMapColor()), true);
        STRIPPED_PALE_OAK_LOG = register("stripped_pale_oak_log", Blocks.log(PALE_OAK_PLANKS.defaultMapColor(), PALE_OAK_PLANKS.defaultMapColor(), SoundType.WOOD), true);
        STRIPPED_PALE_OAK_WOOD = register("stripped_pale_oak_wood", new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(PALE_OAK_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()), true);
        PALE_OAK_LEAVES = register("pale_oak_leaves", new PaleOakLeavesBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_GREEN).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(Blocks::ocelotOrParrot).isSuffocating(Blocks::never).isViewBlocking(Blocks::never).ignitedByLava().pushReaction(PushReaction.DESTROY).isRedstoneConductor(Blocks::never)), true);
        PALE_OAK_SIGN = register("pale_oak_sign", new TerraformSignBlock(PALE_OAK_SIGN_TEXTURE, BlockBehaviour.Properties.of().mapColor(PALE_OAK_PLANKS.defaultMapColor()).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).ignitedByLava()), true);
        PALE_OAK_WALL_SIGN = register("pale_oak_wall_sign", new TerraformWallSignBlock(PALE_OAK_SIGN_TEXTURE, BlockBehaviour.Properties.of().mapColor(PALE_OAK_PLANKS.defaultMapColor()).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).ignitedByLava()), false);
        PALE_OAK_HANGING_SIGN = register("pale_oak_hanging_sign", new TerraformHangingSignBlock(PALE_OAK_HANGING_SIGN_TEXTURE, PALE_OAK_HANGING_SIGN_GUI_TEXTURE, ModWoodTypes.PALE_OAK, BlockBehaviour.Properties.of().mapColor(PALE_OAK_PLANKS.defaultMapColor()).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).ignitedByLava()),false);
        PALE_OAK_WALL_HANGING_SIGN = register("pale_oak_wall_hanging_sign", new TerraformWallHangingSignBlock(PALE_OAK_HANGING_SIGN_TEXTURE, PALE_OAK_HANGING_SIGN_GUI_TEXTURE, ModWoodTypes.PALE_OAK, BlockBehaviour.Properties.of().mapColor(PALE_OAK_PLANKS.defaultMapColor()).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).ignitedByLava()), false);
        PALE_OAK_PRESSURE_PLATE = register("pale_oak_pressure_plate", new PressurePlateBlock(ModBlockSetTypes.PALE_OAK, BlockBehaviour.Properties.of().mapColor(PALE_OAK_PLANKS.defaultMapColor()).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(0.5F).ignitedByLava().pushReaction(PushReaction.DESTROY)), true);
        PALE_OAK_TRAPDOOR = register("pale_oak_trapdoor", new TrapDoorBlock(ModBlockSetTypes.PALE_OAK, BlockBehaviour.Properties.of().mapColor(PALE_OAK_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().isValidSpawn(Blocks::never).ignitedByLava()),true);
        POTTED_PALE_OAK_SAPLING = register("potted_pale_oak_sapling", Blocks.flowerPot(PALE_OAK_SAPLING), false);
        PALE_OAK_BUTTON = register("pale_oak_button",  Blocks.woodenButton(ModBlockSetTypes.PALE_OAK), true);
        PALE_OAK_STAIRS = register("pale_oak_stairs", legacyStair(PALE_OAK_PLANKS), true);
        PALE_OAK_SLAB = register("pale_oak_slab", new SlabBlock(BlockBehaviour.Properties.of().mapColor(PALE_OAK_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava()), true);
        PALE_OAK_FENCE_GATE = register("pale_oak_fence_gate", new FenceGateBlock(ModWoodTypes.PALE_OAK, BlockBehaviour.Properties.of().mapColor(PALE_OAK_PLANKS.defaultMapColor()).forceSolidOn().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).ignitedByLava()), true);
        PALE_OAK_FENCE = register("pale_oak_fence", new FenceBlock(BlockBehaviour.Properties.of().mapColor(PALE_OAK_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).ignitedByLava().sound(SoundType.WOOD)), true);
        PALE_OAK_DOOR = register("pale_oak_door", new DoorBlock(ModBlockSetTypes.PALE_OAK, BlockBehaviour.Properties.of().mapColor(PALE_OAK_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY)), true);

        CREAKING_HEART = register("creaking_heart", new CreakingHeartBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASEDRUM).strength(10.0F).sound(ModSounds.CREAKING_HEART)), true);
    }

    private static ResourceLocation blockId(String string) {
        return ResourceLocation.withDefaultNamespace(string);
    }

    public static Block register(String string, Block block, boolean shouldRegisterItem) {
        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Properties());
            Registry.register(BuiltInRegistries.ITEM, blockId(string), blockItem.asItem());
        }
        return (Block)Registry.register(BuiltInRegistries.BLOCK, blockId(string), block);
    }

    private static Block legacyStair(Block block) {
        return new StairBlock(block.defaultBlockState(), BlockBehaviour.Properties.ofLegacyCopy(block));
    }


    public static void initialize() {
    }

}
