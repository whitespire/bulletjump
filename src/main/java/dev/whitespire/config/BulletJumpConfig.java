package dev.whitespire.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class BulletJumpConfig {

    private float staminaCost = 3;
    private float minSlideSeconds = 0.25f;
    private double jumpVelocityMultiplier = 1.5;
    private boolean enableAirSlide = true;
    public static final BuilderCodec<BulletJumpConfig> CODEC =
        BuilderCodec.builder(BulletJumpConfig.class, BulletJumpConfig::new)
            .append(
                new KeyedCodec<Boolean>("EnableAirSlide", Codec.BOOLEAN),
                (o, v) -> {
                    o.enableAirSlide = v;
                },
                o -> o.enableAirSlide
            )
            .add()
            .append(
                new KeyedCodec<Float>("StaminaCost", Codec.FLOAT),
                (o, v) -> {
                    o.staminaCost = v;
                },
                o -> o.staminaCost
            )
            .add()
            .append(
                new KeyedCodec<Float>("MinSlideSeconds", Codec.FLOAT),
                (o, v) -> {
                    o.minSlideSeconds = v;
                },
                o -> o.minSlideSeconds
            )
            .add()
            .append(
                new KeyedCodec<Double>("JumpVelocityMultiplier", Codec.DOUBLE),
                (o, v) -> {
                    o.jumpVelocityMultiplier = v;
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

    public boolean isAirSlideEnabled() {
        return this.enableAirSlide;
    }
}
