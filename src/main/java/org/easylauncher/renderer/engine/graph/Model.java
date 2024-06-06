package org.easylauncher.renderer.engine.graph;

import lombok.Getter;
import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.engine.graph.mesh.Mesh;
import org.easylauncher.renderer.engine.type.Cleanable;

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
    protected final Mesh[] meshes;
    private final List<Entity> entities;

    public Model(byte modelId, Mesh... meshes) {
        this.modelId = modelId;
        this.meshes = meshes;
        this.entities = new ArrayList<>();
    }

    @Override
    public void cleanup() {
        for (Mesh mesh : meshes)
            mesh.cleanup();
    }

    public Mesh[] getRenderableMeshes(RenderOptions options) {
        return meshes;
    }

    public Entity createEntity(RenderOptions options) {
        return new Entity(modelId);
    }

}
