package dev.whitespire.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class BulletJumpConfig {

    private float staminaCost = 3;
    private float minSlideSeconds = 0.3f;
    private double jumpVelocityMultiplier = 1.5;
    public static final BuilderCodec<BulletJumpConfig> CODEC =
        BuilderCodec.builder(BulletJumpConfig.class, BulletJumpConfig::new)
            .append(
                new KeyedCodec<Float>("StaminaCost", Codec.FLOAT),
                (o, f) -> {
                    o.staminaCost = f;
                },
                o -> o.staminaCost
            )
            .add()
            .append(
                new KeyedCodec<Float>("MinSlideTicks", Codec.FLOAT),
                (o, v) -> {
                    o.minSlideSeconds = v;
                },
                o -> o.minSlideSeconds
            )
            .add()
            .append(
                new KeyedCodec<Double>("JumpVelocityMultiplier", Codec.DOUBLE),
                (o, d) -> {
                    o.jumpVelocityMultiplier = d;
                },
                o -> o.jumpVelocityMultiplier
            )
            .add()
            .build();

    public BulletJumpConfig() {}

    public float getStaminaCost() {
        return this.staminaCost;
    }

    public float getMinSlideSeconds() {
        return this.minSlideSeconds;
    }

    public double getJumpVelocityMultiplier() {
        return this.jumpVelocityMultiplier;
    }
}
