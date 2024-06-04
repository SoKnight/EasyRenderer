package org.easylauncher.framework.gamerender.engine.light;

import lombok.Data;
import org.joml.Vector3f;

@Data
public class AmbientLight {

    private final Vector3f color;
    private float intensity;

    public AmbientLight(Vector3f color, float intensity) {
        this.color = color;
        this.intensity = intensity;
    }

    public AmbientLight() {
        this(new Vector3f(1F, 1F, 1F), 0.7F);
    }

    public void setColor(float red, float green, float blue) {
        this.color.set(red, green, blue);
    }

}
