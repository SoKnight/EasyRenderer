package org.easylauncher.framework.gamerender.engine;

import org.easylauncher.framework.gamerender.engine.exception.shader.ShaderGLException;
import org.easylauncher.framework.gamerender.engine.exception.shader.ShaderLoadException;
import org.easylauncher.framework.gamerender.engine.scene.Scene;

public final class Engine {

    public static final int DEFAULT_TARGET_UPS = 30;

    private final AppLogic appLogic;
    private final Window window;
    private final Render render;
    private final Scene scene;

    private final int targetFps;
    private final int targetUps;
    private boolean running;

    public Engine(String windowTitle, Window.Options windowOptions, AppLogic appLogic) throws ShaderGLException, ShaderLoadException {
        this.appLogic = appLogic;
        this.window = new Window(windowTitle, windowOptions, this::resize);
        this.render = new Render(appLogic.getRenderOptions());
        this.scene = new Scene(window.getWidth(), window.getHeight());

        this.targetFps = windowOptions.fps();
        this.targetUps = windowOptions.ups();
        this.running = true;

        appLogic.init(window, scene, render);
    }

    public void start() {
        this.running = true;
        try {
            run();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        this.running = false;
    }

    public void stop() {
        this.running = false;
    }

    private void run() throws ShaderGLException {
        long initialTime = System.currentTimeMillis();
        float timeU = 1000.0F / targetUps;
        float timeR = targetFps > 0 ? 1000.0F / targetFps : 0F;
        float deltaUpdate = 0F;
        float deltaFps = 0F;

        long updateTime = initialTime;
        while (running && !window.windowShouldClose()) {
            window.pollEvents();

            long now = System.currentTimeMillis();
            deltaUpdate += (now - initialTime) / timeU;

            if (timeR > 0F) {
                deltaFps += (now - initialTime) / timeR;
            }

            if (targetFps <= 0 || deltaFps >= 1) {
                window.getMouseInput().input();
                appLogic.input(window, scene, now - initialTime);
            }

            if (deltaUpdate >= 1) {
                appLogic.update(window, scene, now - updateTime);
                updateTime = now;
                deltaUpdate--;
            }

            if (targetFps <= 0 || deltaFps >= 1) {
                render.render(window, scene);
                deltaFps--;
                window.update();
            }

            initialTime = now;
        }

        cleanup();
    }

    private void cleanup() {
        appLogic.cleanup();
        render.cleanup();
        scene.cleanup();
        window.cleanup();
    }

    private void resize() {
        scene.resize(window.getWidth(), window.getHeight());
    }

}
