package com.bwt.utils.rectangular_entity;

import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public record EntityRectDimensions(float width, float length, float height, float eyeHeight) {
    public static EntityRectDimensions fixed(float width, float height, float length) {
        return new EntityRectDimensions(width, height, length, height / 2f);
    }

    public Box getBoxAt(Vec3d pos) {
        return getBoxAt(pos.x, pos.y, pos.z);
    }

    public Box getBoxAt(double x, double y, double z) {
        double half_width = this.width / 2.0f;
        double half_length = this.length / 2.0f;
        return new Box(
                x - half_width,
                y,
                z - half_length,
                x + half_width,
                y + (double)this.height,
                z + half_length
        );
    }
}
