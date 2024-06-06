package org.easylauncher.renderer.game.cape;

import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.engine.graph.Material;
import org.easylauncher.renderer.engine.graph.Model;
import org.easylauncher.renderer.engine.graph.mesh.Mesh;
import org.easylauncher.renderer.engine.scene.Scene;
import org.easylauncher.renderer.state.MaterialDesiring;

public class CapeModel extends Model implements MaterialDesiring {

    public static final int WIDTH = 10;
    public static final int HEIGHT = 16;
    public static final int DEPTH = 1;

    public CapeModel() {
        super(MODEL_ID_CAPE, new CapeMesh());
    }

    @Override
    public Material getDesiredMaterial(Scene scene) {
        return scene.getCapeMaterial();
    }

    @Override
    public Mesh[] getRenderableMeshes(RenderOptions options) {
        return options.showCape() ? meshes : null;
    }

    @Override
    public CapeEntity createEntity(RenderOptions options) {
        return new CapeEntity();
    }

}
