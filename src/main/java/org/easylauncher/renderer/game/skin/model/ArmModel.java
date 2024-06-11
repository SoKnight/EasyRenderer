package org.easylauncher.renderer.game.skin.model;

import lombok.Getter;
import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.context.SkinPart;
import org.easylauncher.renderer.engine.exception.shader.ShaderGLException;
import org.easylauncher.renderer.game.skin.SkinPartMesh;
import org.easylauncher.renderer.game.skin.entity.ArmEntity;
import org.easylauncher.renderer.util.MeshRenderFunction;

@Getter
public class ArmModel extends SkinModelBase {

    private final boolean left;

    public ArmModel(boolean left) {
        super(left ? MODEL_ID_LEFT_ARM : MODEL_ID_RIGHT_ARM, left ? SkinPart.LEFT_ARM : SkinPart.RIGHT_ARM, 3);

        this.left = left;

        if (left) {
            // --- thick (default)
            addMesh(new SkinPartMesh(4, 12, 4, 0F, 32, 48, false, false, false), false, false, false);  // inner, modern
            addMesh(new SkinPartMesh(4, 12, 4, 0F, 40, 16, false, true, true), false, true, false);     // inner, legacy

            addMesh(new SkinPartMesh(4, 12, 4, 0.25F, 48, 48, true, false, false), true, false, false); // outer, modern
            // no mesh: outer, legacy

            // --- thin (slim)
            addMesh(new SkinPartMesh(3, 12, 4, 0F, 32, 48, false, false, false), false, false, true);   // inner, modern
            addMesh(new SkinPartMesh(3, 12, 4, 0F, 40, 16, false, true, true), false, true, true);      // inner, legacy

            addMesh(new SkinPartMesh(3, 12, 4, 0.25F, 48, 48, true, false, false), true, false, true);  // outer, modern
            // no mesh: outer, legacy
        } else {
            // --- thick (default)
            addMesh(new SkinPartMesh(4, 12, 4, 0F, 40, 16, false, false, false), false, false, false);  // inner, modern
            addMesh(new SkinPartMesh(4, 12, 4, 0F, 40, 16, false, false, true), false, true, false);    // inner, legacy

            addMesh(new SkinPartMesh(4, 12, 4, 0.25F, 40, 32, true, false, false), true, false, false); // outer, modern
            // no mesh: outer, legacy

            // --- thin (slim)
            addMesh(new SkinPartMesh(3, 12, 4, 0F, 40, 16, false, false, false), false, false, true);   // inner, modern
            addMesh(new SkinPartMesh(3, 12, 4, 0F, 40, 16, false, false, true), false, true, true);     // inner, legacy

            addMesh(new SkinPartMesh(3, 12, 4, 0.25F, 40, 32, true, false, false), true, false, true);  // outer, modern
            // no mesh: outer, legacy
        }
    }

    @Override
    public void renderMeshes(RenderOptions options, MeshRenderFunction renderFunction) throws ShaderGLException {
        if (options.isInnerLayerVisible(skinPart))
            renderFunction.render(mesh(false, options.legacySkinTexture(), options.skinThinArms()));

        if (options.isOuterLayerVisible(skinPart))
            renderFunction.render(mesh(true, options.legacySkinTexture(), options.skinThinArms()));
    }

    @Override
    public ArmEntity createEntity(RenderOptions options) {
        return new ArmEntity(left, options.skinThinArms());
    }

}
