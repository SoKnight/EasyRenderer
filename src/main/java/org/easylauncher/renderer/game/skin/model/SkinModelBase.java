package org.easylauncher.renderer.game.skin.model;

import lombok.Getter;
import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.context.SkinPart;
import org.easylauncher.renderer.engine.graph.Material;
import org.easylauncher.renderer.engine.graph.Model;
import org.easylauncher.renderer.engine.graph.mesh.Mesh;
import org.easylauncher.renderer.engine.scene.Scene;
import org.easylauncher.renderer.engine.type.MaterialDesiring;

@Getter
public abstract class SkinModelBase extends Model implements MaterialDesiring {

    private final SkinPart skinPart;

    SkinModelBase(byte modelId, SkinPart skinPart, Mesh... meshes) {
        super(modelId, meshes);
        this.skinPart = skinPart;
    }

    protected Mesh[] getRelevantMeshes(RenderOptions options) {
        return meshes;
    }

    @Override
    public Material getDesiredMaterial(Scene scene) {
        return scene.getSkinMaterial();
    }

    @Override
    public Mesh[] getRenderableMeshes(RenderOptions options) {
        boolean inner = options.isInnerLayerVisible(skinPart);
        boolean outer = options.isOuterLayerVisible(skinPart);

        if (inner || outer) {
            Mesh[] meshes = getRelevantMeshes(options);
            if (inner && outer)
                return meshes;

            return new Mesh[]{ meshes[inner ? 0 : 1] };
        }

        return null;
    }

}
