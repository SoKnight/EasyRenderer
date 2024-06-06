package org.easylauncher.renderer.game.skin.entity;

import org.easylauncher.renderer.engine.graph.Entity;

public abstract class SkinEntityBase extends Entity {

    SkinEntityBase(byte modelId, float dx, float dy) {
        super(modelId);

        move(dx, dy, 0F);
        updateTransformMatrix();
    }

}
