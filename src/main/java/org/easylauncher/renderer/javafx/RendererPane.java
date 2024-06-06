package org.easylauncher.renderer.javafx;

import com.huskerdev.openglfx.canvas.GLCanvas;
import com.huskerdev.openglfx.canvas.GLProfile;
import com.huskerdev.openglfx.lwjgl.LWJGLExecutor;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.context.RendererContext;
import org.easylauncher.renderer.context.ViewDesire;
import org.easylauncher.renderer.engine.Engine;
import org.easylauncher.renderer.engine.exception.shader.ShaderGLException;
import org.easylauncher.renderer.engine.exception.shader.ShaderLoadException;
import org.easylauncher.renderer.engine.graph.texture.Texture;
import org.easylauncher.renderer.engine.scene.Scene;
import org.easylauncher.renderer.game.cape.CapeModel;
import org.easylauncher.renderer.game.skin.model.ArmModel;
import org.easylauncher.renderer.game.skin.model.BodyModel;
import org.easylauncher.renderer.game.skin.model.HeadModel;
import org.easylauncher.renderer.game.skin.model.LegModel;

import java.util.function.Consumer;

@Getter
public class RendererPane extends StackPane {

    private static final String DEFAULT_STYLE_CLASS = "renderer-pane";
    private static final float MOUSE_SENSITIVITY = 0.5F;

    private RendererContext renderContext;
    private GLCanvas canvas;

    private double previousX, previousY;

    public RendererPane() {
        initialize();
    }

    private void initialize() {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    public void postInitialize(RenderOptions renderOptions, ViewDesire viewDesire, Consumer<RendererPane> sceneSetupFunction, Consumer<Exception> failureHandler) {
        this.renderContext = Engine.CURRENT.loadContext(renderOptions, viewDesire);
        this.canvas = new GLCanvas(LWJGLExecutor.LWJGL_MODULE, GLProfile.Core, false, 4, true);

        canvas.addOnInitEvent(event -> onCanvasInit(sceneSetupFunction, failureHandler));
        canvas.addOnReshapeEvent(event -> onCanvasReshape(event.width, event.height));
        canvas.addOnRenderEvent(event -> onCanvasRender(event.fps, event.delta, failureHandler));
        canvas.addOnDisposeEvent(event -> onCanvasDispose());

        getChildren().add(canvas);

        setOnMouseMoved(event -> {
            this.previousX = event.getX();
            this.previousY = event.getY();
        });

        setOnMouseDragged(event -> {
            if (previousX != 0D || previousY != 0D) {
                double dx = event.getX() - previousX;
                double dy = event.getY() - previousY;

                if (dx != 0D || dy != 0D) {
                    renderContext.rotateSceneBy(
                            (float) dy * MOUSE_SENSITIVITY,
                            (float) dx * MOUSE_SENSITIVITY
                    );
                    canvas.repaint();
                }
            }

            this.previousX = event.getX();
            this.previousY = event.getY();
        });
    }

    public void loadDefaultSceneComposition(RenderOptions renderOptions) {
        Scene scene = renderContext.getScene();

        HeadModel headModel = new HeadModel();
        BodyModel bodyModel = new BodyModel();
        ArmModel leftArmModel = new ArmModel(true);
        ArmModel rightArmModel = new ArmModel(false);
        LegModel leftLegModel = new LegModel(true);
        LegModel rightLegModel = new LegModel(false);
        CapeModel capeModel = new CapeModel();

        scene.addModel(headModel);
        scene.addModel(bodyModel);
        scene.addModel(leftArmModel);
        scene.addModel(rightArmModel);
        scene.addModel(leftLegModel);
        scene.addModel(rightLegModel);
        scene.addModel(capeModel);

        scene.addEntity(headModel.createEntity(renderOptions));
        scene.addEntity(bodyModel.createEntity(renderOptions));
        scene.addEntity(leftArmModel.createEntity(renderOptions));
        scene.addEntity(rightArmModel.createEntity(renderOptions));
        scene.addEntity(leftLegModel.createEntity(renderOptions));
        scene.addEntity(rightLegModel.createEntity(renderOptions));
        scene.addEntity(capeModel.createEntity(renderOptions));
    }

    protected void onCanvasInit(Consumer<RendererPane> sceneSetupFunction, Consumer<Exception> failureHandler) {
        try {
            renderContext.initialize();
            sceneSetupFunction.accept(this);
        } catch (ShaderGLException | ShaderLoadException ex) {
            failureHandler.accept(ex);
        }
    }

    protected void onCanvasReshape(int width, int height) {
        if (renderContext.isAvailable()) {
            renderContext.resize(width, height);
        }
    }

    protected void onCanvasRender(int fps, double delta, Consumer<Exception> failureHandler) {
        if (renderContext.isAvailable()) {
            try {
                renderContext.render();
            } catch (ShaderGLException ex) {
                failureHandler.accept(ex);
            }
        }
    }

    protected void onCanvasDispose() {
        if (renderContext.isAvailable()) {
            renderContext.unload();
        }
    }

    public void updateCapeTexture(Texture texture) {
        renderContext.getScene().getCapeMaterial().setTexture(texture);
        canvas.repaint();
    }

    public void updateSkinTexture(Texture texture) {
        renderContext.getScene().getSkinMaterial().setTexture(texture);
        canvas.repaint();
    }

}
