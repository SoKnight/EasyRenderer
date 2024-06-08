package org.easylauncher.renderer.javafx.texture.validation.exception;

import lombok.Getter;

@Getter
public class TooSmallTextureException extends AbstractTextureException {

    private final int width;
    private final int height;

    public TooSmallTextureException(int width, int height) {
        super("Texture is too small (size: %d x %d)".formatted(width, height));
        this.width = width;
        this.height = height;
    }

}
