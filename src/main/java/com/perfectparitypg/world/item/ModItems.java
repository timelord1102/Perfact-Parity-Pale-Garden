package com.perfectparitypg.world.item;

import com.perfectparitypg.entity.ModEntities;
import com.perfectparitypg.world.entity.ModBoats;
import com.perfectparitypg.world.level.block.ModBlocks;
import com.terraformersmc.terraform.boat.api.item.TerraformBoatItemHelper;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
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
    public static final Item CREAKING_SPAWN_EGG;

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
        CREAKING_SPAWN_EGG = registerItem("creaking_spawn_egg", new SpawnEggItem(ModEntities.CREAKING, 0x5F5F5F, 0xFC7812, new Item.Properties()));

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
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS)
                .register((itemGroup) -> itemGroup.addAfter(Items.NETHER_BRICK, ModItems.RESIN_BRICK));

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
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS)
                .register((itemGroup) -> itemGroup.addAfter(Items.CHERRY_SAPLING, ModBlocks.PALE_OAK_SAPLING));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS)
                .register((itemGroup) -> itemGroup.addAfter(Items.CHERRY_LOG, ModBlocks.PALE_OAK_LOG));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS)
                .register((itemGroup) -> itemGroup.addAfter(Items.CHERRY_LEAVES, ModBlocks.PALE_OAK_LEAVES));

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS)
                .register((itemGroup) -> itemGroup.addAfter(Items.TRIAL_SPAWNER, ModBlocks.CREAKING_HEART));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS)
                .register((itemGroup) -> itemGroup.addAfter(Items.COW_SPAWN_EGG, ModItems.CREAKING_SPAWN_EGG));

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS)
                .register((itemGroup) -> itemGroup.addAfter(Items.MOSS_CARPET, ModBlocks.PALE_MOSS_BLOCK, ModBlocks.PALE_MOSS_CARPET, ModBlocks.PALE_HANGING_MOSS));

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS)
                .register((itemGroup) -> itemGroup.addAfter(Items.TORCHFLOWER, ModBlocks.CLOSED_EYEBLOSSOM, ModBlocks.OPEN_EYEBLOSSOM));

    }

    public static void registerFuels() {

    }

    public static void registerCompostable() {
        CompostingChanceRegistry.INSTANCE.add(ModBlocks.CLOSED_EYEBLOSSOM, 0.65f);
        CompostingChanceRegistry.INSTANCE.add(ModBlocks.OPEN_EYEBLOSSOM, 0.3f);
        CompostingChanceRegistry.INSTANCE.add(ModBlocks.PALE_MOSS_BLOCK, 0.65f);
        CompostingChanceRegistry.INSTANCE.add(ModBlocks.PALE_MOSS_BLOCK, 0.3f);
        CompostingChanceRegistry.INSTANCE.add(ModBlocks.PALE_HANGING_MOSS, 0.3f);
        CompostingChanceRegistry.INSTANCE.add(ModBlocks.PALE_MOSS_CARPET, 0.3f);
        CompostingChanceRegistry.INSTANCE.add(ModBlocks.PALE_OAK_LEAVES, 0.3f);
    }

}
