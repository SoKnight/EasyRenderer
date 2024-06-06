package org.easylauncher.renderer.engine.graph;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

@Getter
public final class Rotation {

    private final Axis axis;
    private final Vector3f pivot;
    private final Quaternionf quat;

    public Rotation(Axis axis) {
        this.axis = axis;
        this.pivot = new Vector3f();
        this.quat = new Quaternionf();
    }

    public void apply(Matrix4f matrix) {
        matrix.rotateAround(quat, pivot.x, pivot.y, pivot.z);
    }

    public boolean hasRotation() {
        return quat.angle() != 0F;
    }

    public void setPivot(float x, float y, float z) {
        pivot.set(x,y,z);
    }

    public void setAngleDeg(float angle) {
        quat.fromAxisAngleDeg(axis.axis, angle);
    }

    public void setAngleRad(float angle) {
        quat.fromAxisAngleRad(axis.axis, angle);
    }

    @AllArgsConstructor
    public enum Axis {

        X(new Vector3f(1F, 0F, 0F)),
        Y(new Vector3f(0F, 1F, 0F)),
        Z(new Vector3f(0F, 0F, 1F)),
        ;

        private final Vector3fc axis;

    }

}
