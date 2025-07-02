package com.perfectparitypg.event;

import com.perfectparitypg.world.level.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;

public class WanderingTraderTrades {
    public static void register() {
        TradeOfferHelper.registerWanderingTraderOffers(1, factories -> {
            factories.add((entity, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 1),
                    new ItemStack(ModBlocks.PALE_OAK_SAPLING, 1),
                    5, 1, 0.05f
            ));
            factories.add((entity, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 1),
                    new ItemStack(ModBlocks.OPEN_EYEBLOSSOM, 1),
                    5, 1, 0.05f
            ));
            factories.add((entity, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 1),
                    new ItemStack(ModBlocks.PALE_HANGING_MOSS, 1),
                    5, 1, 0.05f
            ));
            factories.add((entity, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 1),
                    new ItemStack(ModBlocks.PALE_MOSS_BLOCK, 1),
                    5, 1, 0.05f
            ));
        });
    }
}
