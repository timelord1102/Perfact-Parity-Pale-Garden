package com.perfectparitypg.sound;

import com.perfectparitypg.PerfectParityPG;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
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

    public static final SoundEvent CREAKING_HEART_BREAK = registerSoundEvent("block.creaking_heart.break");
    public static final SoundEvent CREAKING_HEART_FALL = registerSoundEvent("block.creaking_heart.fall");
    public static final SoundEvent CREAKING_HEART_HIT = registerSoundEvent("block.creaking_heart.hit");
    public static final SoundEvent CREAKING_HEART_HURT = registerSoundEvent("block.creaking_heart.hurt");
    public static final SoundEvent CREAKING_HEART_PLACE = registerSoundEvent("block.creaking_heart.place");
    public static final SoundEvent CREAKING_HEART_STEP = registerSoundEvent("block.creaking_heart.step");
    public static final SoundEvent CREAKING_HEART_IDLE = registerSoundEvent("block.creaking_heart.idle");
    public static final SoundEvent CREAKING_HEART_SPAWN = registerSoundEvent("block.creaking_heart.spawn");

    public static final SoundEvent CREAKING_AMBIENT = registerSoundEvent("entity.creaking.ambient");
    public static final SoundEvent CREAKING_ACTIVATE = registerSoundEvent("entity.creaking.activate");
    public static final SoundEvent CREAKING_DEACTIVATE = registerSoundEvent("entity.creaking.deactivate");
    public static final SoundEvent CREAKING_ATTACK = registerSoundEvent("entity.creaking.attack");
    public static final SoundEvent CREAKING_DEATH = registerSoundEvent("entity.creaking.death");
    public static final SoundEvent CREAKING_STEP = registerSoundEvent("entity.creaking.step");
    public static final SoundEvent CREAKING_FREEZE = registerSoundEvent("entity.creaking.freeze");
    public static final SoundEvent CREAKING_UNFREEZE = registerSoundEvent("entity.creaking.unfreeze");
    public static final SoundEvent CREAKING_SPAWN = registerSoundEvent("entity.creaking.spawn");
    public static final SoundEvent CREAKING_SWAY = registerSoundEvent("entity.creaking.sway");
    public static final SoundEvent CREAKING_TWITCH = registerSoundEvent("entity.creaking.twitch");
    public static final SoundEvent EYEBLOSSOM_OPEN_LONG = registerSoundEvent("block.eyeblossom.open_long");
    public static final SoundEvent EYEBLOSSOM_OPEN = registerSoundEvent("block.eyeblossom.open");
    public static final SoundEvent EYEBLOSSOM_CLOSE_LONG = registerSoundEvent("block.eyeblossom.close_long");
    public static final SoundEvent EYEBLOSSOM_CLOSE = registerSoundEvent("block.eyeblossom.close");
    public static final SoundEvent EYEBLOSSOM_IDLE = registerSoundEvent("block.eyeblossom.idle");

    public static final Holder.Reference<SoundEvent> NONE = registerForHolder("music.none");
    public static final Music NO_MUSIC = new Music(ModSounds.NONE, 0, 0, true);

    public static final SoundEvent PALE_HANGING_MOSS_IDLE = registerSoundEvent("block.pale_hanging_moss.idle");

    public static final SoundType RESIN = new SoundType(1.0F, 1.0F, RESIN_BREAK, RESIN_STEP, RESIN_PLACE, SoundEvents.EMPTY, RESIN_FALL);
    public static final SoundType RESIN_BRICKS = new SoundType(1.0F, 1.0F, ModSounds.RESIN_BRICKS_BREAK, ModSounds.RESIN_BRICKS_STEP, ModSounds.RESIN_BRICKS_PLACE, ModSounds.RESIN_BRICKS_HIT, ModSounds.RESIN_BRICKS_FALL);
    public static final SoundType CREAKING_HEART = new SoundType(1.0F, 1.0F, ModSounds.CREAKING_HEART_BREAK, ModSounds.CREAKING_HEART_STEP, ModSounds.CREAKING_HEART_PLACE, ModSounds.CREAKING_HEART_HIT, ModSounds.CREAKING_HEART_FALL);

    private static SoundEvent registerSoundEvent(String name) {
        ResourceLocation resourceLocation = ResourceLocation.tryBuild(ResourceLocation.DEFAULT_NAMESPACE, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, resourceLocation, SoundEvent.createVariableRangeEvent(resourceLocation));
    }

    private static Holder.Reference<SoundEvent> registerForHolder(String string) {
        return registerForHolder(ResourceLocation.withDefaultNamespace(string));
    }

    private static Holder.Reference<SoundEvent> registerForHolder(ResourceLocation resourceLocation) {
        return registerForHolder(resourceLocation, resourceLocation);
    }

    private static Holder.Reference<SoundEvent> registerForHolder(ResourceLocation resourceLocation, ResourceLocation resourceLocation2) {
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, resourceLocation, SoundEvent.createVariableRangeEvent(resourceLocation2));
    }

    public static void registerSounds() {
        PerfectParityPG.LOGGER.info("Registering Sounds for PerfectParity");
    }
}
