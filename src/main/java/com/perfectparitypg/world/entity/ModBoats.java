package com.perfectparitypg.world.entity;

import com.perfectparitypg.world.item.ModItems;
import com.perfectparitypg.world.level.block.ModBlocks;
import com.terraformersmc.terraform.boat.api.TerraformBoatType;
import com.terraformersmc.terraform.boat.api.TerraformBoatTypeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ModBoats {
    public static final ResourceLocation PALE_OAK_BOAT_ID;
    public static final ResourceLocation PALE_OAK_CHEST_BOAT_ID;

    public static final ResourceKey<TerraformBoatType> PALE_OAK_BOAT_KEY;

    public static TerraformBoatType PALE_OAK_BOAT;

    public static TerraformBoatType register(ResourceKey<TerraformBoatType> key, TerraformBoatType type) {
        return Registry.register(TerraformBoatTypeRegistry.INSTANCE, key, type);
    }

    public static void registerBoats() {
        PALE_OAK_BOAT = register(PALE_OAK_BOAT_KEY, new TerraformBoatType.Builder()
                .item(ModItems.PALE_OAK_BOAT)
                .chestItem(ModItems.PALE_OAK_CHEST_BOAT)
                .planks(ModBlocks.PALE_OAK_PLANKS.asItem())
                .build()
        );
    }

    static {
        PALE_OAK_BOAT_ID = ResourceLocation.withDefaultNamespace("pale_oak_boat");
        PALE_OAK_CHEST_BOAT_ID = ResourceLocation.withDefaultNamespace("pale_oak_chest_boat");

        PALE_OAK_BOAT_KEY = TerraformBoatTypeRegistry.createKey(PALE_OAK_BOAT_ID);


    }
}
