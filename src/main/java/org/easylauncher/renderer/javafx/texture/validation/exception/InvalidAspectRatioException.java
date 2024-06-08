package org.easylauncher.renderer.javafx.texture.validation.exception;

import lombok.Getter;

@Getter
public class InvalidAspectRatioException extends AbstractTextureException {

    private final int width;
    private final int height;

    public InvalidAspectRatioException(int width, int height) {
        super("Texture has an invalid aspect ratio (size: %d x %d)".formatted(width, height));
        this.width = width;
        this.height = height;
    }

}
