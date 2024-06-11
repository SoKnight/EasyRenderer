package org.easylauncher.renderer.javafx.behavior;

import com.huskerdev.openglfx.canvas.GLCanvasAnimator;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.util.Duration;
import lombok.Getter;
import org.easylauncher.renderer.context.RendererContext;
import org.easylauncher.renderer.engine.graph.Entity;
import org.easylauncher.renderer.engine.graph.Model;
import org.easylauncher.renderer.engine.scene.Scene;
import org.easylauncher.renderer.game.cape.CapeEntity;
import org.easylauncher.renderer.game.skin.entity.SkinEntityBase;
import org.easylauncher.renderer.javafx.RendererPane;
import org.easylauncher.renderer.state.Animated;

import java.util.function.Predicate;

public final class AnimatedPaneBehavior implements RendererPaneBehavior {

    private final RendererPane rendererPane;
    private final RendererContext rendererContext;

    private final double animatorFps;
    private final Duration capeAnimationDuration;
    private final Duration skinAnimationDuration;

    private SceneAnimation capeAnimation;
    private SceneAnimation skinAnimation;

    @Getter
    private boolean playing;

    public AnimatedPaneBehavior(
            RendererPane rendererPane,
            double animatorFps,
            Duration capeAnimationDuration,
            Duration skinAnimationDuration
    ) {
        this.rendererPane = rendererPane;
        this.rendererContext = rendererPane.getRenderContext();

        this.animatorFps = animatorFps;
        this.capeAnimationDuration = capeAnimationDuration;
        this.skinAnimationDuration = skinAnimationDuration;
    }

    @Override
    public void initialize() {
        Scene scene = rendererContext.getScene();
        this.capeAnimation = new SceneAnimation(scene, capeAnimationDuration, e -> e instanceof CapeEntity);
        this.skinAnimation = new SceneAnimation(scene, skinAnimationDuration, e -> e instanceof SkinEntityBase);
    }

    @Override
    public void bind() {
        rendererPane.setCanvasAnimator(new GLCanvasAnimator(animatorFps));
    }

    @Override
    public void unbind() {
        capeAnimation.stop();
        capeAnimation.jumpTo(Duration.ZERO);

        skinAnimation.stop();
        skinAnimation.jumpTo(Duration.ZERO);

        rendererPane.setCanvasAnimator(null);
    }

    public void play() {
        if (isPlaying())
            return;

        capeAnimation.play();
        skinAnimation.play();
        this.playing = true;
    }

    public void pause() {
        if (!isPlaying())
            return;

        capeAnimation.pause();
        skinAnimation.pause();
        this.playing = false;
    }

    public void reset() {
        if (isPlaying()) {
            capeAnimation.stop();
            capeAnimation.playFromStart();

            skinAnimation.stop();
            skinAnimation.playFromStart();
        } else {
            capeAnimation.jumpTo(Duration.ZERO);
            skinAnimation.jumpTo(Duration.ZERO);
        }
    }

    private static final class SceneAnimation extends Transition {

        private final Scene scene;
        private final Predicate<Entity> entityFilter;

        public SceneAnimation(Scene scene, Duration duration, Predicate<Entity> entityFilter) {
            this.scene = scene;
            this.entityFilter = entityFilter;

            setAutoReverse(true);
            setInterpolator(Interpolator.LINEAR);
            setCycleDuration(duration);
            setCycleCount(INDEFINITE);
        }

        @Override
        protected void interpolate(double frac) {
            float timeFactor = (float) Math.sin(Math.toRadians(180D * (frac - 0.5D))) / 2F;

            for (Model model : scene.getModels().values()) {
                for (Entity entity : model.getEntities()) {
                    if (!entityFilter.test(entity))
                        continue;

                    if (entity instanceof Animated animated) {
                        animated.updateAnimation(timeFactor);
                    }
                }
            }
        }

    }

}
