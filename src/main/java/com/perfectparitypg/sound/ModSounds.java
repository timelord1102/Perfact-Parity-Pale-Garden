package com.perfectparitypg.sound;

import com.perfectparitypg.PerfectParityPG;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;

public class ModSounds {

    public static final SoundEvent RESIN_BREAK = registerSoundEvent("block.resin.break");
    public static final SoundEvent RESIN_FALL = registerSoundEvent("block.resin.fall");
    public static final SoundEvent RESIN_PLACE = registerSoundEvent("block.resin.place");
    public static final SoundEvent RESIN_STEP = registerSoundEvent("block.resin.step");
    public static final SoundEvent RESIN_BRICKS_BREAK = registerSoundEvent("block.resin_bricks.break");
    public static final SoundEvent RESIN_BRICKS_FALL = registerSoundEvent("block.resin_bricks.fall");
    public static final SoundEvent RESIN_BRICKS_HIT = registerSoundEvent("block.resin_bricks.hit");
    public static final SoundEvent RESIN_BRICKS_PLACE = registerSoundEvent("block.resin_bricks.place");
    public static final SoundEvent RESIN_BRICKS_STEP = registerSoundEvent("block.resin_bricks.step");

    public static final SoundType RESIN = new SoundType(1.0F, 1.0F, RESIN_BREAK, RESIN_STEP, RESIN_PLACE, SoundEvents.EMPTY, RESIN_FALL);
    public static final SoundType RESIN_BRICKS = new SoundType(1.0F, 1.0F, ModSounds.RESIN_BRICKS_BREAK, ModSounds.RESIN_BRICKS_STEP, ModSounds.RESIN_BRICKS_PLACE, ModSounds.RESIN_BRICKS_HIT, ModSounds.RESIN_BRICKS_FALL);


    private static SoundEvent registerSoundEvent(String name) {
        ResourceLocation resourceLocation = ResourceLocation.tryBuild(ResourceLocation.DEFAULT_NAMESPACE, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, resourceLocation, SoundEvent.createVariableRangeEvent(resourceLocation));
    }


    public static void registerSounds() {
        PerfectParityPG.LOGGER.info("Registering Sounds for PerfectParity");
    }
}
