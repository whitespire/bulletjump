package dev.whitespire.component;

import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.whitespire.BulletJump;
import javax.annotation.Nonnull;

public class BulletJumpComponent implements Component<EntityStore> {

    private float slideSeconds;
    private float airborneSeconds;
    private boolean activeJump;
    private boolean airSlideBoostGiven;

    public BulletJumpComponent() {
        this.slideSeconds = 0;
        this.airborneSeconds = 0;
        this.activeJump = false;
        this.airSlideBoostGiven = false;
    }

    public BulletJumpComponent(BulletJumpComponent other) {
        this.slideSeconds = other.getSlideSeconds();
        this.airborneSeconds = other.getAirborneSeconds();
        this.airSlideBoostGiven = other.isAirSlideBoostGiven();
        this.activeJump = other.isBulletJumpActive();
    }

    public void resetSlide() {
        this.slideSeconds = 0;
    }

    public void tickSlide(float dt) {
        this.slideSeconds += dt;
    }

    public void tickAirborne(float dt) {
        this.airborneSeconds += dt;
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
        this.airborneSeconds = 0;
    }

    public float getSlideSeconds() {
        return this.slideSeconds;
    }

    public float getAirborneSeconds() {
        return this.airborneSeconds;
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
