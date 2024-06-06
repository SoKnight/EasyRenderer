package org.easylauncher.renderer.engine.graph;

import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@Getter
@Setter
public class Entity {

    private final byte modelId;
    private final Matrix4f transformMatrix;
    private final Matrix4f rotationMatrix;
    private final Vector3f position;
    private final Rotation rotationX;
    private final Rotation rotationY;
    private final Rotation rotationZ;
    private float scale;

    public Entity(byte modelId) {
        this.modelId = modelId;
        this.transformMatrix = new Matrix4f();
        this.rotationMatrix = new Matrix4f();
        this.position = new Vector3f();
        this.rotationX = new Rotation(Rotation.Axis.X);
        this.rotationY = new Rotation(Rotation.Axis.Y);
        this.rotationZ = new Rotation(Rotation.Axis.Z);
        this.scale = 1;
    }

    public void move(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void updateTransformMatrix() {
        transformMatrix.translate(position);
        transformMatrix.scale(scale);
    }

    public void updateRotationMatrix() {
        rotationMatrix.identity();
        rotationX.apply(rotationMatrix);
        rotationY.apply(rotationMatrix);
        rotationZ.apply(rotationMatrix);
    }

}
