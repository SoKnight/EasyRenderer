package org.easylauncher.renderer.engine.graph.texture.source;

import lombok.Getter;
import org.easylauncher.renderer.engine.exception.texture.TextureLoadException;
import org.easylauncher.renderer.engine.graph.texture.Texture;
import org.easylauncher.renderer.engine.graph.texture.TextureLoader;

import java.nio.file.Path;
import java.util.function.Consumer;

@Getter
public final class FileTextureSource extends TextureSourceBase {

    private final Path filePath;

    FileTextureSource(Path filePath, Consumer<TextureLoadException> failureHandler) {
        super(failureHandler);
        this.filePath = filePath;
    }

    @Override
    protected Texture loadInternal() throws TextureLoadException {
        return TextureLoader.loadFrom(filePath);
    }

}
