package com.perfectparitypg.particle;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record TrailParticleOption(Vec3 target, int color, int duration) implements ParticleOptions {
    public static final MapCodec<TrailParticleOption> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(Vec3.CODEC.fieldOf("target").forGetter(TrailParticleOption::target), ExtraCodecs.ARGB_COLOR_CODEC.fieldOf("color").forGetter(TrailParticleOption::color), ExtraCodecs.POSITIVE_INT.fieldOf("duration").forGetter(TrailParticleOption::duration)).apply(instance, TrailParticleOption::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, TrailParticleOption> STREAM_CODEC;
    public static final StreamCodec<FriendlyByteBuf, Vec3> VEC3_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.DOUBLE, Vec3::x,
                    ByteBufCodecs.DOUBLE, Vec3::y,
                    ByteBufCodecs.DOUBLE, Vec3::z,
                    Vec3::new
            );

    public @NotNull ParticleType<TrailParticleOption> getType() {
        return ModParticles.TRAIL;
    }

    public Vec3 target() {
        return this.target;
    }

    public int color() {
        return this.color;
    }

    public int duration() {
        return this.duration;
    }

    static {

        STREAM_CODEC =
                StreamCodec.composite(
                        VEC3_CODEC,       // read/write a Vec3
                        TrailParticleOption::target,
                        ByteBufCodecs.INT,    // read/write the int color
                        TrailParticleOption::color,
                        ByteBufCodecs.VAR_INT, // read/write the int duration
                        TrailParticleOption::duration,
                        TrailParticleOption::new  // combiner: (Vec3, color, duration) → new TrailParticleOption(...)
                );
    }
}