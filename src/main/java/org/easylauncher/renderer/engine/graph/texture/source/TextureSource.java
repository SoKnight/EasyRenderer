package org.easylauncher.renderer.engine.graph.texture.source;

import org.easylauncher.renderer.engine.exception.texture.TextureLoadException;
import org.easylauncher.renderer.engine.graph.texture.Texture;

import java.net.URL;
import java.nio.file.Path;

public interface TextureSource {

    static FileTextureSource fromFile(Path filePath) {
        return new FileTextureSource(filePath);
    }

    static PixelBufferTextureSource fromPixelBuffer(int width, int height, byte[] pixelBuffer) {
        return new PixelBufferTextureSource(width, height, pixelBuffer);
    }

    static ByteArrayTextureSource fromBytes(byte[] bytes) {
        return new ByteArrayTextureSource(bytes);
    }

    static URLTextureSource fromURL(URL url) {
        return new URLTextureSource(url);
    }

    Texture getLoaded();

    Texture getOrLoadTexture() throws TextureLoadException;

    boolean isLoaded();

    Texture load() throws TextureLoadException;

}
