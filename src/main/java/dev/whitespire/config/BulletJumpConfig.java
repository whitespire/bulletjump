package dev.whitespire.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class BulletJumpConfig {

    private float staminaCost = 3;
    private int minSlideTicks = 5;
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
                new KeyedCodec<Integer>("MinSlideTicks", Codec.INTEGER),
                (o, i) -> {
                    o.minSlideTicks = i;
                },
                o -> o.minSlideTicks
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

    public int getMinSlideTicks() {
        return this.minSlideTicks;
    }

    public double getJumpVelocityMultiplier() {
        return this.jumpVelocityMultiplier;
    }
}
