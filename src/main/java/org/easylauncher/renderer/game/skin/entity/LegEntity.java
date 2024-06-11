package org.easylauncher.renderer.game.skin.entity;

import org.easylauncher.renderer.state.Animated;

import static org.easylauncher.renderer.engine.graph.Model.MODEL_ID_LEFT_LEG;
import static org.easylauncher.renderer.engine.graph.Model.MODEL_ID_RIGHT_LEG;

public class LegEntity extends SkinEntityBase implements Animated {

    private final boolean left;

    public LegEntity(boolean left) {
        super(
                left ? MODEL_ID_LEFT_LEG : MODEL_ID_RIGHT_LEG,
                left ? 2.01F : -2.01F,
                -10F
        );

        this.left = left;

        getRotationX().setAngleDeg(left ? -20F : 20F);
        getRotationX().setPivot(0F, 6F, 0F);
        updateRotationMatrix();
    }

    @Override
    public void updateAnimation(float timeFactor) {
        getRotationX().setAngleDeg((left ? 40F : -40F) * timeFactor);
        updateRotationMatrix();
    }

}
