package org.easylauncher.renderer.game.skin.entity;

import lombok.Getter;

import static org.easylauncher.renderer.engine.graph.Model.MODEL_ID_LEFT_ARM;
import static org.easylauncher.renderer.engine.graph.Model.MODEL_ID_RIGHT_ARM;

@Getter
public class ArmEntity extends SkinEntityBase {

    private final boolean left;
    private boolean thin;

    public ArmEntity(boolean left, boolean thin) {
        super(
                left ? MODEL_ID_LEFT_ARM : MODEL_ID_RIGHT_ARM,
                computePositionX(4F, left, thin),
                2F
        );

        this.left = left;
        this.thin = thin;

        getRotationX().setAngleDeg(left ? 18F : -18F);
        getRotationX().setPivot(-computePositionX(0F, left, thin), 4F, 0F);
        updateRotationMatrix();
    }

    public void updateThickness(boolean thin) {
        if (this.thin != thin) {
            this.thin = thin;

            move(computePositionX(4F, left, thin), 2F, 0F);
            updateTransformMatrix();

            getRotationX().setPivot(-computePositionX(0F, left, thin), 4F, 0F);
            updateRotationMatrix();
        }
    }

    private static float computePositionX(float base, boolean left, boolean slim) {
        base += (slim ? 3F : 4F) / 2F;
        return left ? base : -base;
    }

}
