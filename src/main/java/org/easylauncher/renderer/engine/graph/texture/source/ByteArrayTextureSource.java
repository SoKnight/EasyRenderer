package org.easylauncher.renderer.engine.graph.texture.source;

import lombok.Getter;
import org.easylauncher.renderer.engine.exception.texture.TextureLoadException;
import org.easylauncher.renderer.engine.graph.texture.Texture;
import org.easylauncher.renderer.engine.graph.texture.TextureLoader;

import java.io.ByteArrayInputStream;

@Getter
public final class ByteArrayTextureSource extends TextureSourceBase {

    private final byte[] bytes;

    ByteArrayTextureSource(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    protected Texture loadInternal() throws TextureLoadException {
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        return TextureLoader.loadFrom(stream);
    }

}
