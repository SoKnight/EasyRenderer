package org.easylauncher.renderer.game.skin.model;

import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.context.SkinPart;
import org.easylauncher.renderer.game.skin.SkinPartMesh;
import org.easylauncher.renderer.game.skin.entity.HeadEntity;

public class HeadModel extends SkinModelBase {

    public HeadModel() {
        super(MODEL_ID_HEAD, SkinPart.HEAD, 2);

        addMesh(new SkinPartMesh(8, 8, 8, 0F, 0, 0, false, false, false), false, false);    // inner, modern
        addMesh(new SkinPartMesh(8, 8, 8, 0F, 0, 0, false, false, true), false, true);      // inner, legacy
        addMesh(new SkinPartMesh(8, 8, 8, 0.5F, 32, 0, true, false, false), true, false);   // outer, modern
        addMesh(new SkinPartMesh(8, 8, 8, 0.5F, 32, 0, true, false, true), true, true);     // outer, legacy
    }

    @Override
    public HeadEntity createEntity(RenderOptions options) {
        return new HeadEntity();
    }

}
