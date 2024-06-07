package org.easylauncher.renderer.javafx;

import org.easylauncher.renderer.composition.SceneComposition;
import org.easylauncher.renderer.context.ViewDesire;
import org.easylauncher.renderer.game.skin.resolver.DefaultSkinResolver;

public interface RendererPaneCustomizer {

    float DEFAULT_MOUSE_SENSITIVITY = 0.5F;

    RendererPaneCustomizer desireView(ViewDesire viewDesire);

    RendererPaneCustomizer sceneComposition(SceneComposition.Maker compositionMaker);

    RendererPaneCustomizer defaultSkinResolver(DefaultSkinResolver defaultSkinResolver);

    RendererPaneCustomizer enableAnimations();

    RendererPaneCustomizer makeInteractive();

    RendererPaneCustomizer makeInteractive(float mouseSensitivity);

}
