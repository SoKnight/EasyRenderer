package org.easylauncher.renderer.engine.graph.texture.source;

import org.easylauncher.renderer.engine.exception.texture.TextureLoadException;
import org.easylauncher.renderer.engine.graph.texture.Texture;

import java.util.Objects;

abstract class TextureSourceBase implements TextureSource {

    private Texture texture;
    private boolean loaded;

    @Override
    public Texture getLoaded() {
        return texture;
    }

    @Override
    public Texture getOrLoadTexture() throws TextureLoadException {
        return loaded ? getLoaded() : load();
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public Texture load() throws TextureLoadException {
        if (!loaded) {
            this.texture = loadInternal();
            Objects.requireNonNull(texture);
            this.loaded = true;
            return texture;
        }

        return null;
    }

    protected abstract Texture loadInternal() throws TextureLoadException;

}
