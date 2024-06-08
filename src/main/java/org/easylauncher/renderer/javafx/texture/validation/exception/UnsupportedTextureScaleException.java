package org.easylauncher.renderer.javafx.texture.validation.exception;

import lombok.Getter;

@Getter
public class UnsupportedTextureScaleException extends AbstractTextureException {

    private final int width;
    private final int height;
    private final int scale;

    public UnsupportedTextureScaleException(int width, int height, int scale) {
        super("Texture has an invalid scale (size: %d x %d, scale: x%d)".formatted(width, height, scale));
        this.width = width;
        this.height = height;
        this.scale = scale;
    }

}
