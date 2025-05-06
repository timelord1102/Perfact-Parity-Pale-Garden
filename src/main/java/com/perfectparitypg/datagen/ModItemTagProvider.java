package com.perfectparitypg.datagen;

import com.perfectparitypg.world.item.ModItems;
import com.perfectparitypg.world.level.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public static final TagKey<Item> PALE_OAK_LOGS = TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace("pale_oak_logs"));

    private static TagKey<Item> create(String string) {
        return TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace(string));
    }

    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        addVanillaTags();

        getOrCreateTagBuilder(PALE_OAK_LOGS).add(
                ModBlocks.PALE_OAK_LOG.asItem(),
                ModBlocks.STRIPPED_PALE_OAK_LOG.asItem(),
                ModBlocks.PALE_OAK_WOOD.asItem(),
                ModBlocks.STRIPPED_PALE_OAK_WOOD.asItem()
        );

        getOrCreateTagBuilder(ItemTags.SLABS).add(ModBlocks.RESIN_BRICK_SLAB.asItem());
        getOrCreateTagBuilder(ItemTags.WALLS).add(ModBlocks.RESIN_BRICK_WALL.asItem());
        getOrCreateTagBuilder(ItemTags.STAIRS).add(ModBlocks.RESIN_BRICK_STAIRS.asItem());
        getOrCreateTagBuilder(ItemTags.TRIM_MATERIALS).add(ModItems.RESIN_BRICK);

        getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN).addTag(PALE_OAK_LOGS);
        getOrCreateTagBuilder(ItemTags.WOODEN_BUTTONS).add(ModBlocks.PALE_OAK_BUTTON.asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_DOORS).add(ModBlocks.PALE_OAK_DOOR.asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_FENCES).add(ModBlocks.PALE_OAK_FENCE.asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_SLABS).add(ModBlocks.PALE_OAK_SLAB.asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_STAIRS).add(ModBlocks.PALE_OAK_STAIRS.asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_PRESSURE_PLATES).add(ModBlocks.PALE_OAK_PRESSURE_PLATE.asItem());
        getOrCreateTagBuilder(ItemTags.FENCE_GATES).add(ModBlocks.PALE_OAK_FENCE_GATE.asItem());
        getOrCreateTagBuilder(ItemTags.SIGNS).add(ModItems.PALE_OAK_SIGN);
        getOrCreateTagBuilder(ItemTags.HANGING_SIGNS).add(ModItems.PALE_OAK_HANGING_SIGN);
        getOrCreateTagBuilder(ItemTags.BOATS).add(ModItems.PALE_OAK_BOAT);
        getOrCreateTagBuilder(ItemTags.CHEST_BOATS).add(ModItems.PALE_OAK_CHEST_BOAT);
        getOrCreateTagBuilder(ItemTags.LEAVES).add(ModBlocks.PALE_OAK_LEAVES.asItem());

        getOrCreateTagBuilder(ItemTags.PLANKS).add(ModBlocks.PALE_OAK_PLANKS.asItem());
    }

    protected void addVanillaTags() {
    }
}
