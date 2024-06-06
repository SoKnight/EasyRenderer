package org.easylauncher.renderer.game.skin.entity;

import static org.easylauncher.renderer.engine.graph.Model.MODEL_ID_LEFT_LEG;
import static org.easylauncher.renderer.engine.graph.Model.MODEL_ID_RIGHT_LEG;

public class LegEntity extends SkinEntityBase {

    public LegEntity(boolean left) {
        super(
                left ? MODEL_ID_LEFT_LEG : MODEL_ID_RIGHT_LEG,
                left ? 2.01F : -2.01F,
                -10F
        );

        getRotationX().setAngleDeg(left ? -20F : 20F);
        getRotationX().setPivot(0F, 6F, 0F);
        updateRotationMatrix();
    }

}
