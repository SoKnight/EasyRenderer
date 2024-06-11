package org.easylauncher.renderer.javafx;

import javafx.util.Duration;
import org.easylauncher.renderer.composition.SceneComposition;
import org.easylauncher.renderer.context.ViewDesire;
import org.easylauncher.renderer.game.skin.resolver.DefaultSkinResolver;

public interface RendererPaneCustomizer {

    Duration DEFAULT_CAPE_ANIMATION_DURATION = Duration.seconds(3D);
    Duration DEFAULT_SKIN_ANIMATION_DURATION = Duration.millis(750D);
    double DEFAULT_ANIMATOR_FPS = 60D;
    float DEFAULT_MOUSE_SENSITIVITY = 0.5F;

    RendererPaneCustomizer desireView(ViewDesire viewDesire);

    RendererPaneCustomizer sceneComposition(SceneComposition.Maker compositionMaker);

    RendererPaneCustomizer defaultSkinResolver(DefaultSkinResolver defaultSkinResolver);

    RendererPaneCustomizer makeAnimated();

    RendererPaneCustomizer makeAnimated(double animatorFps);

    RendererPaneCustomizer capeAnimationDuration(Duration animationDuration);

    RendererPaneCustomizer skinAnimationDuration(Duration animationDuration);

    RendererPaneCustomizer makeInteractive();

    RendererPaneCustomizer makeInteractive(float mouseSensitivity);

}
