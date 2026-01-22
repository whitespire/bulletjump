package dev.whitespire.util;

import com.hypixel.hytale.math.vector.Vector3d;

public final class BulletJumpPhysics {

    // Will leave some comments here since we're doing math.
    // We're trying to get a vector for the boost, which is
    // plain horizontal (won't pull you up or down), and is
    // locked to 90 degrees left or right from where you are
    // currently heading to.
    public static Vector3d computeAirSlideBaseBoostVelocity(
        Vector3d lookDirection,
        Vector3d bodyDirection
    ) {
        Vector3d baseVelocity = new Vector3d();
        // Y velocity is 0, since we're neither going
        // up or down
        baseVelocity.setY(0);

        // Get the leftmost and rightmost directions
        // we can go by rotating our body direction
        // 90 degrees left and right
        Vector3d leftmostDirection = bodyDirection.rotateY(
            (float) (-Math.PI / 2)
        );
        Vector3d rightmostDirection = bodyDirection.rotateY(
            (float) (Math.PI / 2)
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
        // This way we ensure that our resulting vector coordinates are either
        // original (less than max, bigger than min), or the closest thing.
        baseVelocity.setX(Math.max(Math.min(lookDirection.getX(), maxX), minX));
        baseVelocity.setZ(Math.max(Math.min(lookDirection.getZ(), maxZ), minZ));
        return baseVelocity;
    }
}
