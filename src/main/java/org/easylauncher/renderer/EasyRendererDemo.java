package org.easylauncher.renderer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.context.ViewDesire;
import org.easylauncher.renderer.engine.graph.texture.source.TextureSource;
import org.easylauncher.renderer.javafx.RendererPane;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public final class EasyRendererDemo extends Application {

    private final Path examplesDir;
    private Path capePath, skinPath;

    public EasyRendererDemo() {
        this.examplesDir = Paths.get("examples");
        this.capePath = examplesDir.resolve("capes").resolve("15year.png");
        this.skinPath = examplesDir.resolve("skins").resolve("soknight.png");
    }

    @Override
    public void start(Stage stage) throws Exception {
//        List<RendererPane> rendererPanes = new ArrayList<>();
//
//        GridPane gridPane = new GridPane();
//        gridPane.setHgap(12);
//        gridPane.setVgap(12);
//        gridPane.setPadding(new Insets(24D));
//        gridPane.setStyle("-fx-background-color: #1B1E23;");
//
//        for (int i = 0; i < 5; i++) {
//            for (int j = 0; j < 3; j++) {
//                RendererPane rendererPane = new RendererPane();
//                GridPane.setHgrow(rendererPane, Priority.ALWAYS);
//                GridPane.setVgrow(rendererPane, Priority.ALWAYS);
//                gridPane.add(rendererPane, i, j);
//
//                rendererPane.setPlayerCape(TextureSource.fromFile(capePath));
////                rendererPane.setPlayerSkin(TextureSource.fromFile(skinPath));
//                rendererPane.setPlayerUUID(UUID.randomUUID());
//
//                rendererPanes.add(rendererPane);
//            }
//        }

        RendererPane rendererPane = new RendererPane();
        RenderOptions renderOptions = new RenderOptions();

        rendererPane.setPlayerUUID(UUID.randomUUID());
        rendererPane.setPlayerCape(TextureSource.fromFile(capePath));

        HBox root = new HBox(24D, rendererPane);
        HBox.setHgrow(rendererPane, Priority.ALWAYS);
        root.setPadding(new Insets(24D));

        Scene scene = new Scene(root, 300D, 400D);
        stage.setScene(scene);
        stage.setTitle("EasyRenderer / JavaFX 21");
        stage.setOnCloseRequest(event -> RendererPane.cleanupAll());
        stage.show();

        rendererPane.initialize(renderOptions, customizer -> customizer
                .desireView(ViewDesire.SKIN)
                .makeInteractive());

        rendererPane.bind();

//        for (int i = 0; i < rendererPanes.size(); i++) {
//            RendererPane rendererPane = rendererPanes.get(i);
//            ViewDesire viewDesire = i % 2 == 0 ? ViewDesire.SKIN : ViewDesire.CAPE;
//
//            rendererPane.initialize(renderOptions, customizer -> customizer
//                    .desireView(viewDesire)
//                    .makeInteractive());
//
//            rendererPane.bind();
//        }
    }

    public static void main(String[] args) {
        Application.launch();
    }

}
