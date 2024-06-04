package org.easylauncher.framework.gamerender.engine.light;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.joml.Vector3f;

@Data
@AllArgsConstructor
public class DirectionalLight {

    private final Vector3f color;
    private final Vector3f direction;
    private float intensity;

    public void setColor(float red, float green, float blue) {
        this.color.set(red, green, blue);
    }

    public void setDirection(float x, float y, float z) {
        this.direction.set(x, y, z);
    }

}
