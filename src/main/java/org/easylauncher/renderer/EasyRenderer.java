package org.easylauncher.renderer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.context.ViewDesire;
import org.easylauncher.renderer.engine.exception.texture.TextureLoadException;
import org.easylauncher.renderer.javafx.RendererPane;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.easylauncher.renderer.engine.graph.texture.TextureLoader.loadFrom;

public final class EasyRenderer extends Application {

    private final Path examplesDir;
    private Path capePath, skinPath;

    public EasyRenderer() {
        this.examplesDir = Paths.get("examples");
        this.capePath = examplesDir.resolve("capes").resolve("15year.png");
        this.skinPath = examplesDir.resolve("skins").resolve("soknight.png");
    }

    @Override
    public void start(Stage stage) throws Exception {
        List<RendererPane> rendererPanes = new ArrayList<>();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(12);
        gridPane.setVgap(12);
        gridPane.setPadding(new Insets(24D));
        gridPane.setStyle("-fx-background-color: #1B1E23;");

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 7; j++) {
                RendererPane rendererPane = new RendererPane();
                GridPane.setHgrow(rendererPane, Priority.ALWAYS);
                GridPane.setVgrow(rendererPane, Priority.ALWAYS);
                gridPane.add(rendererPane, i, j);
                rendererPanes.add(rendererPane);
            }
        }

        RenderOptions renderOptions = new RenderOptions();

        Consumer<RendererPane> sceneSetupFunction = pane -> {
            pane.loadDefaultSceneComposition(renderOptions);
            refreshPaneContent(pane);
        };

        for (int i = 0; i < rendererPanes.size(); i++) {
            RendererPane rendererPane = rendererPanes.get(i);
            rendererPane.postInitialize(
                    renderOptions,
                    i % 2 == 0 ? ViewDesire.SKIN : ViewDesire.CAPE,
                    sceneSetupFunction,
                    Throwable::printStackTrace
            );
        }

        Scene scene = new Scene(gridPane, 600D, 400D);
        stage.setScene(scene);
        stage.setTitle("EasyRenderer / JavaFX 21");
        stage.setOnCloseRequest(event -> {
            for (RendererPane rendererPane : rendererPanes) {
                rendererPane.getCanvas().dispose();
            }
        });
        stage.show();
    }

    private void refreshPaneContent(RendererPane pane) {
        try {
            pane.updateCapeTexture(loadFrom(capePath));
            pane.updateSkinTexture(loadFrom(skinPath));
            pane.getCanvas().repaint();
        } catch (TextureLoadException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) {
        Application.launch();
    }

}
