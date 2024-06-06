package org.easylauncher.renderer.engine;

import lombok.Getter;
import org.joml.Matrix4f;

@Getter
public final class Projection {

    private static final float FOV = 120.0F;
    private static final float Z_FAR = 1000.0F;
    private static final float Z_NEAR = 0.1F;

    private final Matrix4f projectionMatrix;

    public Projection(int width, int height) {
        this.projectionMatrix = new Matrix4f();
        updateProjMatrix(width, height);
    }

    public void updateProjMatrix(int width, int height) {
        projectionMatrix.setPerspective(FOV, (float) width / height, Z_NEAR, Z_FAR);
    }

}
