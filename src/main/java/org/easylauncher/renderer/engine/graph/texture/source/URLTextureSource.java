package org.easylauncher.renderer.engine.graph.texture.source;

import lombok.Getter;
import org.easylauncher.renderer.engine.exception.texture.TextureLoadException;
import org.easylauncher.renderer.engine.graph.texture.Texture;
import org.easylauncher.renderer.engine.graph.texture.TextureLoader;

import java.net.URL;

@Getter
public final class URLTextureSource extends TextureSourceBase {

    private final URL url;

    URLTextureSource(URL url) {
        this.url = url;
    }

    @Override
    protected Texture loadInternal() throws TextureLoadException {
        return TextureLoader.loadFrom(url);
    }

}
