package org.easylauncher.renderer.game.skin.model;

import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.context.SkinPart;
import org.easylauncher.renderer.game.skin.SkinPartMesh;
import org.easylauncher.renderer.game.skin.entity.BodyEntity;

public class BodyModel extends SkinModelBase {

    public BodyModel() {
        super(MODEL_ID_BODY, SkinPart.BODY, 2);

        addMesh(new SkinPartMesh(8, 12, 4, 0F, 16, 16, false, false, false), false, false);     // inner, modern
        addMesh(new SkinPartMesh(8, 12, 4, 0F, 16, 16, false, false, true), false, true);       // inner, legacy

        addMesh(new SkinPartMesh(8, 12, 4, 0.25F, 16, 32, true, false, false), true, false);    // outer, modern
        // no mesh: outer, legacy
    }

    @Override
    public BodyEntity createEntity(RenderOptions options) {
        return new BodyEntity();
    }

}
