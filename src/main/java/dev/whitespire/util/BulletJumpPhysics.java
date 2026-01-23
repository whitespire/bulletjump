package dev.whitespire.util;

import com.hypixel.hytale.math.vector.Vector3d;
import dev.whitespire.BulletJump;

public final class BulletJumpPhysics {

    // Will leave some comments here since we're doing math.
    // We're trying to get a vector for the boost, which is
    // plain horizontal (won't pull you up or down), and is
    // locked to 90 degrees left or right from where you are
    // currently heading to.
    public static Vector3d computeAirSlideBaseBoostVelocity(
        Vector3d lookDirection,
        Vector3d velocityDirection
    ) {
        Vector3d baseVelocity = new Vector3d();
        // Y velocity is 0, since we're neither going
        // up or down
        baseVelocity.setY(0);
        BulletJump.LOGGER.atFine().log(
            "lookDirection     %f, %f \n" + "velocityDirection %f, %f",
            lookDirection.getX(),
            lookDirection.getZ(),
            velocityDirection.getX(),
            velocityDirection.getZ()
        );

        // Get the leftmost and rightmost directions
        // we can go by rotating our body direction
        // 90 degrees left and right
        Vector3d leftmostDirection = velocityDirection
            .clone()
            .rotateY((float) (-Math.PI / 2));
        Vector3d rightmostDirection = velocityDirection
            .clone()
            .rotateY((float) (Math.PI / 2));
        BulletJump.LOGGER.atFine().log(
            "leftMost    %f, %f \n" + "rightMost %f, %f",
            leftmostDirection.getX(),
            leftmostDirection.getZ(),
            rightmostDirection.getX(),
            rightmostDirection.getZ()
        );

        // Our rotation could have landed us in any
        // of the four quadrants of the coordinate
        // grid, and we can't guarantee that by turning
        // right (bigger angle) we will get higher X or Z.
        // Thankfully, we can be sure that all smaller
        // rotations (like the ones we allow) will result
        // in coordinates that are inbetween the lesser
        // and the bigger value, no matter the direction.
        // Try this on paper if confused.
        double minX = Math.min(
            leftmostDirection.getX(),
            rightmostDirection.getX()
        );
        double maxX = Math.max(
            leftmostDirection.getX(),
            rightmostDirection.getX()
        );
        double minZ = Math.min(
            leftmostDirection.getZ(),
            rightmostDirection.getZ()
        );
        double maxZ = Math.max(
            leftmostDirection.getZ(),
            rightmostDirection.getZ()
        );
        BulletJump.LOGGER.atFine().log(
            "X    %f < %f < %f \n" + "Z %f < %f < %f",
            minX,
            lookDirection.getX(),
            maxX,
            minZ,
            lookDirection.getZ(),
            maxZ
        );
        // This way we ensure that our resulting vector coordinates are either
        // original (less than max, bigger than min), or the closest thing.
        baseVelocity.setX(Math.max(Math.min(lookDirection.getX(), maxX), minX));
        baseVelocity.setZ(Math.max(Math.min(lookDirection.getZ(), maxZ), minZ));
        return baseVelocity;
    }
}
