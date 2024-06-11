package org.easylauncher.renderer.game.cape;

import org.easylauncher.renderer.engine.graph.Entity;
import org.easylauncher.renderer.state.Animated;

import static org.easylauncher.renderer.engine.graph.Model.MODEL_ID_CAPE;

public class CapeEntity extends Entity implements Animated {

    public CapeEntity() {
        super(MODEL_ID_CAPE);

        move(0F, 0F, -2.5F);
        updateTransformMatrix();

        getRotationX().setAngleDeg(18F);
        getRotationX().setPivot(0F, 8F, 0F);
        getRotationY().setAngleDeg(180F);
        getRotationY().setPivot(0F, 0F, 0F);
        updateRotationMatrix();
    }

    @Override
    public void updateAnimation(float timeFactor) {
        getRotationX().setAngleDeg(18F - (float) (6D * timeFactor));
        updateRotationMatrix();
    }

}
