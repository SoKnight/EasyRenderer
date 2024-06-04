package org.easylauncher.framework.gamerender.engine;

import javafx.scene.paint.Color;
import org.easylauncher.framework.gamerender.RenderOptions;
import org.easylauncher.framework.gamerender.engine.exception.shader.ShaderGLException;
import org.easylauncher.framework.gamerender.engine.exception.shader.ShaderLoadException;
import org.easylauncher.framework.gamerender.engine.scene.Scene;
import org.easylauncher.framework.gamerender.engine.scene.SceneRender;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;

public final class Render {

    private final SceneRender sceneRender;

    public Render(RenderOptions renderOptions) throws ShaderGLException, ShaderLoadException {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glEnable(GL_MULTISAMPLE);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        Color color = Color.web("#1B1E23");
        float r = (float) color.getRed();
        float g = (float) color.getGreen();
        float b = (float) color.getBlue();
        glClearColor(r, g, b, 1f);

        this.sceneRender = new SceneRender(renderOptions);
    }

    public void cleanup() {
        sceneRender.cleanup();
    }

    public void render(Window window, Scene scene) throws ShaderGLException {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, window.getWidth(), window.getHeight());
        sceneRender.render(scene);
    }

}
