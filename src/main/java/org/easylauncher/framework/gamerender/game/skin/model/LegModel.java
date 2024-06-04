package org.easylauncher.framework.gamerender.game.skin.model;

import lombok.Getter;
import org.easylauncher.framework.gamerender.RenderOptions.SkinLayer;
import org.easylauncher.framework.gamerender.game.skin.SkinPartMesh;
import org.easylauncher.framework.gamerender.game.skin.entity.LegEntity;

@Getter
public class LegModel extends SkinModelBase {

    private final boolean left;

    public LegModel(boolean left) {
        super(
                left ? MODEL_ID_LEFT_LEG : MODEL_ID_RIGHT_LEG,
                left ? SkinLayer.LEFT_LEG : SkinLayer.RIGHT_LEG,
                SkinPartMesh.create(4, 12, 4, (left ? 16 : 0), (left ? 48 : 16), 0F, false),
                SkinPartMesh.create(4, 12, 4, 0, (left ? 48 : 32), 0.25F, true)
        );

        this.left = left;
    }

    @Override
    public LegEntity createEntity() {
        return new LegEntity(left);
    }

}
