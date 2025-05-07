package com.perfectparitypg.particle;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class ModParticles {
    public static final SimpleParticleType PALE_OAK_LEAVES = registerParticle("pale_oak_leaves", FabricParticleTypes.simple(false));
    public static final ParticleType<BlockParticleOption> BLOCK_CRUMBLE = registerParticle("block_crumble", false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
    public static final ParticleType<TrailParticleOption> TRAIL = registerParticle("trail", false, (particleType) -> TrailParticleOption.CODEC, (particleType) -> TrailParticleOption.STREAM_CODEC);


    private static SimpleParticleType registerParticle(String name, SimpleParticleType particleType) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, ResourceLocation.tryBuild(ResourceLocation.DEFAULT_NAMESPACE, name), particleType);
    }

    private static <T extends ParticleOptions> ParticleType<T> registerParticle(
            String string,
            boolean bl,
            Function<ParticleType<T>, MapCodec<T>> function,
            Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> function2
    ) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE,  ResourceLocation.tryBuild(ResourceLocation.DEFAULT_NAMESPACE, string), new ParticleType<T>(bl) {
            @Override
            public MapCodec<T> codec() {
                return (MapCodec<T>)function.apply(this);
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return (StreamCodec<? super RegistryFriendlyByteBuf, T>)function2.apply(this);
            }
        });
    }

    public static void registerParticles(){

    }
}
