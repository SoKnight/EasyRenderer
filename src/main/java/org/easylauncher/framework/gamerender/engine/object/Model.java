package org.easylauncher.framework.gamerender.engine.object;

import lombok.Getter;
import org.easylauncher.framework.gamerender.RenderOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class Model {

    public static final byte MODEL_ID_HEAD = 1;
    public static final byte MODEL_ID_BODY = 2;
    public static final byte MODEL_ID_LEFT_ARM = 3;
    public static final byte MODEL_ID_RIGHT_ARM = 4;
    public static final byte MODEL_ID_LEFT_LEG = 5;
    public static final byte MODEL_ID_RIGHT_LEG = 6;

    protected final byte modelId;
    protected final List<Mesh> meshes;
    private final List<Entity> entities;

    public Model(byte modelId, Mesh... meshes) {
        this.modelId = modelId;
        this.meshes = Arrays.asList(meshes);
        this.entities = new ArrayList<>();
    }

    public List<Mesh> getRenderableMeshes(RenderOptions options) {
        return meshes;
    }

    public void cleanup() {
        meshes.forEach(Mesh::cleanup);
    }

    public Entity createEntity() {
        return new Entity(modelId);
    }

}
