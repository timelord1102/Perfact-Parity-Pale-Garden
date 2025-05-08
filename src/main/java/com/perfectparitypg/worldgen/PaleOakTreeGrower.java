package com.perfectparitypg.worldgen;

import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class PaleOakTreeGrower {
    public static final TreeGrower PALE_OAK = new TreeGrower("pale_oak", Optional.of(ModTreeFeatures.PALE_OAK), Optional.empty(), Optional.empty());


    public static void registerTreeGrower () {

    }
}
