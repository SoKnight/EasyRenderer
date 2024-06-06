package org.easylauncher.renderer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.context.ViewDesire;
import org.easylauncher.renderer.engine.exception.texture.TextureLoadException;
import org.easylauncher.renderer.javafx.RendererPaneBase;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.easylauncher.renderer.engine.graph.texture.TextureLoader.loadFrom;

public final class EasyRendererDemo extends Application {

    private final Path examplesDir;
    private Path capePath, skinPath;

    public EasyRendererDemo() {
        this.examplesDir = Paths.get("examples");
        this.capePath = examplesDir.resolve("capes").resolve("15year.png");
        this.skinPath = examplesDir.resolve("defaults").resolve("soknight.png");
    }

    @Override
    public void start(Stage stage) throws Exception {
        List<RendererPaneBase> rendererPanes = new ArrayList<>();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(12);
        gridPane.setVgap(12);
        gridPane.setPadding(new Insets(24D));
        gridPane.setStyle("-fx-background-color: #1B1E23;");

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                RendererPaneBase rendererPane = new RendererPaneBase();
                GridPane.setHgrow(rendererPane, Priority.ALWAYS);
                GridPane.setVgrow(rendererPane, Priority.ALWAYS);
                gridPane.add(rendererPane, i, j);
                rendererPanes.add(rendererPane);
            }
        }

        RenderOptions renderOptions = new RenderOptions();

        Consumer<RendererPaneBase> sceneSetupFunction = pane -> {
            pane.loadDefaultSceneComposition(renderOptions);
            refreshPaneContent(pane);
        };

        Scene scene = new Scene(gridPane, 900D, 750D);
        stage.setScene(scene);
        stage.setTitle("EasyRenderer / JavaFX 21");
        stage.setOnCloseRequest(event -> {
            for (RendererPaneBase rendererPane : rendererPanes) {
                rendererPane.getCanvas().dispose();
            }
        });
        stage.show();

        for (int i = 0; i < rendererPanes.size(); i++) {
            RendererPaneBase rendererPane = rendererPanes.get(i);
            ViewDesire viewDesire = i % 2 == 0 ? ViewDesire.SKIN : ViewDesire.CAPE;
            Platform.runLater(() -> rendererPane.postInitialize(renderOptions, viewDesire, sceneSetupFunction, Throwable::printStackTrace));
        }
    }

    private void refreshPaneContent(RendererPaneBase pane) {
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
