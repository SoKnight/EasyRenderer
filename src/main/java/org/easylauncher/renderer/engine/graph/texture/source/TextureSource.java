package org.easylauncher.renderer.engine.graph.texture.source;

import org.easylauncher.renderer.engine.exception.texture.TextureLoadException;
import org.easylauncher.renderer.engine.graph.texture.Texture;

import java.net.URL;
import java.nio.file.Path;
import java.util.function.Consumer;

public interface TextureSource {

    static FileTextureSource fromFile(Path filePath, Consumer<TextureLoadException> failureHandler) {
        return new FileTextureSource(filePath, failureHandler);
    }

    static URLTextureSource fromURL(URL url, Consumer<TextureLoadException> failureHandler) {
        return new URLTextureSource(url, failureHandler);
    }

    boolean isLoaded();

    Texture load() throws TextureLoadException;

}
