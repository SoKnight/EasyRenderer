package org.easylauncher.renderer.engine.scene;

import lombok.Getter;
import lombok.Setter;
import org.easylauncher.renderer.engine.Camera;
import org.easylauncher.renderer.engine.Projection;
import org.easylauncher.renderer.engine.exception.texture.TextureLoadException;
import org.easylauncher.renderer.engine.graph.Entity;
import org.easylauncher.renderer.engine.graph.Material;
import org.easylauncher.renderer.engine.graph.Model;
import org.easylauncher.renderer.engine.graph.Rotation;
import org.easylauncher.renderer.engine.graph.texture.Texture;
import org.easylauncher.renderer.engine.graph.texture.TextureLoader;
import org.easylauncher.renderer.state.Cleanable;
import org.joml.Matrix4f;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public final class Scene implements Cleanable {

    private final Projection projection;
    private final Camera camera;
    private final SceneLights sceneLights;

    private final Matrix4f rotationMatrix;
    private final Rotation rotationX;
    private final Rotation rotationY;

    private final Map<Byte, Model> models;
    private final Material capeMaterial;
    private final Material skinMaterial;

    private Texture alexSkinTexture;
    private Texture steveSkinTexture;

    public Scene(int width, int height, Camera camera, SceneLights sceneLights) {
        this.projection = new Projection(width, height);
        this.camera = camera;
        this.sceneLights = sceneLights;

        this.rotationMatrix = new Matrix4f();
        this.rotationX = new Rotation(Rotation.Axis.X);
        this.rotationY = new Rotation(Rotation.Axis.Y);

        this.models = new HashMap<>();
        this.capeMaterial = new Material();
        this.skinMaterial = new Material();
    }

    @Override
    public void cleanup() {
        models.values().forEach(Model::cleanup);
        cleanupTexture(alexSkinTexture);
        cleanupTexture(steveSkinTexture);
        cleanupTexture(capeMaterial.getTexture());
        cleanupTexture(skinMaterial.getTexture());
    }

    public Texture getAlexSkinTexture() throws TextureLoadException {
        if (alexSkinTexture == null)
            this.alexSkinTexture = loadDefaultSkinTexture("alex");

        return alexSkinTexture;
    }

    public Texture getSteveSkinTexture() throws TextureLoadException {
        if (steveSkinTexture == null)
            this.steveSkinTexture = loadDefaultSkinTexture("steve");

        return steveSkinTexture;
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

    public void addModels(Model... models) {
        for (Model model : models) {
            addModel(model);
        }
    }

    public void resize(int width, int height) {
        projection.updateProjMatrix(width, height);
    }

    public void updateRotationMatrix() {
        rotationMatrix.identity();
        rotationX.apply(rotationMatrix);
        rotationY.apply(rotationMatrix);
    }

    private void cleanupTexture(Texture texture) {
        if (texture != null) {
            texture.cleanup();
        }
    }

    private Texture loadDefaultSkinTexture(String name) throws TextureLoadException {
        URL resource = getClass().getResource("/defaults/%s.png".formatted(name));
        return TextureLoader.loadFrom(resource);
    }

}
