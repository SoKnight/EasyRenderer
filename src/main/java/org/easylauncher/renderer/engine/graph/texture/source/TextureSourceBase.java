package org.easylauncher.renderer.engine.graph.texture.source;

import lombok.RequiredArgsConstructor;
import org.easylauncher.renderer.engine.exception.texture.TextureLoadException;
import org.easylauncher.renderer.engine.graph.texture.Texture;

import java.util.Objects;
import java.util.function.Consumer;

@RequiredArgsConstructor
abstract class TextureSourceBase implements TextureSource {

    private final Consumer<TextureLoadException> failureHandler;
    private boolean loaded;

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public Texture load() {
        try {
            Texture texture = loadInternal();
            Objects.requireNonNull(texture);
            this.loaded = true;
            return texture;
        } catch (TextureLoadException ex) {
            failureHandler.accept(ex);
            this.loaded = false;
            return null;
        }
    }

    protected abstract Texture loadInternal() throws TextureLoadException;

}
