package org.easylauncher.renderer.javafx;

import com.huskerdev.openglfx.canvas.GLCanvas;
import com.huskerdev.openglfx.canvas.GLProfile;
import com.huskerdev.openglfx.lwjgl.LWJGLExecutor;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import org.easylauncher.renderer.composition.SceneComposition;
import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.context.RendererContext;
import org.easylauncher.renderer.engine.Engine;
import org.easylauncher.renderer.engine.exception.shader.ShaderGLException;
import org.easylauncher.renderer.engine.exception.shader.ShaderLoadException;
import org.easylauncher.renderer.engine.graph.texture.Texture;
import org.easylauncher.renderer.engine.scene.Scene;
import org.easylauncher.renderer.javafx.behavior.InteractivePaneBehavior;

import java.util.UUID;
import java.util.function.Consumer;

public final class RendererPane extends StackPane {

    private static final String DEFAULT_STYLE_CLASS = "renderer-pane";

    @Getter
    private RendererContext renderContext;
    private GLCanvas canvas;
    private SceneComposition sceneComposition;

    private InteractivePaneBehavior interactivePaneBehavior;
    private boolean uuidChanged;
    private boolean capeChanged;
    private boolean skinChanged;

    public RendererPane() {
        initialize();
    }

    private void initialize() {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    public void initialize(RenderOptions renderOptions, Consumer<RendererPaneCustomizer> customizer) {
        RendererPaneCustomizerBase customizerBase = new RendererPaneCustomizerBase();
        customizer.accept(customizerBase);

        this.renderContext = Engine.CURRENT.loadContext(renderOptions, customizerBase.getViewDesire());
        this.canvas = new GLCanvas(LWJGLExecutor.LWJGL_MODULE, GLProfile.Core, false, 4, true);

        canvas.addOnInitEvent(event -> onCanvasInit(renderOptions, customizerBase.getCompositionMaker()));
        canvas.addOnReshapeEvent(event -> onCanvasReshape(event.width, event.height));
        canvas.addOnRenderEvent(event -> onCanvasRender(event.fps, event.delta));
        canvas.addOnDisposeEvent(event -> onCanvasDispose());

        if (customizerBase.isInteractive())
            this.interactivePaneBehavior = new InteractivePaneBehavior(this, customizerBase.getMouseSensitivity());

        getChildren().add(canvas);
    }

    public void updateGraphics() {
        if (canvas != null) {
            canvas.repaint();
        }
    }

    private void onCanvasInit(RenderOptions renderOptions, SceneComposition.Maker compositionMaker) {
        try {
            renderContext.initialize();

            Scene scene = renderContext.getScene();
            this.sceneComposition = compositionMaker.make(scene, renderOptions);
        } catch (ShaderGLException | ShaderLoadException ex) {
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
                renderContext.render();
            } catch (ShaderGLException ex) {
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

    // --- player skin texture
    private ObjectProperty<Texture> playerSkin;
    public Texture getPlayerSkin() { return playerSkin != null ? playerSkin.get() : null; }
    public void setPlayerSkin(Texture value) { playerSkinProperty().set(value); }
    public ObjectProperty<Texture> playerSkinProperty() {
        if (playerSkin == null) {
            this.playerSkin = new SimpleObjectProperty<>(this, "Player Skin");
            this.playerSkin.addListener((a, from, to) -> this.skinChanged = true);
        }

        return playerSkin;
    }

    // --- player cape texture
    private ObjectProperty<Texture> playerCape;
    public Texture getPlayerCape() { return playerCape != null ? playerCape.get() : null; }
    public void setPlayerCape(Texture value) { playerCapeProperty().set(value); }
    public ObjectProperty<Texture> playerCapeProperty() {
        if (playerCape == null) {
            this.playerCape = new SimpleObjectProperty<>(this, "Player Cape");
            this.playerCape.addListener((a, from, to) -> this.capeChanged = true);
        }

        return playerCape;
    }

}
