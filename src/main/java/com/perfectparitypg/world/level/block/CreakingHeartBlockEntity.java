package com.perfectparitypg.world.level.block;

import com.mojang.datafixers.util.Either;
import com.perfectparitypg.PerfectParityPG;
import com.perfectparitypg.datagen.ModBlockTagProvider;
import com.perfectparitypg.entity.ModEntities;
import com.perfectparitypg.entity.creaking.Creaking;
import com.perfectparitypg.particle.TrailParticleOption;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.SpawnUtil;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class CreakingHeartBlockEntity extends BlockEntity {
    private static final int PLAYER_DETECTION_RANGE = 32;
    public static final int CREAKING_ROAMING_RADIUS = 32;
    private static final int DISTANCE_CREAKING_TOO_FAR = 34;
    private static final int SPAWN_RANGE_XZ = 16;
    private static final int SPAWN_RANGE_Y = 8;
    private static final int ATTEMPTS_PER_SPAWN = 5;
    private static final int UPDATE_TICKS = 20;
    private static final int UPDATE_TICKS_VARIANCE = 5;
    private static final int HURT_CALL_TOTAL_TICKS = 100;
    private static final int NUMBER_OF_HURT_CALLS = 10;
    private static final int HURT_CALL_INTERVAL = 10;
    private static final int HURT_CALL_PARTICLE_TICKS = 50;
    private static final int MAX_DEPTH = 2;
    private static final int MAX_COUNT = 64;
    private static final int TICKS_GRACE_PERIOD = 30;
    private static final Optional<Creaking> NO_CREAKING = Optional.empty();
    @Nullable
    private Either<Creaking, UUID> creakingInfo;
    private long ticksExisted;
    private int ticker;
    private int emitter;
    @Nullable
    private Vec3 emitterTarget;
    private int outputSignal;

    public CreakingHeartBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CREAKING_HEART, blockPos, blockState);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, CreakingHeartBlockEntity creakingHeartBlockEntity) {
        ++creakingHeartBlockEntity.ticksExisted;
        if (level instanceof ServerLevel serverLevel) {
            int i = creakingHeartBlockEntity.computeAnalogOutputSignal();
            if (creakingHeartBlockEntity.outputSignal != i) {
                creakingHeartBlockEntity.outputSignal = i;
                level.updateNeighbourForOutputSignal(blockPos, ModBlocks.CREAKING_HEART);
            }

            if (creakingHeartBlockEntity.emitter > 0) {
                if (creakingHeartBlockEntity.emitter > 50) {
                    creakingHeartBlockEntity.emitParticles(serverLevel, 1, true);
                    creakingHeartBlockEntity.emitParticles(serverLevel, 1, false);
                }

                if (creakingHeartBlockEntity.emitter % 10 == 0 && creakingHeartBlockEntity.emitterTarget != null) {
                    creakingHeartBlockEntity.getCreakingProtector().ifPresent((creakingx) -> creakingHeartBlockEntity.emitterTarget = creakingx.getBoundingBox().getCenter());
                    Vec3 vec3 = Vec3.atCenterOf(blockPos);
                    float f = 0.2F + 0.8F * (float)(100 - creakingHeartBlockEntity.emitter) / 100.0F;
                    Vec3 vec32 = vec3.subtract(creakingHeartBlockEntity.emitterTarget).scale((double)f).add(creakingHeartBlockEntity.emitterTarget);
                    BlockPos blockPos2 = BlockPos.containing(vec32);
                    float g = (float)creakingHeartBlockEntity.emitter / 2.0F / 100.0F + 0.5F;
                    // serverLevel.playSound((Player)null, blockPos2, SoundEvents.CREAKING_HEART_HURT, SoundSource.BLOCKS, g, 1.0F);
                }

                --creakingHeartBlockEntity.emitter;
            }

            if (creakingHeartBlockEntity.ticker-- < 0) {
                creakingHeartBlockEntity.ticker = creakingHeartBlockEntity.level == null ? 20 : creakingHeartBlockEntity.level.random.nextInt(5) + 20;
                if (creakingHeartBlockEntity.creakingInfo == null) {
                    if (!CreakingHeartBlock.hasRequiredLogs(blockState, level, blockPos)) {
                        level.setBlock(blockPos, (BlockState)blockState.setValue(CreakingHeartBlock.ACTIVE, false), 3);
                    } else if ((Boolean)blockState.getValue(CreakingHeartBlock.ACTIVE)) {
                        if (CreakingHeartBlock.isNaturalNight(level)) {
                            if (level.getDifficulty() != Difficulty.PEACEFUL) {
                                if (serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                                    Player player = level.getNearestPlayer((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), (double)32.0F, false);
                                    if (player != null) {
                                        Creaking creaking = spawnProtector(serverLevel, creakingHeartBlockEntity);
                                        if (creaking != null) {
                                            creakingHeartBlockEntity.setCreakingInfo(creaking);
                                            // creaking.makeSound(SoundEvents.CREAKING_SPAWN);
                                            // level.playSound((Player)null, creakingHeartBlockEntity.getBlockPos(), SoundEvents.CREAKING_HEART_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
                                        }
                                    }

                                }
                            }
                        }
                    }
                } else {
                    Optional<Creaking> optional = creakingHeartBlockEntity.getCreakingProtector();
                    if (optional.isPresent()) {
                        Creaking creaking = (Creaking)optional.get();
                        if (!CreakingHeartBlock.isNaturalNight(level) || creakingHeartBlockEntity.distanceToCreaking() > (double)34.0F || creaking.playerIsStuckInYou()) {
                            creakingHeartBlockEntity.removeProtector((DamageSource)null);
                            return;
                        }

                        if (!CreakingHeartBlock.hasRequiredLogs(blockState, level, blockPos) && creakingHeartBlockEntity.creakingInfo == null) {
                            level.setBlock(blockPos, (BlockState)blockState.setValue(CreakingHeartBlock.ACTIVE, false), 3);
                        }
                    }

                }
            }
        }
    }

    private double distanceToCreaking() {
        return (Double)this.getCreakingProtector().map((creaking) -> Math.sqrt(creaking.distanceToSqr(Vec3.atBottomCenterOf(this.getBlockPos())))).orElse((double)0.0F);
    }

    private void clearCreakingInfo() {
        this.creakingInfo = null;
        this.setChanged();
    }

    public void setCreakingInfo(Creaking creaking) {
        this.creakingInfo = Either.left(creaking);
        this.setChanged();
    }

    public void setCreakingInfo(UUID uUID) {
        this.creakingInfo = Either.right(uUID);
        this.ticksExisted = 0L;
        this.setChanged();
    }

    private Optional<Creaking> getCreakingProtector() {
        if (this.creakingInfo == null) {
            return NO_CREAKING;
        } else {
            if (this.creakingInfo.left().isPresent()) {
                Creaking creaking = (Creaking)this.creakingInfo.left().get();
                if (!creaking.isRemoved()) {
                    return Optional.of(creaking);
                }

                this.setCreakingInfo(creaking.getUUID());
            }

            Level uUID = this.level;
            if (uUID instanceof ServerLevel serverLevel) {
                if (this.creakingInfo.right().isPresent()) {
                    UUID uUID2 = (UUID)this.creakingInfo.right().get();
                    Entity entity = serverLevel.getEntity(uUID2);
                    if (entity instanceof Creaking creaking2) {
                        this.setCreakingInfo(creaking2);
                        return Optional.of(creaking2);
                    }

                    if (this.ticksExisted >= 30L) {
                        this.clearCreakingInfo();
                    }

                    return NO_CREAKING;
                }
            }

            return NO_CREAKING;
        }
    }

    @Nullable
    private static Creaking spawnProtector(ServerLevel serverLevel, CreakingHeartBlockEntity creakingHeartBlockEntity) {
        BlockPos blockPos = creakingHeartBlockEntity.getBlockPos();
        Optional<Creaking> optional = SpawnUtil.trySpawnMob(ModEntities.CREAKING, MobSpawnType.SPAWNER, serverLevel, blockPos, 5, 16, 8, SpawnUtil.Strategy.ON_TOP_OF_COLLIDER);
        if (optional.isEmpty()) {
            return null;
        } else {
            Creaking creaking = (Creaking)optional.get();
            serverLevel.gameEvent(creaking, GameEvent.ENTITY_PLACE, creaking.position());
            serverLevel.broadcastEntityEvent(creaking, (byte)60);
            creaking.setTransient(blockPos);
            return creaking;
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveCustomOnly(provider);
    }

    public void creakingHurt() {
        Object serverLevel = this.getCreakingProtector().orElse((Creaking) null);
        if (serverLevel instanceof Creaking creaking) {
            Level i = this.level;
            if (i instanceof ServerLevel serverLevel2) {
                if (this.emitter <= 0) {
                    this.emitParticles(serverLevel2, 20, false);
                    int rand = this.level.getRandom().nextIntBetweenInclusive(2, 3);

                    for(int j = 0; j < rand; ++j) {
                        this.spreadResin().ifPresent((blockPos) -> {
                            // this.level.playSound((Player)null, blockPos, SoundEvents.RESIN_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                            this.level.gameEvent(GameEvent.BLOCK_PLACE, blockPos, GameEvent.Context.of(this.level.getBlockState(blockPos)));
                        });
                    }

                    this.emitter = 100;
                    this.emitterTarget = creaking.getBoundingBox().getCenter();
                }
            }
        }
    }

    private Optional<BlockPos> spreadResin() {
        Mutable<BlockPos> mutable = new MutableObject((Object)null);
        BlockPos.breadthFirstTraversal(this.worldPosition, 2, 64, (blockPos, consumer) -> {
            for(Direction direction : Util.shuffledCopy(Direction.values(), this.level.random)) {
                BlockPos blockPos2 = blockPos.relative(direction);
                if (this.level.getBlockState(blockPos2).is(ModBlockTagProvider.PALE_OAK_LOGS)) {
                    consumer.accept(blockPos2);
                }
            }

        }, (blockPos) -> {
            if (this.level.getBlockState(blockPos).is(ModBlockTagProvider.PALE_OAK_LOGS)) {
                for (Direction direction : Util.shuffledCopy(Direction.values(), this.level.random)) {
                    BlockPos blockPos2 = blockPos.relative(direction);
                    BlockState blockState = this.level.getBlockState(blockPos2);
                    Direction direction2 = direction.getOpposite();
                    if (blockState.isAir()) {
                        blockState = ModBlocks.RESIN_CLUMP.defaultBlockState();
                    } else if (blockState.is(Blocks.WATER) && blockState.getFluidState().isSource()) {
                        blockState = ModBlocks.RESIN_CLUMP.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, true);
                    }

                    if (blockState.is(ModBlocks.RESIN_CLUMP) && !MultifaceBlock.hasFace(blockState, direction2)) {
                        this.level.setBlock(blockPos2, (BlockState) blockState.setValue(MultifaceBlock.getFaceProperty(direction2), true), 3);
                        mutable.setValue(blockPos2);
                        return false;
                    }
                }

            }
            return true;
        });
        return Optional.ofNullable((BlockPos)mutable.getValue());
    }

    private void emitParticles(ServerLevel serverLevel, int i, boolean bl) {
        Object j = this.getCreakingProtector().orElse((Creaking) null);
        if (j instanceof Creaking creaking) {
            int k = bl ? 16545810 : 6250335;
            RandomSource randomSource = serverLevel.random;

            for(double d = (double)0.0F; d < (double)i; ++d) {
                AABB aABB = creaking.getBoundingBox();
                Vec3 vec3 = aABB.getMinPosition().add(randomSource.nextDouble() * aABB.getXsize(), randomSource.nextDouble() * aABB.getYsize(), randomSource.nextDouble() * aABB.getZsize());
                Vec3 vec32 = Vec3.atLowerCornerOf(this.getBlockPos()).add(randomSource.nextDouble(), randomSource.nextDouble(), randomSource.nextDouble());
                if (bl) {
                    Vec3 vec33 = vec3;
                    vec3 = vec32;
                    vec32 = vec33;
                }
                TrailParticleOption trailParticleOption = new TrailParticleOption(vec32, k, randomSource.nextInt(40) + 10);
                serverLevel.sendParticles(trailParticleOption, vec3.x, vec3.y, vec3.z, 1, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F);

            }

        }
    }

    public void removeProtector(@Nullable DamageSource damageSource) {
        Object var3 = this.getCreakingProtector().orElse((Creaking) null);
        PerfectParityPG.LOGGER.info(String.valueOf(damageSource));
        if (var3 instanceof Creaking creaking) {
            if (damageSource == null) {
                creaking.tearDown();
            } else {
                creaking.creakingDeathEffects(damageSource);
                creaking.setTearingDown();
            }

            this.clearCreakingInfo();
        }

    }

    public boolean isProtector(Creaking creaking) {
        return (Boolean)this.getCreakingProtector().map((creaking2) -> creaking2 == creaking).orElse(false);
    }

    public int getAnalogOutputSignal() {
        return this.outputSignal;
    }

    public int computeAnalogOutputSignal() {
        if (this.creakingInfo != null && !this.getCreakingProtector().isEmpty()) {
            double d = this.distanceToCreaking();
            double e = Math.clamp(d, (double)0.0F, (double)32.0F) / (double)32.0F;
            return 15 - (int)Math.floor(e * (double)15.0F);
        } else {
            return 0;
        }
    }

    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        if (compoundTag.contains("creaking")) {
            this.setCreakingInfo(compoundTag.getUUID("creaking"));
        } else {
            this.clearCreakingInfo();
        }

    }

    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        if (this.creakingInfo != null) {
            compoundTag.putUUID("creaking", (UUID)this.creakingInfo.map(Entity::getUUID, (uUID) -> uUID));
        }

    }
}
