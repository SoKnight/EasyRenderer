package org.easylauncher.renderer.game.skin.model;

import lombok.Getter;
import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.context.SkinPart;
import org.easylauncher.renderer.engine.exception.shader.ShaderGLException;
import org.easylauncher.renderer.engine.graph.Material;
import org.easylauncher.renderer.engine.graph.Model;
import org.easylauncher.renderer.engine.scene.Scene;
import org.easylauncher.renderer.state.MaterialDesiring;
import org.easylauncher.renderer.util.MeshRenderFunction;

@Getter
public abstract class SkinModelBase extends Model implements MaterialDesiring {

    protected final SkinPart skinPart;

    SkinModelBase(byte modelId, SkinPart skinPart, int tensorSize) {
        super(modelId, tensorSize);
        this.skinPart = skinPart;
    }

    @Override
    public Material getDesiredMaterial(Scene scene) {
        return scene.getSkinMaterial();
    }

    @Override
    public void renderMeshes(RenderOptions options, MeshRenderFunction renderFunction) throws ShaderGLException {
        if (options.isInnerLayerVisible(skinPart))
            renderFunction.render(mesh(false, options.legacySkinTexture()));

        if (options.isOuterLayerVisible(skinPart))
            renderFunction.render(mesh(true, options.legacySkinTexture()));
    }

}
