package com.perfectparitypg.entity.creaking;

import com.mojang.serialization.Dynamic;
import com.perfectparitypg.PerfectParityPG;
import com.perfectparitypg.sound.ModSounds;
import com.perfectparitypg.world.level.block.CreakingHeartBlock;
import com.perfectparitypg.world.level.block.CreakingHeartBlockEntity;
import com.perfectparitypg.world.level.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Creaking extends Monster {
    private net.minecraft.world.damagesource.DamageSource lastDamageSource;
    private static final EntityDataAccessor<Boolean> CAN_MOVE;
    private static final EntityDataAccessor<Boolean> IS_ACTIVE;
    private static final EntityDataAccessor<Boolean> IS_TEARING_DOWN;
    private static final EntityDataAccessor<Optional<BlockPos>> HOME_POS;
    private static final int ATTACK_ANIMATION_DURATION = 15;
    private static final int MAX_HEALTH = 1;
    private static final float ATTACK_DAMAGE = 3.0F;
    private static final float FOLLOW_RANGE = 32.0F;
    private static final float ACTIVATION_RANGE_SQ = 144.0F;
    public static final int ATTACK_INTERVAL = 40;
    private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.4F;
    public static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.3F;
    public static final int CREAKING_ORANGE = 16545810;
    public static final int CREAKING_GRAY = 6250335;
    public static final int INVULNERABILITY_ANIMATION_DURATION = 8;
    public static final int TWITCH_DEATH_DURATION = 45;
    private static final int MAX_PLAYER_STUCK_COUNTER = 4;
    private int attackAnimationRemainingTicks;
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState invulnerabilityAnimationState = new AnimationState();
    public final AnimationState deathAnimationState = new AnimationState();
    private int invulnerabilityAnimationRemainingTicks;
    private boolean eyesGlowing;
    private int nextFlickerTime;
    private int playerStuckCounter;



    public Creaking(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.lookControl = new CreakingLookControl(this);
        this.moveControl = new CreakingMoveControl(this);
        this.jumpControl = new CreakingJumpControl(this);
        GroundPathNavigation groundPathNavigation = (GroundPathNavigation)this.getNavigation();
        groundPathNavigation.setCanFloat(true);
        this.xpReward = 0;
        this.nextFlickerTime = 0;
    }

    public void setTransient(BlockPos blockPos) {
        this.setHomePos(blockPos);
        this.setPathfindingMalus(PathType.DAMAGE_OTHER, 8.0F);
        this.setPathfindingMalus(PathType.POWDER_SNOW, 8.0F);
        this.setPathfindingMalus(PathType.LAVA, 8.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, 0.0F);
        this.setPathfindingMalus(PathType.DANGER_FIRE, 0.0F);
    }

    public boolean isHeartBound() {
        return this.getHomePos() != null;
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new CreakingBodyRotationControl(this);
    }

    @Override
    protected Brain.@NotNull Provider<Creaking> brainProvider() {
        return CreakingAi.brainProvider();
    }

    @Override
    protected @NotNull Brain<?> makeBrain(Dynamic<?> dynamic) {
        return CreakingAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CAN_MOVE, true);
        builder.define(IS_ACTIVE, false);
        builder.define(IS_TEARING_DOWN, false);
        builder.define(HOME_POS, Optional.empty());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, (double)1.0F).add(Attributes.MOVEMENT_SPEED, (double)0.4F).add(Attributes.ATTACK_DAMAGE, (double)3.0F).add(Attributes.FOLLOW_RANGE, (double)32.0F).add(Attributes.STEP_HEIGHT, (double)1.0625F);
    }

    public boolean canMove() {
        return (Boolean)this.entityData.get(CAN_MOVE);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        this.attackAnimationRemainingTicks = 15;
        this.level().broadcastEntityEvent(this, (byte)4);
        return super.doHurtTarget(entity);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        this.lastDamageSource = damageSource;
        BlockPos blockPos = this.getHomePos();
        if (blockPos != null && !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            if (this.invulnerabilityAnimationRemainingTicks <= 0 && !this.isDeadOrDying()) {
                Entity attacker = damageSource.getEntity();
                if (attacker instanceof LivingEntity) {
                    this.setLastHurtByMob((LivingEntity)attacker);
                }
                Player player = this.resolvePlayerResponsibleForDamage(damageSource);
                Entity entity = damageSource.getDirectEntity();
                if (!(entity instanceof LivingEntity) && !(entity instanceof Projectile) && player == null) {
                    return false;
                } else {
                    this.invulnerabilityAnimationRemainingTicks = 8;
                    this.level().broadcastEntityEvent(this, (byte)66);
                    PerfectParityPG.LOGGER.info(String.valueOf(invulnerabilityAnimationRemainingTicks));
                    BlockEntity var8 = this.level().getBlockEntity(blockPos);
                    if (var8 instanceof CreakingHeartBlockEntity) {
                        CreakingHeartBlockEntity creakingHeartBlockEntity = (CreakingHeartBlockEntity)var8;
                        if (creakingHeartBlockEntity.isProtector(this)) {
                            if (player != null) {
                                creakingHeartBlockEntity.creakingHurt();
                            }

                            this.playHurtSound(damageSource);
                        }
                    }

                    return true;
                }
            } else {
                return false;
            }
        } else {
            return super.hurt(damageSource, f);
        }
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && this.canMove();
    }

    @Override
    public void push(double d, double e, double f) {
        if (this.canMove()) {
            super.push(d, e, f);
        }
    }

    @Override
    public Brain<Creaking> getBrain() {
        return (Brain<Creaking>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {
        ProfilerFiller profilerFiller = this.level().getProfiler();
        profilerFiller.push("creakingBrain");
        this.getBrain().tick((ServerLevel)this.level(), this);
        profilerFiller.pop();
        CreakingAi.updateActivity(this);
        super.customServerAiStep();
    }

    @Override
    public void aiStep() {
        if (this.invulnerabilityAnimationRemainingTicks > 0) {
            --this.invulnerabilityAnimationRemainingTicks;
        }

        if (this.attackAnimationRemainingTicks > 0) {
            --this.attackAnimationRemainingTicks;
        }

        if (!this.level().isClientSide) {
            boolean bl = this.entityData.get(CAN_MOVE);
            boolean bl2 = this.checkCanMove();
            if (bl2 != bl) {
                this.gameEvent(GameEvent.ENTITY_ACTION);
                if (bl2) {
                    this.makeSound(ModSounds.CREAKING_UNFREEZE);
                } else {
                    this.stopInPlace();
                    this.makeSound(ModSounds.CREAKING_FREEZE);

                }
            }

            this.entityData.set(CAN_MOVE, bl2);
        }

        super.aiStep();
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide) {
            BlockPos blockPos = this.getHomePos();
            if (blockPos != null) {
                boolean var10000;
                label21: {
                    BlockEntity var4 = this.level().getBlockEntity(blockPos);
                    if (var4 instanceof CreakingHeartBlockEntity) {
                        CreakingHeartBlockEntity creakingHeartBlockEntity = (CreakingHeartBlockEntity)var4;
                        if (creakingHeartBlockEntity.isProtector(this)) {
                            var10000 = true;
                            break label21;
                        }
                    }

                    var10000 = false;
                }

                boolean bl = var10000;
                if (!bl) {
                    this.setTearingDown();
                    this.tickDeath();
                }
            }
        }

        super.tick();
        if (this.level().isClientSide) {
            this.setupAnimationStates();
        }

    }

    @Override
    protected void tickDeath() {
        if (this.isHeartBound() && this.isTearingDown()) {

            ++this.deathTime;
            checkEyeBlink();
            if (!this.level().isClientSide() && this.deathTime > 45 && !this.isRemoved()) {
                this.tearDown();
            }
        } else {
            super.tickDeath();
        }

    }

    @Override
    protected void updateWalkAnimation(float f) {
        float g = Math.min(f * 25.0F, 3.0F);
        this.walkAnimation.update(g, 0.4F);
    }



    private void setupAnimationStates() {
        this.attackAnimationState.animateWhen(this.attackAnimationRemainingTicks > 0, this.tickCount);
        this.invulnerabilityAnimationState.animateWhen(this.invulnerabilityAnimationRemainingTicks > 0, this.tickCount);
        this.deathAnimationState.animateWhen(this.isTearingDown(), this.tickCount);
    }

    public void tearDown() {
        Level aABB = this.level();
        if (aABB instanceof ServerLevel serverLevel) {
            AABB aABB2 = this.getBoundingBox();
            Vec3 vec3 = aABB2.getCenter();
            double d = aABB2.getXsize() * 0.3;
            double e = aABB2.getYsize() * 0.3;
            double f = aABB2.getZsize() * 0.3;
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.PALE_OAK_WOOD.defaultBlockState()), vec3.x, vec3.y, vec3.z, 100, d, e, f, (double)0.0F);
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, (BlockState)ModBlocks.CREAKING_HEART.defaultBlockState().setValue(CreakingHeartBlock.ENABLED, true)), vec3.x, vec3.y, vec3.z, 10, d, e, f, (double)0.0F);
        }

        this.makeSound(this.getDeathSound());
        this.remove(RemovalReason.DISCARDED);
    }

    public void creakingDeathEffects(DamageSource damageSource) {
        this.resolvePlayerResponsibleForDamage(damageSource);
        // this.die(damageSource);
        this.makeSound(ModSounds.CREAKING_TWITCH);
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 66) {
            this.invulnerabilityAnimationRemainingTicks = 8;
            this.playHurtSound(this.damageSources().generic());
            this.playAmbientSound();
        } else if (b == 4) {
            this.attackAnimationRemainingTicks = 15;
            this.playAttackSound();
        } else {
            super.handleEntityEvent(b);
        }

    }

    @Override
    public boolean fireImmune() {
        return this.isHeartBound() || super.fireImmune();
    }

    @Override
    protected boolean canAddPassenger(Entity entity) {
        return !this.isHeartBound() && super.canAddPassenger(entity);
    }

    @Override
    protected boolean couldAcceptPassenger() {
        return !this.isHeartBound() && super.couldAcceptPassenger();
    }

    @Override
    protected void addPassenger(Entity entity) {
        if (this.isHeartBound()) {
            throw new IllegalStateException("Should never addPassenger without checking couldAcceptPassenger()");
        }
    }

    @Override
    public boolean canUsePortal(boolean bl) {
        return !this.isHeartBound() && super.canUsePortal(bl);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new CreakingPathNavigation(this, level);
    }


    public boolean playerIsStuckInYou() {
        List<Player> list = (List)this.brain.getMemory(MemoryModuleType.NEAREST_PLAYERS).orElse(List.of());
        if (list.isEmpty()) {
            this.playerStuckCounter = 0;
            return false;
        } else {
            AABB aABB = this.getBoundingBox();

            for(Player player : list) {
                if (aABB.contains(player.getEyePosition())) {
                    ++this.playerStuckCounter;
                    return this.playerStuckCounter > 4;
                }
            }

            this.playerStuckCounter = 0;
            return false;
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains("home_pos")) {
            this.setTransient((BlockPos) NbtUtils.readBlockPos(compoundTag, "home_pos").orElseThrow());
        }

    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        BlockPos blockPos = this.getHomePos();
        if (blockPos != null) {
            compoundTag.put("home_pos", NbtUtils.writeBlockPos(blockPos));
        }

    }

    public void setHomePos(BlockPos blockPos) {
        this.entityData.set(HOME_POS, Optional.of(blockPos));
    }

    @Nullable
    public BlockPos getHomePos() {
        return (BlockPos)((Optional)this.entityData.get(HOME_POS)).orElse((Object)null);
    }

    public void setTearingDown() {
        this.entityData.set(IS_TEARING_DOWN, true);
    }

    public boolean isTearingDown() {
        return (Boolean)this.entityData.get(IS_TEARING_DOWN);
    }

    public boolean hasGlowingEyes() {
        return this.eyesGlowing;
    }

    public void checkEyeBlink() {
        if (this.deathTime > this.nextFlickerTime) {
            this.nextFlickerTime = this.deathTime + this.getRandom().nextIntBetweenInclusive(this.eyesGlowing ? 2 : this.deathTime / 4, this.eyesGlowing ? 8 : this.deathTime / 2);
            this.setIsActive(!this.isActive());
        }

    }

    public void playAttackSound() {
        this.makeSound(ModSounds.CREAKING_ATTACK);
    }
    protected SoundEvent getAmbientSound() {
        return this.isActive() ? null : ModSounds.CREAKING_AMBIENT;
    }
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return this.isHeartBound() ? ModSounds.CREAKING_SWAY : super.getHurtSound(damageSource);
    }
    protected SoundEvent getDeathSound() {
        return ModSounds.CREAKING_DEATH;
    }
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
        this.playSound(ModSounds.CREAKING_STEP, 0.15F, 1.0F);
    }

    @Nullable
    @Override
    public LivingEntity getTarget() {
        LivingEntity target = this.getTargetFromBrain();
        return target;
    }

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }

    @Override
    public void knockback(double d, double e, double f) {
        if (this.canMove() && this.lastDamageSource != null) {
            if (this.lastDamageSource.is(net.minecraft.tags.DamageTypeTags.IS_EXPLOSION) || "wind_charge".equals(this.lastDamageSource.getMsgId())) {
                super.knockback(d, e, f);
            }
            // else: ignore knockback from attacks and other sources
        }
    }

    public boolean checkCanMove() {
        if (isTearingDown()) {
            return false;
        }
        List<Player> players = getNearestPlayers();
        boolean bl = this.isActive();
        // No players nearby → always move
        if (players.isEmpty()) {
            deactivate();          // clear ATTACK_TARGET & isActive if you like
            return true;
        }

        // Freeze if **any** nearby player is staring at us
        boolean bl2 = false;
        for (Player p : players) {
            if (this.canAttack(p) && !this.isAlliedTo(p)) {
                bl2 = true;
                if ((!bl || !p.getInventory().armor.get(3).is(Blocks.CARVED_PUMPKIN.asItem())) && (isLookingAtMe(p, 0.5F, false,
                        this.getEyeY(), this.getY() + 0.5 * this.getScale(),
                        (this.getEyeY() + this.getY()) / 2.0))) {

                    if (bl) {
                        return false;
                    }
                    // If close enough, and we weren’t already angry, become active
                    if (p.distanceToSqr(this) < (double)144.0F) {
                        this.activate(p);
                        return false;
                    }
                }
            }
        }
        if (!bl2 && bl) {
            this.deactivate();
        }
        // Nobody is looking at us → we can move
        return true;
    }

    public void activate(Player player) {
        this.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, player);
        this.gameEvent(GameEvent.ENTITY_ACTION);
        this.makeSound(ModSounds.CREAKING_ACTIVATE);
        this.setIsActive(true);
    }

    public void deactivate() {
        this.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        this.gameEvent(GameEvent.ENTITY_ACTION);
        this.makeSound(ModSounds.CREAKING_DEACTIVATE);
        this.setIsActive(false);
    }

    public void setIsActive(boolean bl) {
        this.entityData.set(IS_ACTIVE, bl);
    }

    public boolean isActive() {
        return (Boolean)this.entityData.get(IS_ACTIVE);
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos, LevelReader levelReader) {
        return 0.0F;
    }

    private List<Player> getNearestPlayers() {
        List<Player> list = this.brain.getMemory(MemoryModuleType.NEAREST_PLAYERS).orElse(List.of());
        if (list.isEmpty()) {
            AABB aabb = this.getBoundingBox().inflate(16);
            List<Entity> entities = level().getEntities(this, aabb);
            List<Player> players = entities.stream()
                    .flatMap(e -> (e instanceof Player p
                            && p.isAlive()
                            && !p.isSpectator()
                            && !p.isCreative())
                            ? Stream.of(p)   // keep the Player
                            : Stream.empty() // drop everything else
                    )
                    .toList();
            brain.setMemory(MemoryModuleType.NEAREST_PLAYERS, players);
            return players;
        }
        return list;
    }


    static {
        CAN_MOVE = SynchedEntityData.defineId(Creaking.class, EntityDataSerializers.BOOLEAN);
        IS_ACTIVE = SynchedEntityData.defineId(Creaking.class, EntityDataSerializers.BOOLEAN);
        IS_TEARING_DOWN = SynchedEntityData.defineId(Creaking.class, EntityDataSerializers.BOOLEAN);
        HOME_POS = SynchedEntityData.defineId(Creaking.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    }

    public boolean isLookingAtMe(Entity player, double d, boolean bl, double... ds) {
        Vec3 vec3 = player.getViewVector(1.0F).normalize();

        for(double e : ds) {
            Vec3 vec32 = new Vec3(this.getX() - player.getX(), e - player.getEyeY(), this.getZ() - player.getZ());
            double f = vec32.length();
            vec32 = vec32.normalize();
            double g = vec3.dot(vec32);
            if (g > (double)1.0F - d / (bl ? f : (double)1.0F) && hasLineOfSight((Player) player, this)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasLineOfSight(Player player, Mob creaking) {
        if (creaking.level() != player.level()) {
            return false;
        } else {
            Vec3 vec3 = new Vec3(player.getX(), player.getEyeY(), player.getZ());
            Vec3 vec31 = new Vec3(creaking.getX(), creaking.getEyeY(), creaking.getZ());
            return (!(vec31.distanceTo(vec3) > 12) || player.level()
                    .clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player))
                    .getType() == HitResult.Type.MISS) && !player.isCreative();
        }
    }

    @Nullable
    protected Player resolvePlayerResponsibleForDamage(DamageSource damageSource) {
        Entity entity = damageSource.getEntity();
        if (entity instanceof Player player) {
            this.lastHurtByPlayerTime = 100;
            this.lastHurtByPlayer = player;
            return player;
        } else {
            if (entity instanceof Wolf wolf) {
                if (wolf.isTame()) {
                    this.lastHurtByPlayerTime = 100;
                    LivingEntity var6 = wolf.getOwner();
                    if (var6 instanceof Player) {
                        Player player2 = (Player)var6;
                        this.lastHurtByPlayer = player2;
                    } else {
                        this.lastHurtByPlayer = null;
                    }

                    return this.lastHurtByPlayer;
                }
            }

            return null;
        }
    }

    class CreakingLookControl extends LookControl {
        public CreakingLookControl(final Creaking creaking2) {
            super(creaking2);
        }

        public void tick() {
            if (Creaking.this.canMove()) {
                super.tick();
            }

        }
    }

    class CreakingMoveControl extends MoveControl {
        public CreakingMoveControl(final Creaking creaking2) {
            super(creaking2);
        }

        public void tick() {
            if (Creaking.this.canMove()) {
                super.tick();
            }

        }
    }

    class CreakingJumpControl extends JumpControl {
        public CreakingJumpControl(final Creaking creaking2) {
            super(creaking2);
        }

        public void tick() {
            if (Creaking.this.canMove()) {
                super.tick();
            } else {
                Creaking.this.setJumping(false);
            }

        }
    }

    class CreakingBodyRotationControl extends BodyRotationControl {
        public CreakingBodyRotationControl(final Creaking creaking2) {
            super(creaking2);
        }

        public void clientTick() {
            if (Creaking.this.canMove()) {
                super.clientTick();
            }

        }
    }

    class HomeNodeEvaluator extends WalkNodeEvaluator {
        private static final int MAX_DISTANCE_TO_HOME_SQ = 1024;

        HomeNodeEvaluator() {
        }

        public PathType getPathType(PathfindingContext pathfindingContext, int i, int j, int k) {
            BlockPos blockPos = Creaking.this.getHomePos();
            if (blockPos == null) {
                return super.getPathType(pathfindingContext, i, j, k);
            } else {
                double d = blockPos.distSqr(new Vec3i(i, j, k));
                return d > (double)1024.0F && d >= blockPos.distSqr(pathfindingContext.mobPosition()) ? PathType.BLOCKED : super.getPathType(pathfindingContext, i, j, k);
            }
        }
    }

    class CreakingPathNavigation extends GroundPathNavigation {
        CreakingPathNavigation(final Creaking creaking2, final Level level) {
            super(creaking2, level);
        }

        public void tick() {
            if (Creaking.this.canMove()) {
                super.tick();
            }

        }

        protected PathFinder createPathFinder(int i) {
            this.nodeEvaluator = Creaking.this.new HomeNodeEvaluator();
            this.nodeEvaluator.setCanPassDoors(true);
            return new PathFinder(this.nodeEvaluator, i);
        }
    }
}
