package org.easylauncher.framework.gamerender.engine.scene;

import org.easylauncher.framework.gamerender.RenderOptions;
import org.easylauncher.framework.gamerender.engine.cache.UniformCache;
import org.easylauncher.framework.gamerender.engine.exception.shader.ShaderGLException;
import org.easylauncher.framework.gamerender.engine.exception.shader.ShaderLoadException;
import org.easylauncher.framework.gamerender.engine.light.AmbientLight;
import org.easylauncher.framework.gamerender.engine.light.Attenuation;
import org.easylauncher.framework.gamerender.engine.light.DirectionalLight;
import org.easylauncher.framework.gamerender.engine.light.PointLight;
import org.easylauncher.framework.gamerender.engine.object.*;
import org.easylauncher.framework.gamerender.engine.shader.ShaderModule;
import org.easylauncher.framework.gamerender.engine.shader.ShaderProgram;
import org.easylauncher.framework.gamerender.engine.shader.ShaderType;
import org.easylauncher.framework.gamerender.game.skin.SkinPartMesh;
import org.easylauncher.framework.gamerender.game.skin.model.SkinModelBase;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public final class SceneRender {

    private static final int MAX_POINT_LIGHTS = 3;

    private final ShaderProgram shaderProgram;
    private final UniformCache uniformCache;
    private final RenderOptions renderOptions;

    public SceneRender(RenderOptions renderOptions) throws ShaderGLException, ShaderLoadException {
        this.shaderProgram = new ShaderProgram(
                new ShaderModule("scene", ShaderType.VERTEX),
                new ShaderModule("scene", ShaderType.FRAGMENT)
        );

        this.uniformCache = createUniforms();
        this.renderOptions = renderOptions;
    }

    public void cleanup() {
        shaderProgram.cleanup();
    }

    public void render(Scene scene) throws ShaderGLException {
        shaderProgram.bind();

        uniformCache.putUniform("projectionMatrix", scene.getProjection().getProjectionMatrix());
        uniformCache.putUniform("viewMatrix", scene.getCamera().getViewMatrix());
        uniformCache.putUniform("sceneRotationMatrix", scene.getRotationMatrix());
        uniformCache.putUniform("textureSampler", 0);

        updateLights(scene);

        Collection<Model> models = scene.getModels().values();
        Material currentMaterial = null;

        for (Model model : models) {
            Material desiredMaterial;
            if (model instanceof SkinModelBase) {
                desiredMaterial = scene.getSkinMaterial();
            } else {
                desiredMaterial = null;
            }

            if (!Objects.equals(currentMaterial, desiredMaterial)) {
                currentMaterial = desiredMaterial;
                if (currentMaterial != null) {
                    Texture texture = currentMaterial.getTexture();
                    if (texture != null) {
                        glActiveTexture(GL_TEXTURE0);
                        texture.bind();
                    }

                    uniformCache.putUniform("material.ambientColor", currentMaterial.getAmbientColor());
                    uniformCache.putUniform("material.diffuseColor", currentMaterial.getDiffuseColor());
                    uniformCache.putUniform("material.specularColor", currentMaterial.getSpecularColor());
                    uniformCache.putUniform("material.reflectance", currentMaterial.getReflectance());
                } else {
                    glActiveTexture(GL_TEXTURE1);
                }
            }

            List<Mesh> meshes = model.getRenderableMeshes(renderOptions);
            if (meshes != null) {
                for (Mesh mesh : meshes) {
                    List<Entity> entities = model.getEntities();
                    if (entities.isEmpty())
                        continue;

                    boolean transparencyAllowed = !(mesh instanceof SkinPartMesh cast) || cast.isTransparencyAllowed();

                    glBindVertexArray(mesh.getVaoId());
                    for (Entity entity : entities) {
                        uniformCache.putUniform("modelTransformMatrix", entity.getTransformMatrix());
                        uniformCache.putUniform("modelRotationMatrix", entity.getRotationMatrix());
                        uniformCache.putUniform("transparencyAllowed", transparencyAllowed);
                        glDrawElements(GL_TRIANGLES, mesh.getVerticesCount(), GL_UNSIGNED_INT, 0);
                    }
                }
            }
        }

        glBindVertexArray(0);

        shaderProgram.unbind();
    }

    private void updateLights(Scene scene) throws ShaderGLException {
        Matrix4f viewMatrix = scene.getCamera().getViewMatrix();

        SceneLights sceneLights = scene.getSceneLights();
        AmbientLight ambientLight = sceneLights.getAmbientLight();
        uniformCache.putUniform("ambientLight.color", ambientLight.getColor());
        uniformCache.putUniform("ambientLight.intensity", ambientLight.getIntensity());

        DirectionalLight directionalLight = sceneLights.getDirectionalLight();
        if (directionalLight != null) {
            Vector4f auxDir = new Vector4f(directionalLight.getDirection(), 0);
            auxDir.mul(viewMatrix);
            Vector3f dir = new Vector3f(auxDir.x, auxDir.y, auxDir.z);
            uniformCache.putUniform("directionalLight.color", directionalLight.getColor());
            uniformCache.putUniform("directionalLight.direction", dir);
            uniformCache.putUniform("directionalLight.intensity", directionalLight.getIntensity());
        } else {
            uniformCache.putUniform("directionalLight.color", new Vector3f());
            uniformCache.putUniform("directionalLight.direction", new Vector3f());
            uniformCache.putUniform("directionalLight.intensity", 0F);
        }

        List<PointLight> pointLights = sceneLights.getPointLights();
        int pointLightsCount = pointLights.size();
        for (int i = 0; i < MAX_POINT_LIGHTS; i++) {
            PointLight pointLight = i < pointLightsCount ? pointLights.get(i) : null;
            String name = "pointLights[" + i + "]";
            updatePointLight(pointLight, name, viewMatrix);
        }
    }

    private void updatePointLight(PointLight pointLight, String prefix, Matrix4f viewMatrix) throws ShaderGLException {
        Vector4f aux = new Vector4f();

        Vector3f color = new Vector3f();
        Vector3f position = new Vector3f();
        float intensity = 0.0f;

        float constant = 0.0f;
        float exponent = 0.0f;
        float linear = 0.0f;
        
        if (pointLight != null) {
            aux.set(pointLight.getPosition(), 1);
            aux.mul(viewMatrix);

            color.set(pointLight.getColor());
            position.set(aux.x, aux.y, aux.z);
            intensity = pointLight.getIntensity();

            Attenuation attenuation = pointLight.getAttenuation();
            constant = attenuation.getConstant();
            exponent = attenuation.getExponent();
            linear = attenuation.getLinear();
        }

        uniformCache.putUniform(prefix + ".attenuation.constant", constant);
        uniformCache.putUniform(prefix + ".attenuation.exponent", exponent);
        uniformCache.putUniform(prefix + ".attenuation.linear", linear);
        uniformCache.putUniform(prefix + ".color", color);
        uniformCache.putUniform(prefix + ".position", position);
        uniformCache.putUniform(prefix + ".intensity", intensity);
    }

    private UniformCache createUniforms() throws ShaderGLException {
        UniformCache uniformCache = new UniformCache(shaderProgram.getProgramId());

        uniformCache.createUniform("projectionMatrix");
        uniformCache.createUniform("viewMatrix");
        uniformCache.createUniform("sceneRotationMatrix");
        uniformCache.createUniform("modelTransformMatrix");
        uniformCache.createUniform("modelRotationMatrix");
        uniformCache.createUniform("textureSampler");
        uniformCache.createUniform("transparencyAllowed");

        uniformCache.createUniform("material.ambientColor");
        uniformCache.createUniform("material.diffuseColor");
        uniformCache.createUniform("material.specularColor");
        uniformCache.createUniform("material.reflectance");

        uniformCache.createUniform("ambientLight.color");
        uniformCache.createUniform("ambientLight.intensity");

        for (int i = 0; i < MAX_POINT_LIGHTS; i++) {
            String name = "pointLights[%d]".formatted(i);
            uniformCache.createUniform(name + ".attenuation.constant");
            uniformCache.createUniform(name + ".attenuation.exponent");
            uniformCache.createUniform(name + ".attenuation.linear");
            uniformCache.createUniform(name + ".color");
            uniformCache.createUniform(name + ".position");
            uniformCache.createUniform(name + ".intensity");
        }

        uniformCache.createUniform("directionalLight.color");
        uniformCache.createUniform("directionalLight.direction");
        uniformCache.createUniform("directionalLight.intensity");

        return uniformCache;
    }

}
