package org.easylauncher.framework.gamerender.game.skin.model;

import lombok.Getter;
import org.easylauncher.framework.gamerender.RenderOptions.SkinLayer;
import org.easylauncher.framework.gamerender.game.skin.SkinPartMesh;
import org.easylauncher.framework.gamerender.game.skin.entity.ArmEntity;

@Getter
public class ArmModel extends SkinModelBase {

    private final boolean left;
    private final boolean slim;

    public ArmModel(boolean left, boolean slim) {
        super(
                left ? MODEL_ID_LEFT_ARM : MODEL_ID_RIGHT_ARM,
                left ? SkinLayer.LEFT_ARM : SkinLayer.RIGHT_ARM,
                SkinPartMesh.create((slim ? 3 : 4), 12, 4, (left ? 32 : 40), (left ? 48 : 16), 0F, false),
                SkinPartMesh.create((slim ? 3 : 4), 12, 4, (left ? 48 : 40), (left ? 48 : 32), 0.25F, true)
        );

        this.left = left;
        this.slim = slim;
    }

    @Override
    public ArmEntity createEntity() {
        return new ArmEntity(left, slim);
    }

}
