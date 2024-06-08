package org.easylauncher.renderer.javafx.texture.validation.exception;

import lombok.Getter;

@Getter
public class TooLargeTextureException extends AbstractTextureException {

    private final int width;
    private final int height;

    public TooLargeTextureException(int width, int height) {
        super("Texture is too large (size: %d x %d)".formatted(width, height));
        this.width = width;
        this.height = height;
    }

}
