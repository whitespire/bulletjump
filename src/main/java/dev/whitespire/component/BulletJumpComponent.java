package dev.whitespire.component;

import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.whitespire.BulletJump;
import javax.annotation.Nonnull;

public class BulletJumpComponent implements Component<EntityStore> {

    private int slideTicks;

    public BulletJumpComponent() {
        this.slideTicks = 0;
    }

    public BulletJumpComponent(BulletJumpComponent other) {
        this.slideTicks = other.getTicks();
    }

    public void reset() {
        this.slideTicks = 0;
    }

    public void tick() {
        this.slideTicks += 1;
    }

    public int getTicks() {
        return this.slideTicks;
    }

    public static ComponentType<
        EntityStore,
        BulletJumpComponent
    > getComponentType() {
        return BulletJump.get().bulletJumpComponentType;
    }

    @Override
    @Nonnull
    public Component<EntityStore> clone() {
        return new BulletJumpComponent(this);
    }
}
