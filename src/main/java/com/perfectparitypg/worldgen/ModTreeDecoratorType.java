package com.perfectparitypg.worldgen;

import com.perfectparitypg.mixin.TreeDecoratorTypeInvoker;
import net.minecraft.world.level.levelgen.feature.treedecorators.*;

public class ModTreeDecoratorType {
    public static final TreeDecoratorType<PaleMossDecorator> PALE_MOSS;
    public static final TreeDecoratorType<CreakingHeartDecorator> CREAKING_HEART;


    public static void registerTreeDecorators() {

    }

    static {
        PALE_MOSS = TreeDecoratorTypeInvoker.callRegister("pale_moss", PaleMossDecorator.CODEC);
        CREAKING_HEART = TreeDecoratorTypeInvoker.callRegister("creaking_heart", CreakingHeartDecorator.CODEC);
    }
}
