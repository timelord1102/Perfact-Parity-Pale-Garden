package com.perfectparitypg.world.item;

import com.perfectparitypg.world.entity.ModBoats;
import com.perfectparitypg.world.level.block.ModBlocks;
import com.terraformersmc.terraform.boat.api.item.TerraformBoatItemHelper;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

public class ModItems {

    public static final Item RESIN_BRICK;
    public static final Item RESIN_CLUMP;
    public static final Item RESIN_BLOCK;
    public static final Item RESIN_BRICKS;
    public static final Item RESIN_BRICK_STAIRS;
    public static final Item RESIN_BRICK_SLAB;
    public static final Item RESIN_BRICK_WALL;
    public static final Item CHISELED_RESIN_BRICKS;

    public static final Item PALE_OAK_SIGN = registerItem("pale_oak_sign", new SignItem(new Item.Properties().stacksTo(16), ModBlocks.PALE_OAK_SIGN, ModBlocks.PALE_OAK_WALL_SIGN));
    public static final Item PALE_OAK_HANGING_SIGN = registerItem("pale_oak_hanging_sign", new HangingSignItem(ModBlocks.PALE_OAK_HANGING_SIGN, ModBlocks.PALE_OAK_WALL_HANGING_SIGN, new Item.Properties().stacksTo(16)));

    public static final Item PALE_OAK_BOAT = TerraformBoatItemHelper.registerBoatItem(
            ModBoats.PALE_OAK_BOAT_ID, ModBoats.PALE_OAK_BOAT_KEY, false);

    public static final Item PALE_OAK_CHEST_BOAT = TerraformBoatItemHelper.registerBoatItem(
            ModBoats.PALE_OAK_CHEST_BOAT_ID, ModBoats.PALE_OAK_BOAT_KEY, true);

    static {
        RESIN_BRICK = registerItem("resin_brick");
        RESIN_CLUMP = ModBlocks.RESIN_CLUMP.asItem();
        RESIN_BLOCK = ModBlocks.RESIN_BLOCK.asItem();
        RESIN_BRICKS = ModBlocks.RESIN_BRICKS.asItem();
        RESIN_BRICK_STAIRS = ModBlocks.RESIN_BRICK_STAIRS.asItem();
        RESIN_BRICK_SLAB = ModBlocks.RESIN_BRICK_SLAB.asItem();
        RESIN_BRICK_WALL = ModBlocks.RESIN_BRICK_WALL.asItem();
        CHISELED_RESIN_BRICKS = ModBlocks.CHISELED_RESIN_BRICKS.asItem();
    }

    public static Item registerItem(String string, Item item) {
        return registerItem(ResourceKey.create(BuiltInRegistries.ITEM.key(), ResourceLocation.withDefaultNamespace(string)), item);
    }

    public static Item registerItem(String string) {
        return registerItem( string, new Item(new Item.Properties()));
    }

    public static Item registerItem(ResourceKey<Item> resourceKey, Item item) {
        if (item instanceof BlockItem) {
            ((BlockItem)item).registerBlocks(Item.BY_BLOCK, item);
        }

        return (Item)Registry.register(BuiltInRegistries.ITEM, resourceKey, item);
    }

    public static void initialize() {
        registerFuels();
        registerCompostable();
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS)
                .register((itemGroup) -> itemGroup.addAfter(Items.MUD_BRICK_WALL, ModItems.RESIN_BRICKS));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS)
                .register((itemGroup) -> itemGroup.addAfter(ModItems.RESIN_BRICKS, ModItems.RESIN_BRICK_STAIRS));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS)
                .register((itemGroup) -> itemGroup.addAfter(ModItems.RESIN_BRICK_STAIRS, ModItems.RESIN_BRICK_SLAB));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS)
                .register((itemGroup) -> itemGroup.addAfter(ModItems.RESIN_BRICK_SLAB, ModItems.RESIN_BRICK_WALL));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS)
                .register((itemGroup) -> itemGroup.addAfter(ModItems.RESIN_BRICK_WALL, ModItems.CHISELED_RESIN_BRICKS));

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS)
                .register((itemGroup) -> itemGroup.addAfter(Items.HONEY_BLOCK, ModItems.RESIN_BLOCK));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS)
                .register((itemGroup) -> itemGroup.addAfter(Items.HONEYCOMB, ModItems.RESIN_CLUMP));

        // Pale Oak
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS)
                .register((itemGroup) -> itemGroup.addAfter(Items.CHERRY_BUTTON, ModBlocks.PALE_OAK_LOG,
                        ModBlocks.PALE_OAK_WOOD, ModBlocks.STRIPPED_PALE_OAK_LOG, ModBlocks.STRIPPED_PALE_OAK_WOOD, ModBlocks.PALE_OAK_PLANKS,
                        ModBlocks.PALE_OAK_STAIRS, ModBlocks.PALE_OAK_SLAB, ModBlocks.PALE_OAK_FENCE, ModBlocks.PALE_OAK_FENCE_GATE,
                        ModBlocks.PALE_OAK_DOOR, ModBlocks.PALE_OAK_TRAPDOOR, ModBlocks.PALE_OAK_PRESSURE_PLATE, ModBlocks.PALE_OAK_BUTTON));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS)
                .register((itemGroup) -> itemGroup.addAfter(Items.CHERRY_HANGING_SIGN, ModItems.PALE_OAK_SIGN, ModItems.PALE_OAK_HANGING_SIGN));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register((itemGroup) -> itemGroup.addAfter(Items.CHERRY_CHEST_BOAT, ModItems.PALE_OAK_BOAT, ModItems.PALE_OAK_CHEST_BOAT));
    }

    public static void registerFuels() {

    }

    public static void registerCompostable() {

    }

}
