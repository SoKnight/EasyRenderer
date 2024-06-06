package org.easylauncher.renderer.engine.scene;

import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.engine.Uniforms;
import org.easylauncher.renderer.engine.exception.shader.ShaderGLException;
import org.easylauncher.renderer.engine.exception.shader.ShaderLoadException;
import org.easylauncher.renderer.engine.graph.Entity;
import org.easylauncher.renderer.engine.graph.Material;
import org.easylauncher.renderer.engine.graph.Model;
import org.easylauncher.renderer.engine.graph.mesh.Mesh;
import org.easylauncher.renderer.engine.graph.texture.Texture;
import org.easylauncher.renderer.engine.light.AmbientLight;
import org.easylauncher.renderer.engine.light.DirectedLight;
import org.easylauncher.renderer.engine.shader.ShaderProgram;
import org.easylauncher.renderer.engine.type.Cleanable;
import org.easylauncher.renderer.engine.type.MaterialDesiring;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.easylauncher.renderer.engine.shader.ShaderType.FRAGMENT;
import static org.easylauncher.renderer.engine.shader.ShaderType.VERTEX;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public final class SceneRender implements Cleanable {

    private final ShaderProgram shaderProgram;
    private final Uniforms uniforms;
    private final RenderOptions renderOptions;

    public SceneRender(RenderOptions renderOptions) throws ShaderGLException, ShaderLoadException {
        this.shaderProgram = new ShaderProgram("renderer", VERTEX, FRAGMENT);
        this.uniforms = shaderProgram.uniforms();
        this.renderOptions = renderOptions;
        createUniforms(uniforms);
    }

    @Override
    public void cleanup() {
        shaderProgram.cleanup();
    }

    public void render(Scene scene) throws ShaderGLException {
        shaderProgram.bind();

        uniforms.put("projectionMatrix", scene.getProjection().getProjectionMatrix());
        uniforms.put("viewMatrix", scene.getCamera().getViewMatrix());
        uniforms.put("sceneRotationMatrix", scene.getRotationMatrix());
        uniforms.put("textureSampler", 0);

        updateLights(scene, uniforms);

        Collection<Model> models = scene.getModels().values();
        Material currentMaterial = null;

        for (Model model : models) {
            Material desiredMaterial = model instanceof MaterialDesiring cast ? cast.getDesiredMaterial(scene) : null;
            if (!Objects.equals(currentMaterial, desiredMaterial)) {
                currentMaterial = desiredMaterial;
                if (currentMaterial != null) {
                    Texture texture = currentMaterial.getTexture();
                    if (texture != null) {
                        glActiveTexture(GL_TEXTURE0);
                        texture.bind();
                    }
                } else {
                    glActiveTexture(GL_TEXTURE1);
                }
            }

            Mesh[] meshes = model.getRenderableMeshes(renderOptions);
            if (meshes != null) {
                for (Mesh mesh : meshes) {
                    List<Entity> entities = model.getEntities();
                    if (entities.isEmpty())
                        continue;

                    mesh.bind();
                    for (Entity entity : entities) {
                        uniforms.put("modelTransformMatrix", entity.getTransformMatrix());
                        uniforms.put("modelRotationMatrix", entity.getRotationMatrix());
                        mesh.draw(uniforms);
                    }
                }
            }
        }

        glBindVertexArray(0);
        shaderProgram.unbind();
    }

    private static void updateLights(Scene scene, Uniforms uniforms) throws ShaderGLException {
        SceneLights sceneLights = scene.getSceneLights();

        AmbientLight ambientLight = sceneLights.ambientLight();
        uniforms.put("ambientLight.color", ambientLight.color());
        uniforms.put("ambientLight.intensity", ambientLight.intensity());

        DirectedLight directedLight = sceneLights.directedLight();
        uniforms.put("directedLight.color", directedLight.color());
        uniforms.put("directedLight.direction", directedLight.direction());
        uniforms.put("directedLight.intensity", directedLight.intensity());
    }

    private static void createUniforms(Uniforms uniforms) throws ShaderGLException {
        uniforms.create("projectionMatrix");
        uniforms.create("viewMatrix");
        uniforms.create("sceneRotationMatrix");
        uniforms.create("modelTransformMatrix");
        uniforms.create("modelRotationMatrix");
        uniforms.create("textureSampler");
        uniforms.create("transparencyAllowed");

        uniforms.create("ambientLight.color");
        uniforms.create("ambientLight.intensity");

        uniforms.create("directedLight.color");
        uniforms.create("directedLight.direction");
        uniforms.create("directedLight.intensity");
    }

}
