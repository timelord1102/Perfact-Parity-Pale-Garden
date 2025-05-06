package com.perfectparitypg.utils;

import com.perfectparitypg.world.level.block.ModBlocks;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class ModifyLootTables {
    private static final ResourceLocation WOODLAND_MANSION_ID = BuiltInLootTables.WOODLAND_MANSION.location();

    public static void modifyVanillaLootTables() {
        LootTableEvents.MODIFY.register(((resourceKey, builder, lootTableSource, provider) -> {
            if (WOODLAND_MANSION_ID.equals(resourceKey.location())) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .conditionally(LootItemRandomChanceCondition.randomChance(44.5f).build())
                        .with(LootItem.lootTableItem(ModBlocks.RESIN_CLUMP).build())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2f, 4f)).build());
                builder.withPool(poolBuilder);
            }
        }));
    }
}
