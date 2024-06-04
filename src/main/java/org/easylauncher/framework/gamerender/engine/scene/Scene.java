package org.easylauncher.framework.gamerender.engine.scene;

import lombok.Getter;
import lombok.Setter;
import org.easylauncher.framework.gamerender.engine.Camera;
import org.easylauncher.framework.gamerender.engine.object.Entity;
import org.easylauncher.framework.gamerender.engine.object.Material;
import org.easylauncher.framework.gamerender.engine.object.Model;
import org.easylauncher.framework.gamerender.engine.object.Rotation;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public final class Scene {

    private final Map<Byte, Model> models;
    private final SceneLights sceneLights;
    private final Projection projection;
    private final Camera camera;

    private final Matrix4f rotationMatrix;
    private final Rotation rotationX;
    private final Rotation rotationY;

    private Material skinMaterial;
    private Material capeMaterial;

    public Scene(int width, int height) {
        this.models = new HashMap<>();
        this.sceneLights = new SceneLights();
        this.projection = new Projection(width, height);
        this.camera = new Camera();

        this.rotationMatrix = new Matrix4f();
        this.rotationX = new Rotation(Rotation.Axis.X);
        this.rotationY = new Rotation(Rotation.Axis.Y);

        rotationX.setAngleDeg(21F);
        rotationY.setAngleDeg(-30F);
        updateRotationMatrix();
    }

    public void cleanup() {
        models.values().forEach(Model::cleanup);
    }

    public void addEntity(Entity entity) {
        byte modelId = entity.getModelId();
        Model model = models.get(modelId);
        if (model == null)
            throw new IllegalArgumentException("Model not found: %d".formatted(modelId));

        model.getEntities().add(entity);
    }

    public void addModel(Model model) {
        models.put(model.getModelId(), model);
    }

    public void resize(int width, int height) {
        projection.updateProjMatrix(width, height);
    }

    public void updateRotationMatrix() {
        rotationX.apply(rotationMatrix);
        rotationY.apply(rotationMatrix);
    }

}
