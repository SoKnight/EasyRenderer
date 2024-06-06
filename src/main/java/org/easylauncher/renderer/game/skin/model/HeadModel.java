package org.easylauncher.renderer.game.skin.model;

import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.context.SkinPart;
import org.easylauncher.renderer.game.skin.SkinPartMesh;
import org.easylauncher.renderer.game.skin.entity.HeadEntity;

public class HeadModel extends SkinModelBase {

    public HeadModel() {
        super(
                MODEL_ID_HEAD,
                SkinPart.HEAD,
                new SkinPartMesh(8, 8, 8, 0F, 0, 0, false),
                new SkinPartMesh(8, 8, 8, 0.5F, 32, 0, true)
        );
    }

    @Override
    public HeadEntity createEntity(RenderOptions options) {
        return new HeadEntity();
    }

}
