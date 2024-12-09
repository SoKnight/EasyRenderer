package org.easylauncher.renderer.javafx;

import com.huskerdev.grapl.gl.GLProfile;
import com.huskerdev.openglfx.canvas.GLCanvas;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.SneakyThrows;
import org.easylauncher.renderer.composition.SceneComposition;
import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.context.RendererContext;
import org.easylauncher.renderer.engine.Engine;
import org.easylauncher.renderer.engine.exception.shader.ShaderGLException;
import org.easylauncher.renderer.engine.exception.shader.ShaderLoadException;
import org.easylauncher.renderer.engine.exception.texture.TextureLoadException;
import org.easylauncher.renderer.engine.graph.Material;
import org.easylauncher.renderer.engine.graph.texture.Texture;
import org.easylauncher.renderer.engine.scene.Scene;
import org.easylauncher.renderer.game.skin.SkinTextureWrapper;
import org.easylauncher.renderer.game.skin.resolver.DefaultSkinResolver;
import org.easylauncher.renderer.javafx.behavior.AnimatedPaneBehavior;
import org.easylauncher.renderer.javafx.behavior.InteractivePaneBehavior;
import org.easylauncher.renderer.javafx.texture.TextureImageWrapper;
import org.easylauncher.renderer.javafx.texture.TextureType;
import org.easylauncher.renderer.state.Bindable;
import org.easylauncher.renderer.state.Cleanable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static com.huskerdev.openglfx.lwjgl.LWJGLExecutor.LWJGL_MODULE;
import static org.easylauncher.renderer.context.SkinPart.ALL_LAYERS;
import static org.easylauncher.renderer.context.SkinPart.INNER_LAYERS;

public final class RendererPane extends StackPane implements Bindable, Cleanable {

    private static final String DEFAULT_STYLE_CLASS = "renderer-pane";
    private static final List<RendererPane> INSTANCES = new ArrayList<>();

    public static void cleanupAll() {
        Iterator<RendererPane> iterator = INSTANCES.iterator();
        while (iterator.hasNext()) {
            RendererPane rendererPane = iterator.next();
            rendererPane.unbind();
            rendererPane.cleanup();
            iterator.remove();
        }
    }

    @Getter
    private RendererContext renderContext;
    private GLCanvas canvas;
    private SceneComposition sceneComposition;
    private DefaultSkinResolver defaultSkinResolver;

    private AnimatedPaneBehavior animatedPaneBehavior;
    private InteractivePaneBehavior interactivePaneBehavior;

    private boolean capeChanged;
    private boolean skinChanged;
    private boolean uuidChanged;

    public RendererPane() {
        initialize();
        INSTANCES.add(this);
    }

    private void initialize() {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    public void initialize(RenderOptions renderOptions) {
        initialize(renderOptions, null);
    }

    @SneakyThrows
    public void initialize(RenderOptions renderOptions, Consumer<RendererPaneCustomizer> customizer) {
        RendererPaneCustomizerBase customizerBase = new RendererPaneCustomizerBase();
        if (customizer != null)
            customizer.accept(customizerBase);

        this.renderContext = Engine.CURRENT.loadContext(renderOptions, customizerBase.getViewDesire());
        this.canvas = new GLCanvas(LWJGL_MODULE, GLProfile.CORE, false, 16);
        this.defaultSkinResolver = customizerBase.getDefaultSkinResolver();

        canvas.addOnInitEvent(event -> onCanvasInit(renderOptions, customizerBase.getCompositionMaker()));
        canvas.addOnReshapeEvent(event -> onCanvasReshape(event.width, event.height));
        canvas.addOnRenderEvent(event -> onCanvasRender(event.fps, event.delta));
        canvas.addOnDisposeEvent(event -> onCanvasDispose());

        if (customizerBase.isAnimated()) {
            this.animatedPaneBehavior = new AnimatedPaneBehavior(
                    this,
                    customizerBase.getAnimatorFps(),
                    customizerBase.getCapeAnimationDuration(),
                    customizerBase.getSkinAnimationDuration()
            );

            animatedPaneBehavior.initialize();
        }

        if (customizerBase.isInteractive()) {
            this.interactivePaneBehavior = new InteractivePaneBehavior(this, customizerBase.getMouseSensitivity());
            this.interactivePaneBehavior.initialize();
        }

        getChildren().add(canvas);
    }

    @Override
    public void bind() {
        if (animatedPaneBehavior != null) {
            animatedPaneBehavior.bind();
        }

        if (interactivePaneBehavior != null) {
            interactivePaneBehavior.bind();
        }
    }

    @Override
    public void cleanup() {
        if (canvas != null) {
            canvas.dispose();
        }
    }

    @Override
    public void unbind() {
        if (animatedPaneBehavior != null) {
            animatedPaneBehavior.unbind();
        }

        if (interactivePaneBehavior != null) {
            interactivePaneBehavior.unbind();
        }
    }

    public void requestRender() {
        if (canvas != null) {
            canvas.repaint();
        }
    }

    public boolean isAnimationPlaying() {
        return animatedPaneBehavior != null && animatedPaneBehavior.isPlaying();
    }

    public void playAnimation() {
        if (animatedPaneBehavior != null) {
            animatedPaneBehavior.play();
        }
    }

    public void pauseAnimation() {
        if (animatedPaneBehavior != null) {
            animatedPaneBehavior.pause();
        }
    }

    public void resetAnimation() {
        if (animatedPaneBehavior != null) {
            animatedPaneBehavior.reset();
        }
    }

    public void setFps(int fps) {
        if (canvas != null) {
            canvas.setFps(fps);
        }
    }

    private void updateTextures() throws TextureLoadException {
        Scene scene = renderContext.getScene();
        Material capeMaterial = scene.getCapeMaterial();
        Material skinMaterial = scene.getSkinMaterial();
        boolean shouldUpdateArmThickness = false;

        if (capeChanged) {
            TextureImageWrapper capeImage = getCapeTexture();
            Texture capeTexture = capeImage != null ? capeImage.toTextureSource().getOrLoadTexture() : null;

            Texture previousTexture = capeMaterial.updateTexture(capeTexture);
            if (previousTexture != null)
                previousTexture.cleanup();

            renderContext.getRenderOptions()
                    .capeScale(capeImage != null ? capeImage.getScale() : 1)
                    .showCape(capeTexture != null);

            this.capeChanged = false;
        }

        if (skinChanged) {
            TextureImageWrapper skinImage = getSkinTexture();
            Texture skinTexture = skinImage != null ? skinImage.toTextureSource().getOrLoadTexture() : null;

            Texture previousTexture = skinMaterial.updateTexture(skinTexture);
            if (previousTexture != null && !scene.isShowingDefaultSkin())
                previousTexture.cleanup();

            renderContext.getRenderOptions()
                    .legacySkinTexture(skinImage != null && skinImage.getTextureType() == TextureType.LEGACY_SKIN)
                    .skinThinArms(skinImage != null && skinImage.isThinArms())
                    .skinScale(skinImage != null ? skinImage.getScale() : 1)
                    .visibleLayersMask(skinImage != null ? skinImage.getVisibleLayersMask() : ALL_LAYERS);

            scene.setShowingDefaultSkin(false);
            resetAnimation();

            shouldUpdateArmThickness = true;
            this.skinChanged = false;
        }

        if (!skinMaterial.hasTexture() || (scene.isShowingDefaultSkin() && uuidChanged)) {
            SkinTextureWrapper skinTexture = defaultSkinResolver.resolveSkinTexture(scene, getPlayerUUID());

            renderContext.getRenderOptions()
                    .skinScale(1)
                    .skinThinArms(skinTexture.isThinArms())
                    .visibleLayersMask(INNER_LAYERS);

            skinMaterial.updateTexture(skinTexture);
            scene.setShowingDefaultSkin(true);
            resetAnimation();

            shouldUpdateArmThickness = true;
            this.uuidChanged = false;
        }

        if (shouldUpdateArmThickness) {
            sceneComposition.updateArmsThickness(renderContext.getRenderOptions().skinThinArms());
        }
    }

    private void onCanvasInit(RenderOptions renderOptions, SceneComposition.Maker compositionMaker) {
        try {
            renderContext.initialize();

            Scene scene = renderContext.getScene();
            this.sceneComposition = compositionMaker.make(scene, renderOptions);
        } catch (ShaderGLException | ShaderLoadException ex) {
            ex.printStackTrace();
            // TODO handle exception
        }
    }

    private void onCanvasReshape(int width, int height) {
        if (renderContext.isAvailable()) {
            renderContext.resize(width, height);
        }
    }

    private void onCanvasRender(int fps, double delta) {
        if (renderContext.isAvailable()) {
            try {
                updateTextures();
                renderContext.render();
            } catch (ShaderGLException | TextureLoadException ex) {
                ex.printStackTrace();
                // TODO handle exception
            }
        }
    }

    private void onCanvasDispose() {
        if (renderContext.isAvailable()) {
            renderContext.cleanup();
        }
    }

    // --- player UUID
    private ObjectProperty<UUID> playerUUID;
    public UUID getPlayerUUID() { return playerUUID != null ? playerUUID.get() : null; }
    public void setPlayerUUID(UUID value) { playerUUIDProperty().set(value); }
    public ObjectProperty<UUID> playerUUIDProperty() {
        if (playerUUID == null) {
            this.playerUUID = new SimpleObjectProperty<>(this, "Player UUID");
            this.playerUUID.addListener((a, from, to) -> this.uuidChanged = true);
        }

        return playerUUID;
    }

    // --- player cape texture
    private ObjectProperty<TextureImageWrapper> capeTexture;
    public TextureImageWrapper getCapeTexture() { return capeTexture != null ? capeTexture.get() : null; }
    public void setCapeTexture(TextureImageWrapper value) { capeTextureProperty().set(value); }
    public ObjectProperty<TextureImageWrapper> capeTextureProperty() {
        if (capeTexture == null) {
            this.capeTexture = new SimpleObjectProperty<>(this, "Cape Texture");
            this.capeTexture.addListener((a, from, to) -> this.capeChanged = true);
        }

        return capeTexture;
    }

    // --- player skin texture
    private ObjectProperty<TextureImageWrapper> skinTexture;
    public TextureImageWrapper getSkinTexture() { return skinTexture != null ? skinTexture.get() : null; }
    public void setSkinTexture(TextureImageWrapper value) { skinTextureProperty().set(value); }
    public ObjectProperty<TextureImageWrapper> skinTextureProperty() {
        if (skinTexture == null) {
            this.skinTexture = new SimpleObjectProperty<>(this, "Skin Texture");
            this.skinTexture.addListener((a, from, to) -> this.skinChanged = true);
        }

        return skinTexture;
    }

}
