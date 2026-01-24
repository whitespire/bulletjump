package dev.whitespire.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.protocol.ChangeVelocityType;
import com.hypixel.hytale.protocol.MovementStates;
import com.hypixel.hytale.server.core.entity.entities.player.movement.MovementManager;
import com.hypixel.hytale.server.core.entity.movement.MovementStatesComponent;
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.modules.splitvelocity.VelocityConfig;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import dev.whitespire.BulletJump;
import dev.whitespire.component.BulletJumpComponent;
import dev.whitespire.config.BulletJumpConfig;
import javax.annotation.Nonnull;

public class BulletJumpSystem extends EntityTickingSystem<EntityStore> {

    private Config<BulletJumpConfig> config;

    public BulletJumpSystem(Config<BulletJumpConfig> config) {
        this.config = config;
    }

    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return Query.and(
            PlayerRef.getComponentType(),
            MovementStatesComponent.getComponentType(),
            BulletJumpComponent.getComponentType(),
            MovementManager.getComponentType()
        );
    }

    private void applyBulletJump(
        int index,
        @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
        @Nonnull Store<EntityStore> store
    ) {
        Velocity velocity = archetypeChunk.getComponent(
            index,
            Velocity.getComponentType()
        );
        HeadRotation headRotation = archetypeChunk.getComponent(
            index,
            HeadRotation.getComponentType()
        );
        MovementManager movementManager = archetypeChunk.getComponent(
            index,
            MovementManager.getComponentType()
        );
        double bulletJumpVelocityMultiplier =
            movementManager.getSettings().jumpForce *
            config.get().getJumpVelocityMultiplier();
        Vector3d bulletJumpVelocity = headRotation
            .getDirection()
            .scale(bulletJumpVelocityMultiplier);
        VelocityConfig velocityConfig = new VelocityConfig();
        velocity.addInstruction(
            bulletJumpVelocity,
            velocityConfig,
            ChangeVelocityType.Add
        );
    }

    private void applyAirSlide(
        int index,
        @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
        @Nonnull Store<EntityStore> store
    ) {
        if (!config.get().isAirSlideEnabled()) {
            return;
        }
        Velocity velocity = archetypeChunk.getComponent(
            index,
            Velocity.getComponentType()
        );
        HeadRotation headRotation = archetypeChunk.getComponent(
            index,
            HeadRotation.getComponentType()
        );
        MovementManager movementManager = archetypeChunk.getComponent(
            index,
            MovementManager.getComponentType()
        );
        double slideVelocityMultiplier =
            movementManager.getSettings().minSlideEntrySpeed *
            config.get().getJumpVelocityMultiplier() *
            2;

        Vector3d airSlideBoostVelocity = headRotation.getDirection();
        airSlideBoostVelocity.setY(0);
        airSlideBoostVelocity = airSlideBoostVelocity.scale(
            slideVelocityMultiplier
        );
        VelocityConfig velocityConfig = new VelocityConfig();
        velocity.addInstruction(
            airSlideBoostVelocity,
            velocityConfig,
            ChangeVelocityType.Add
        );
    }

    private void deductStamina(
        int index,
        @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
        @Nonnull Store<EntityStore> store,
        float staminaCount
    ) {
        EntityStatMap statMap = archetypeChunk.getComponent(
            index,
            EntityStatMap.getComponentType()
        );
        statMap.subtractStatValue(
            DefaultEntityStatTypes.getStamina(),
            staminaCount
        );
    }

    @Override
    public void tick(
        float dt,
        int index,
        @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
        @Nonnull Store<EntityStore> store,
        @Nonnull CommandBuffer<EntityStore> commandBuffer
    ) {
        MovementStatesComponent movementStatesComponent =
            archetypeChunk.getComponent(
                index,
                MovementStatesComponent.getComponentType()
            );
        MovementStates movementStates =
            movementStatesComponent.getMovementStates();
        BulletJumpComponent bulletJumpComponent = archetypeChunk.getComponent(
            index,
            BulletJumpComponent.getComponentType()
        );
        // Main state selector - in the air (slide boost) or
        // on the ground (can bullet jump)
        if (movementStates.onGround) {
            // do the component ticks first
            bulletJumpComponent.land();
            if (movementStates.sliding) {
                bulletJumpComponent.tickSlide(dt);
                bulletJumpComponent.setCrouchActionHeld(true);
            } else {
                bulletJumpComponent.resetSlide();
                if (
                    movementStates.crouching
                ) bulletJumpComponent.setCrouchActionHeld(true);
            }

            // bullet jump condition
        } else {
            bulletJumpComponent.tickAirborne(dt);
            if (
                movementStates.jumping &&
                bulletJumpComponent.getSlideSeconds() >=
                config.get().getMinSlideSeconds()
            ) {
                applyBulletJump(index, archetypeChunk, store);
                if (config.get().getStaminaCost() > 0) {
                    deductStamina(
                        index,
                        archetypeChunk,
                        store,
                        config.get().getStaminaCost()
                    );
                }
                bulletJumpComponent.startJump();
            } else if (
                movementStates.crouching &&
                bulletJumpComponent.isCrouchActionHeld() &&
                bulletJumpComponent.getAirborneSeconds() > 0.5 &&
                !bulletJumpComponent.isAirSlideBoostGiven()
            ) {
                applyAirSlide(index, archetypeChunk, store);
                if (config.get().getStaminaCost() > 0) {
                    deductStamina(
                        index,
                        archetypeChunk,
                        store,
                        config.get().getStaminaCost() / 2
                    );
                }
                bulletJumpComponent.useAirSlideBoost();
            }
            bulletJumpComponent.resetSlide();
        }
        if (movementStates.sliding || movementStates.crouching) {
            bulletJumpComponent.setCrouchActionHeld(true);
        } else {
            bulletJumpComponent.setCrouchActionHeld(false);
        }
    }
}
