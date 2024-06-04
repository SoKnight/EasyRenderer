package org.easylauncher.framework.gamerender.game.skin.model;

import org.easylauncher.framework.gamerender.RenderOptions.SkinLayer;
import org.easylauncher.framework.gamerender.game.skin.SkinPartMesh;
import org.easylauncher.framework.gamerender.game.skin.entity.HeadEntity;

public class HeadModel extends SkinModelBase {

    public HeadModel() {
        super(
                MODEL_ID_HEAD,
                SkinLayer.HEAD,
                SkinPartMesh.create(8, 8, 8, 0, 0, 0F, false),
                SkinPartMesh.create(8, 8, 8, 32, 0, 0.5F, true)
        );
    }

    @Override
    public HeadEntity createEntity() {
        return new HeadEntity();
    }

}
