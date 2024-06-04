package org.easylauncher.framework.gamerender.engine;

import org.easylauncher.framework.gamerender.RenderOptions;
import org.easylauncher.framework.gamerender.engine.scene.Scene;

public interface AppLogic {

    RenderOptions getRenderOptions();

    void init(Window window, Scene scene, Render render);

    void input(Window window, Scene scene, long diffTime);

    void update(Window window, Scene scene, long diffTime);

    void cleanup();

}
