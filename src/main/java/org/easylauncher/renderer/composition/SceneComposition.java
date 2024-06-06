package org.easylauncher.renderer.composition;

import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.engine.scene.Scene;

public interface SceneComposition {

    Scene getScene();

    void updateArmsThickness(boolean thinArms);

    @FunctionalInterface
    interface Maker {

        Maker DEFAULT = (scene, options) -> new SceneCompositionBase(scene, options, true);
        Maker NO_CAPE = (scene, options) -> new SceneCompositionBase(scene, options, false);

        SceneComposition make(Scene scene, RenderOptions options);

    }

}
