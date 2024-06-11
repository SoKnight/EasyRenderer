package org.easylauncher.renderer.game.cape;

import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.engine.exception.shader.ShaderGLException;
import org.easylauncher.renderer.engine.graph.Material;
import org.easylauncher.renderer.engine.graph.Model;
import org.easylauncher.renderer.engine.scene.Scene;
import org.easylauncher.renderer.state.MaterialDesiring;
import org.easylauncher.renderer.util.MeshRenderFunction;

public class CapeModel extends Model implements MaterialDesiring {

    public static final int WIDTH = 10;
    public static final int HEIGHT = 16;
    public static final int DEPTH = 1;

    public CapeModel() {
        super(MODEL_ID_CAPE, 0);
        addMesh(new CapeMesh());
    }

    @Override
    public Material getDesiredMaterial(Scene scene) {
        return scene.getCapeMaterial();
    }

    @Override
    public void renderMeshes(RenderOptions options, MeshRenderFunction renderFunction) throws ShaderGLException {
        if (options.showCape())
            renderFunction.render(mesh());
    }

    @Override
    public CapeEntity createEntity(RenderOptions options) {
        return new CapeEntity();
    }

}
