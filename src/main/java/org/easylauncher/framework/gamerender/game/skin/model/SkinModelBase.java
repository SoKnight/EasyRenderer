package org.easylauncher.framework.gamerender.game.skin.model;

import lombok.Getter;
import org.easylauncher.framework.gamerender.RenderOptions;
import org.easylauncher.framework.gamerender.RenderOptions.SkinLayer;
import org.easylauncher.framework.gamerender.engine.object.Mesh;
import org.easylauncher.framework.gamerender.engine.object.Model;

import java.util.Collections;
import java.util.List;

@Getter
public abstract class SkinModelBase extends Model {

    private final SkinLayer skinLayer;

    SkinModelBase(byte modelId, SkinLayer skinLayer, Mesh... meshes) {
        super(modelId, meshes);
        this.skinLayer = skinLayer;
    }

    @Override
    public List<Mesh> getRenderableMeshes(RenderOptions options) {
        boolean inner = options.isInnerLayerVisible(skinLayer);
        boolean outer = options.isOuterLayerVisible(skinLayer);

        if (inner || outer) {
            if (inner && outer)
                return meshes;

            return Collections.singletonList(inner ? meshes.getFirst() : meshes.getLast());
        }

        return null;
    }

}
