package org.easylauncher.renderer.game.skin.model;

import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.context.SkinPart;
import org.easylauncher.renderer.game.skin.SkinPartMesh;
import org.easylauncher.renderer.game.skin.entity.BodyEntity;

public class BodyModel extends SkinModelBase {

    public BodyModel() {
        super(
                MODEL_ID_BODY,
                SkinPart.BODY,
                new SkinPartMesh(8, 12, 4, 0F, 16, 16, false),
                new SkinPartMesh(8, 12, 4, 0.25F, 16, 32, true)
        );
    }

    @Override
    public BodyEntity createEntity(RenderOptions options) {
        return new BodyEntity();
    }

}
