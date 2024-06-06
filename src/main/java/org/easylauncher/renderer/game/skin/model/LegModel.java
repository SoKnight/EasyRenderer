package org.easylauncher.renderer.game.skin.model;

import lombok.Getter;
import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.context.SkinPart;
import org.easylauncher.renderer.game.skin.SkinPartMesh;
import org.easylauncher.renderer.game.skin.entity.LegEntity;

@Getter
public class LegModel extends SkinModelBase {

    private final boolean left;

    public LegModel(boolean left) {
        super(
                left ? MODEL_ID_LEFT_LEG : MODEL_ID_RIGHT_LEG,
                left ? SkinPart.LEFT_LEG : SkinPart.RIGHT_LEG,
                new SkinPartMesh(4, 12, 4, 0F, (left ? 16 : 0), (left ? 48 : 16), false),
                new SkinPartMesh(4, 12, 4, 0.25F, 0, (left ? 48 : 32), true)
        );

        this.left = left;
    }

    @Override
    public LegEntity createEntity(RenderOptions options) {
        return new LegEntity(left);
    }

}
