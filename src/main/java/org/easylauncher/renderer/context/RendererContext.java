package org.easylauncher.renderer.context;

import lombok.Getter;
import org.easylauncher.renderer.engine.Camera;
import org.easylauncher.renderer.engine.Engine;
import org.easylauncher.renderer.engine.Render;
import org.easylauncher.renderer.engine.exception.shader.ShaderGLException;
import org.easylauncher.renderer.engine.exception.shader.ShaderLoadException;
import org.easylauncher.renderer.engine.scene.Scene;
import org.easylauncher.renderer.engine.scene.SceneLights;
import org.easylauncher.renderer.engine.type.Cleanable;
import org.easylauncher.renderer.engine.type.Initializable;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
public final class RendererContext implements Cleanable, Initializable {

    public static final Camera DEFAULT_CAMERA = new Camera();
    public static final SceneLights DEFAULT_SCENE_LIGHTS = new SceneLights();

    static {
        DEFAULT_CAMERA.moveBackwards(70F);
    }

    private static final AtomicInteger IDENTIFIER = new AtomicInteger(0);
    private static final int DEFAULT_SCENE_WIDTH = 1;
    private static final int DEFAULT_SCENE_HEIGHT = 1;
    private static final float DEFAULT_SCENE_ANGLE_X = 21F;

    private final int id;
    private final Engine engine;
    private final Render render;
    private final Scene scene;

    private int width, height;
    private float rotationX, rotationY;
    private ViewDesire viewDesire;
    private boolean available;

    public RendererContext(Engine engine, Render render, ViewDesire viewDesire) {
        this(engine, render, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT, DEFAULT_CAMERA, DEFAULT_SCENE_LIGHTS, viewDesire);
    }

    public RendererContext(Engine engine, Render render, int width, int height, Camera camera, SceneLights sceneLights, ViewDesire viewDesire) {
        this.id = IDENTIFIER.getAndIncrement();
        this.engine = engine;
        this.render = render;
        this.scene = new Scene(width, height, camera, sceneLights);

        this.width = width;
        this.height = height;
        this.viewDesire = viewDesire;

        rotateSceneTo(21F, viewDesire.getSceneAngleY());
    }

    @Override
    public void cleanup() {
        scene.cleanup();
        render.cleanup();
    }

    @Override
    public void initialize() throws ShaderGLException, ShaderLoadException {
        render.initialize();
        this.available = true;
    }

    public void render() throws ShaderGLException {
        render.render(scene);
    }

    public void resize(int width, int height) {
        if (this.width != width || this.height != height) {
            this.width = width;
            this.height = height;
            render.reshape(width, height);
            scene.resize(width, height);
        }
    }

    public void rotateSceneBy(float dx, float dy) {
        float newAngleX = rotationX + dx;
        float newAngleY = rotationY + dy;
        rotateSceneTo(newAngleX % 360F, newAngleY % 360F);
    }

    public void rotateSceneTo(float x, float y) {
        this.rotationX = x;
        this.rotationY = y;

        scene.getRotationX().setAngleDeg(rotationX);
        scene.getRotationY().setAngleDeg(rotationY);
        scene.updateRotationMatrix();
    }

    public void desireView(ViewDesire viewDesire) {
        if (this.viewDesire != viewDesire) {
            this.viewDesire = viewDesire;
            scene.getRotationY().setAngleDeg(viewDesire.getSceneAngleY());
            scene.updateRotationMatrix();
        }
    }

    public void unload() {
        this.available = false;
        engine.unloadContext(this);
    }

}
