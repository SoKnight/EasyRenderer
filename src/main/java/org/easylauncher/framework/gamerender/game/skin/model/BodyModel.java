package org.easylauncher.framework.gamerender.game.skin.model;

import org.easylauncher.framework.gamerender.RenderOptions.SkinLayer;
import org.easylauncher.framework.gamerender.game.skin.SkinPartMesh;
import org.easylauncher.framework.gamerender.game.skin.entity.BodyEntity;

public class BodyModel extends SkinModelBase {

    public BodyModel() {
        super(
                MODEL_ID_BODY,
                SkinLayer.BODY,
                SkinPartMesh.create(8, 12, 4, 16, 16, 0F, false),
                SkinPartMesh.create(8, 12, 4, 16, 32, 0.25F, true)
        );
    }

    @Override
    public BodyEntity createEntity() {
        return new BodyEntity();
    }

}
