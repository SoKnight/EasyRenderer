package org.easylauncher.renderer.engine.graph;

import lombok.Getter;
import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.engine.exception.shader.ShaderGLException;
import org.easylauncher.renderer.engine.graph.mesh.Mesh;
import org.easylauncher.renderer.state.Cleanable;
import org.easylauncher.renderer.util.StateTensor;
import org.easylauncher.renderer.util.MeshRenderFunction;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Model implements Cleanable {

    public static final byte MODEL_ID_HEAD = 1;
    public static final byte MODEL_ID_BODY = 2;
    public static final byte MODEL_ID_LEFT_ARM = 3;
    public static final byte MODEL_ID_RIGHT_ARM = 4;
    public static final byte MODEL_ID_LEFT_LEG = 5;
    public static final byte MODEL_ID_RIGHT_LEG = 6;
    public static final byte MODEL_ID_CAPE = 7;

    protected final byte modelId;
    protected final StateTensor<Mesh> meshes;
    private final List<Entity> entities;

    public Model(byte modelId, int tensorSize) {
        this.modelId = modelId;
        this.meshes = new StateTensor<>(tensorSize, Mesh[]::new);
        this.entities = new ArrayList<>();
    }

    @Override
    public void cleanup() {
        for (Mesh mesh : meshes.items()) {
            if (mesh != null) {
                mesh.cleanup();
            }
        }
    }

    public void renderMeshes(RenderOptions options, MeshRenderFunction renderFunction) throws ShaderGLException {
        for (Mesh mesh : meshes.items()) {
            renderFunction.render(mesh);
        }
    }

    public Entity createEntity(RenderOptions options) {
        return new Entity(modelId);
    }

    protected final Mesh mesh(boolean... states) {
        return meshes.get(states);
    }

    protected final void addMesh(Mesh mesh, boolean... states) {
        meshes.set(mesh, states);
    }

}
