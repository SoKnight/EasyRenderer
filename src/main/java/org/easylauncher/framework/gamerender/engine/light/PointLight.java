package org.easylauncher.framework.gamerender.engine.light;

import lombok.Data;
import org.joml.Vector3f;

@Data
public class PointLight {

    private final Attenuation attenuation;
    private final Vector3f color;
    private final Vector3f position;
    private float intensity;

    public PointLight(Vector3f color, Vector3f position, float intensity) {
        this.attenuation = new Attenuation(0F, 1F, 0F);
        this.color = color;
        this.position = position;
        this.intensity = intensity;
    }

    public void setColor(float red, float green, float blue) {
        this.color.set(red, green, blue);
    }

    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
    }

}
