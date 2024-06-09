package org.easylauncher.renderer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.context.ViewDesire;
import org.easylauncher.renderer.javafx.RendererPane;
import org.easylauncher.renderer.javafx.texture.TextureImageWrapper;

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
        RendererPane rendererPane = new RendererPane();
        RenderOptions renderOptions = new RenderOptions();

        TextureImageWrapper capeImage = TextureImageWrapper.wrapCape(new Image(capePath.toUri().toURL().toExternalForm()));
        TextureImageWrapper skinImage = TextureImageWrapper.wrapSkin(new Image(skinPath.toUri().toURL().toExternalForm()), false);

        rendererPane.setPlayerUUID(UUID.randomUUID());
        rendererPane.setCapeTexture(capeImage);
        rendererPane.setSkinTexture(skinImage);

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
    }

    public static void main(String[] args) {
        Application.launch();
    }

}
