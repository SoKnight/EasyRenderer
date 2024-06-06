package org.easylauncher.renderer.engine.light;

import org.joml.Vector3f;

public record AmbientLight(Vector3f color, float intensity) {

    public void setColor(float red, float green, float blue) {
        this.color.set(red, green, blue);
    }

}
