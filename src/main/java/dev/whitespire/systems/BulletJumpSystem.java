package dev.whitespire.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.ChangeVelocityType;
import com.hypixel.hytale.server.core.entity.entities.player.movement.MovementManager;
import com.hypixel.hytale.server.core.entity.movement.MovementStatesComponent;
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.modules.splitvelocity.VelocityConfig;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
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

    private void deductStamina(
        int index,
        @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
        @Nonnull Store<EntityStore> store
    ) {
        EntityStatMap statMap = archetypeChunk.getComponent(
            index,
            EntityStatMap.getComponentType()
        );
        statMap.subtractStatValue(
            DefaultEntityStatTypes.getStamina(),
            config.get().getStaminaCost()
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
        MovementStatesComponent movementStates = archetypeChunk.getComponent(
            index,
            MovementStatesComponent.getComponentType()
        );
        BulletJumpComponent bulletJumpComponent = archetypeChunk.getComponent(
            index,
            BulletJumpComponent.getComponentType()
        );
        if (movementStates.getMovementStates().sliding) {
            bulletJumpComponent.tick();
        } else if (
            movementStates.getMovementStates().jumping &&
            bulletJumpComponent.getTicks() > config.get().getMinSlideTicks()
        ) {
            Velocity playerVelocity = archetypeChunk.getComponent(
                index,
                Velocity.getComponentType()
            );
            HeadRotation playerHeadRotation = archetypeChunk.getComponent(
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
            Vector3d bulletJumpVelocity = playerHeadRotation
                .getDirection()
                .scale(bulletJumpVelocityMultiplier);
            VelocityConfig velocityConfig = new VelocityConfig();
            playerVelocity.addInstruction(
                bulletJumpVelocity,
                velocityConfig,
                ChangeVelocityType.Set
            );
            if (config.get().getStaminaCost() > 0) {
                deductStamina(index, archetypeChunk, store);
            }

            bulletJumpComponent.reset();
        } else {
            bulletJumpComponent.reset();
        }
    }
}
