package org.easylauncher.renderer.game.skin.entity;

import static org.easylauncher.renderer.engine.graph.Model.MODEL_ID_LEFT_ARM;
import static org.easylauncher.renderer.engine.graph.Model.MODEL_ID_RIGHT_ARM;

public class ArmEntity extends SkinEntityBase {

    public ArmEntity(boolean left, boolean slim) {
        super(
                left ? MODEL_ID_LEFT_ARM : MODEL_ID_RIGHT_ARM,
                computePositionX(4F, left, slim),
                2F
        );

        getRotationX().setAngleDeg(left ? 18F : -18F);
        getRotationX().setPivot(-computePositionX(0F, left, slim), 4F, 0F);
        updateRotationMatrix();
    }

    private static float computePositionX(float base, boolean left, boolean slim) {
        base += (slim ? 3F : 4F) / 2F;
        return left ? base : -base;
    }

}
