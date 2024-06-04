package org.easylauncher.framework.gamerender.game.skin.entity;

import static org.easylauncher.framework.gamerender.engine.object.Model.*;

public class LegEntity extends SkinEntityBase {

    public LegEntity(boolean left) {
        super(
                left ? MODEL_ID_LEFT_LEG : MODEL_ID_RIGHT_LEG,
                left ? 1.9F : -1.9F,
                -10F
        );

        getRotationX().setAngleDeg(left ? -20F : 20F);
        getRotationX().setPivot(0F, 4F, 0F);
        updateRotationMatrix();
    }

}
