package org.easylauncher.renderer.javafx;

import javafx.util.Duration;
import lombok.Getter;
import org.easylauncher.renderer.composition.SceneComposition;
import org.easylauncher.renderer.context.ViewDesire;
import org.easylauncher.renderer.game.skin.resolver.DefaultSkinResolver;

import java.util.Objects;

@Getter
final class RendererPaneCustomizerBase implements RendererPaneCustomizer {

    private ViewDesire viewDesire;
    private SceneComposition.Maker compositionMaker;
    private DefaultSkinResolver defaultSkinResolver;
    private boolean animated;
    private int animatorFps;
    private Duration capeAnimationDuration;
    private Duration skinAnimationDuration;
    private boolean interactive;
    private float mouseSensitivity;

    public RendererPaneCustomizerBase() {
        this.viewDesire = ViewDesire.SKIN;
        this.compositionMaker = SceneComposition.Maker.DEFAULT;
        this.defaultSkinResolver = DefaultSkinResolver.PLAYER_UUID_BASED;
        this.animated = false;
        this.animatorFps = DEFAULT_ANIMATOR_FPS;
        this.capeAnimationDuration = DEFAULT_CAPE_ANIMATION_DURATION;
        this.skinAnimationDuration = DEFAULT_SKIN_ANIMATION_DURATION;
        this.interactive = false;
        this.mouseSensitivity = DEFAULT_MOUSE_SENSITIVITY;
    }

    @Override
    public RendererPaneCustomizer desireView(ViewDesire viewDesire) {
        Objects.requireNonNull(viewDesire);
        this.viewDesire = viewDesire;
        return this;
    }

    @Override
    public RendererPaneCustomizer sceneComposition(SceneComposition.Maker compositionMaker) {
        Objects.requireNonNull(compositionMaker);
        this.compositionMaker = compositionMaker;
        return this;
    }

    @Override
    public RendererPaneCustomizer defaultSkinResolver(DefaultSkinResolver defaultSkinResolver) {
        Objects.requireNonNull(defaultSkinResolver);
        this.defaultSkinResolver = defaultSkinResolver;
        return this;
    }

    @Override
    public RendererPaneCustomizer makeAnimated() {
        return makeAnimated(-1);
    }

    @Override
    public RendererPaneCustomizer makeAnimated(int animatorFps) {
        if (animatorFps > 0)
            this.animatorFps = animatorFps;

        this.animated = true;
        return this;
    }

    @Override
    public RendererPaneCustomizer capeAnimationDuration(Duration animationDuration) {
        Objects.requireNonNull(animationDuration);
        this.capeAnimationDuration = animationDuration;
        return this;
    }

    @Override
    public RendererPaneCustomizer skinAnimationDuration(Duration animationDuration) {
        Objects.requireNonNull(animationDuration);
        this.skinAnimationDuration = animationDuration;
        return this;
    }

    @Override
    public RendererPaneCustomizer makeInteractive() {
        return makeInteractive(DEFAULT_MOUSE_SENSITIVITY);
    }

    @Override
    public RendererPaneCustomizer makeInteractive(float mouseSensitivity) {
        if (mouseSensitivity > 0F)
            this.mouseSensitivity = mouseSensitivity;

        this.interactive = true;
        return this;
    }

}
