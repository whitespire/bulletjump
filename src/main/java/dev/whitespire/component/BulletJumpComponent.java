package dev.whitespire.component;

import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.whitespire.BulletJump;
import javax.annotation.Nonnull;

public class BulletJumpComponent implements Component<EntityStore> {

    private int slideTicks;
    private boolean activeJump;
    private boolean airSlideBoostGiven;

    public BulletJumpComponent() {
        this.slideTicks = 0;
        this.activeJump = false;
        this.airSlideBoostGiven = false;
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

    public void useAirSlideBoost() {
        this.airSlideBoostGiven = true;
    }

    public void startJump() {
        this.activeJump = true;
    }

    public void land() {
        this.activeJump = false;
        this.airSlideBoostGiven = false;
    }

    public int getTicks() {
        return this.slideTicks;
    }

    public boolean isBulletJumpActive() {
        return this.activeJump;
    }

    public boolean isAirSlideBoostGiven() {
        return this.airSlideBoostGiven;
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
