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
        super(left ? MODEL_ID_LEFT_LEG : MODEL_ID_RIGHT_LEG, left ? SkinPart.LEFT_LEG : SkinPart.RIGHT_LEG, 2);

        this.left = left;

        if (left) {
            addMesh(new SkinPartMesh(4, 12, 4, 0F, 16, 48, false, false, false), false, false); // inner, modern
            addMesh(new SkinPartMesh(4, 12, 4, 0F, 0, 16, false, true, true), false, true);     // inner, legacy

            addMesh(new SkinPartMesh(4, 12, 4, 0.25F, 0, 48, true, false, false), true, false); // outer, modern
            // no mesh: outer, legacy
        } else {
            addMesh(new SkinPartMesh(4, 12, 4, 0F, 0, 16, false, false, false), false, false);  // inner, modern
            addMesh(new SkinPartMesh(4, 12, 4, 0F, 0, 16, false, false, true), false, true);    // inner, legacy

            addMesh(new SkinPartMesh(4, 12, 4, 0.25F, 0, 32, true, false, false), true, false); // outer, modern
            // no mesh: outer, legacy
        }
    }

    @Override
    public LegEntity createEntity(RenderOptions options) {
        return new LegEntity(left);
    }

}
