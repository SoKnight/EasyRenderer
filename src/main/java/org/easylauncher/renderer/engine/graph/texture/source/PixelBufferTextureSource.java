package org.easylauncher.renderer.engine.graph.texture.source;

import lombok.Getter;
import org.easylauncher.renderer.engine.graph.texture.Texture;
import org.easylauncher.renderer.engine.graph.texture.TextureLoader;

@Getter
public final class PixelBufferTextureSource extends TextureSourceBase {

    private final int width, height;
    private final byte[] pixelBuffer;

    PixelBufferTextureSource(int width, int height, byte[] pixelBuffer) {
        this.width = width;
        this.height = height;
        this.pixelBuffer = pixelBuffer;
    }

    @Override
    protected Texture loadInternal() {
        return TextureLoader.loadFromPixelBuffer(width, height, pixelBuffer);
    }

}
