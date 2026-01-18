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
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.modules.splitvelocity.VelocityConfig;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.whitespire.component.BulletJumpComponent;
import javax.annotation.Nonnull;

public class BulletJumpSystem extends EntityTickingSystem<EntityStore> {

    public BulletJumpSystem() {}

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
            bulletJumpComponent.getTicks() > 5
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
                movementManager.getSettings().jumpForce * 1.5;
            Vector3d bulletJumpVelocity = playerHeadRotation
                .getDirection()
                .scale(bulletJumpVelocityMultiplier);
            VelocityConfig velocityConfig = new VelocityConfig();
            playerVelocity.addInstruction(
                bulletJumpVelocity,
                velocityConfig,
                ChangeVelocityType.Set
            );

            bulletJumpComponent.reset();
        } else {
            bulletJumpComponent.reset();
        }
    }
}
