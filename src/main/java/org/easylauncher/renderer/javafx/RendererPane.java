package org.easylauncher.renderer.javafx;

import com.huskerdev.openglfx.canvas.GLCanvas;
import com.huskerdev.openglfx.canvas.GLProfile;
import com.huskerdev.openglfx.lwjgl.LWJGLExecutor;
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
import org.easylauncher.renderer.engine.graph.texture.source.TextureSource;
import org.easylauncher.renderer.engine.scene.Scene;
import org.easylauncher.renderer.game.skin.SkinTextureWrapper;
import org.easylauncher.renderer.game.skin.resolver.DefaultSkinResolver;
import org.easylauncher.renderer.javafx.behavior.InteractivePaneBehavior;
import org.easylauncher.renderer.state.Bindable;
import org.easylauncher.renderer.state.Cleanable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

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

    private InteractivePaneBehavior interactivePaneBehavior;
    private boolean capeChanged;
    private boolean skinChanged;

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
        this.canvas = new GLCanvas(LWJGLExecutor.LWJGL_MODULE, GLProfile.Core, false, 4, true);
        this.defaultSkinResolver = customizerBase.getDefaultSkinResolver();

        canvas.addOnInitEvent(event -> onCanvasInit(renderOptions, customizerBase.getCompositionMaker()));
        canvas.addOnReshapeEvent(event -> onCanvasReshape(event.width, event.height));
        canvas.addOnRenderEvent(event -> onCanvasRender(event.fps, event.delta));
        canvas.addOnDisposeEvent(event -> onCanvasDispose());

        if (customizerBase.isInteractive()) {
            this.interactivePaneBehavior = new InteractivePaneBehavior(this, customizerBase.getMouseSensitivity());
            this.interactivePaneBehavior.initialize();
        }

        getChildren().add(canvas);
    }

    @Override
    public void bind() {
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
        if (interactivePaneBehavior != null) {
            interactivePaneBehavior.unbind();
        }
    }

    public void requestRender() {
        if (canvas != null) {
            canvas.repaint();
        }
    }

    private void updateTextures() throws TextureLoadException {
        Scene scene = renderContext.getScene();
        Material capeMaterial = scene.getCapeMaterial();
        Material skinMaterial = scene.getSkinMaterial();
        Texture previousTexture = null;

        if (capeChanged) {
            TextureSource capeSource = getPlayerCape();
            Texture capeTexture = capeSource != null ? capeSource.getOrLoadTexture() : null;
            previousTexture = capeMaterial.updateTexture(capeTexture);

            if (previousTexture != null) {
                previousTexture.cleanup();
            }
        }

        if (skinChanged) {
            TextureSource skinSource = getPlayerSkin();
            Texture skinTexture = skinSource != null ? skinSource.getOrLoadTexture() : null;
            previousTexture = skinMaterial.updateTexture(skinTexture);

            if (previousTexture != null && !scene.isShowingDefaultSkin()) {
                previousTexture.cleanup();
            }
        }

        if (!skinMaterial.hasTexture()) {
            UUID playerUUID = getPlayerUUID();
            SkinTextureWrapper skinTexture = defaultSkinResolver.resolveSkinTexture(scene, playerUUID);
            sceneComposition.updateArmsThickness(skinTexture.isThinArms());
            skinMaterial.updateTexture(skinTexture);
            scene.setShowingDefaultSkin(true);
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
                sceneComposition.updateArmsThickness(renderContext.getRenderOptions().thinArms());
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
        if (playerUUID == null)
            this.playerUUID = new SimpleObjectProperty<>(this, "Player UUID");

        return playerUUID;
    }

    // --- player skin texture
    private ObjectProperty<TextureSource> playerSkin;
    public TextureSource getPlayerSkin() { return playerSkin != null ? playerSkin.get() : null; }
    public void setPlayerSkin(TextureSource value) { playerSkinProperty().set(value); }
    public ObjectProperty<TextureSource> playerSkinProperty() {
        if (playerSkin == null) {
            this.playerSkin = new SimpleObjectProperty<>(this, "Player Skin");
            this.playerSkin.addListener((a, from, to) -> this.skinChanged = true);
        }

        return playerSkin;
    }

    // --- player cape texture
    private ObjectProperty<TextureSource> playerCape;
    public TextureSource getPlayerCape() { return playerCape != null ? playerCape.get() : null; }
    public void setPlayerCape(TextureSource value) { playerCapeProperty().set(value); }
    public ObjectProperty<TextureSource> playerCapeProperty() {
        if (playerCape == null) {
            this.playerCape = new SimpleObjectProperty<>(this, "Player Cape");
            this.playerCape.addListener((a, from, to) -> this.capeChanged = true);
        }

        return playerCape;
    }

}
