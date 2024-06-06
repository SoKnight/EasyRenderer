package org.easylauncher.renderer.engine.light;

import org.joml.Vector3f;

public record DirectedLight(Vector3f color, Vector3f direction, float intensity) {

    public void setColor(float red, float green, float blue) {
        this.color.set(red, green, blue);
    }

    public void setDirection(float x, float y, float z) {
        this.direction.set(x, y, z);
    }

}
