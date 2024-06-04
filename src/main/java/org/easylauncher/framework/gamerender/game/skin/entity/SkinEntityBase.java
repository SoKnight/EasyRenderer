package org.easylauncher.framework.gamerender.game.skin.entity;

import org.easylauncher.framework.gamerender.engine.object.Entity;

public abstract class SkinEntityBase extends Entity {

    SkinEntityBase(byte modelId, float dx, float dy) {
        super(modelId);
        move(dx, dy, 0F);
        updateTransformMatrix();
    }

}
