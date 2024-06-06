package org.easylauncher.renderer.engine;

import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.engine.exception.shader.ShaderGLException;
import org.easylauncher.renderer.engine.exception.shader.ShaderLoadException;
import org.easylauncher.renderer.engine.scene.Scene;
import org.easylauncher.renderer.engine.scene.SceneRender;
import org.easylauncher.renderer.engine.type.Cleanable;
import org.easylauncher.renderer.engine.type.Initializable;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;

public final class Render implements Cleanable, Initializable {

    private final RenderOptions renderOptions;
    private SceneRender sceneRender;

    public Render(RenderOptions renderOptions) {
        this.renderOptions = renderOptions;
    }

    @Override
    public void cleanup() {
        if (sceneRender != null) {
            sceneRender.cleanup();
        }
    }

    @Override
    public void initialize() throws ShaderGLException, ShaderLoadException {
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);

        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

        this.sceneRender = new SceneRender(renderOptions);
    }

    public void reshape(int width, int height) {
        glViewport(0, 0, width, height);
    }

    public void render(Scene scene) throws ShaderGLException {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        sceneRender.render(scene);
    }

}
