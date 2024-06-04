package org.easylauncher.framework.gamerender.engine.object;

import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Vector4f;

@Data
@Accessors(chain = true)
public class Material {

    public static final Vector4f DEFAULT_AMBIENT_COLOR = new Vector4f(0F, 0F, 0F, 1F);
    public static final Vector4f DEFAULT_DIFFUSE_COLOR = new Vector4f(0F, 0F, 0F, 1F);
    public static final Vector4f DEFAULT_SPECULAR_COLOR = new Vector4f(0F, 0F, 0F, 0F);
    public static final float DEFAULT_REFLECTANCE = 0F;

    private Texture texture;
    private Vector4f ambientColor;
    private Vector4f diffuseColor;
    private Vector4f specularColor;
    private float reflectance;

    public Material() {
        this.ambientColor = DEFAULT_AMBIENT_COLOR;
        this.diffuseColor = DEFAULT_DIFFUSE_COLOR;
        this.specularColor = DEFAULT_SPECULAR_COLOR;
        this.reflectance = DEFAULT_REFLECTANCE;
    }

}
