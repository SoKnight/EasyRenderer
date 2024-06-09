package org.easylauncher.renderer.game.skin.model;

import lombok.Getter;
import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.context.SkinPart;
import org.easylauncher.renderer.engine.graph.mesh.Mesh;
import org.easylauncher.renderer.game.skin.SkinPartMesh;
import org.easylauncher.renderer.game.skin.entity.ArmEntity;

@Getter
public class ArmModel extends SkinModelBase {

    private final boolean left;

    private final Mesh[] thinArmsMeshes;
    private final Mesh[] thickArmsMeshes;

    public ArmModel(boolean left) {
        super(
                left ? MODEL_ID_LEFT_ARM : MODEL_ID_RIGHT_ARM,
                left ? SkinPart.LEFT_ARM : SkinPart.RIGHT_ARM,
                new SkinPartMesh(3, 12, 4, 0F, (left ? 32 : 40), (left ? 48 : 16), false),
                new SkinPartMesh(4, 12, 4, 0F, (left ? 32 : 40), (left ? 48 : 16), false),
                new SkinPartMesh(3, 12, 4, 0.25F, (left ? 48 : 40), (left ? 48 : 32), true),
                new SkinPartMesh(4, 12, 4, 0.25F, (left ? 48 : 40), (left ? 48 : 32), true)
        );

        this.left = left;

        this.thinArmsMeshes = new Mesh[] { meshes[0], meshes[2] };
        this.thickArmsMeshes = new Mesh[] { meshes[1], meshes[3] };
    }

    @Override
    protected Mesh[] getRelevantMeshes(RenderOptions options) {
        return options.skinThinArms() ? thinArmsMeshes : thickArmsMeshes;
    }

    @Override
    public ArmEntity createEntity(RenderOptions options) {
        return new ArmEntity(left, options.skinThinArms());
    }

}
