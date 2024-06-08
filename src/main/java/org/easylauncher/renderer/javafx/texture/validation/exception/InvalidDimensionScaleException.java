package org.easylauncher.renderer.javafx.texture.validation.exception;

import lombok.Getter;

@Getter
public class InvalidDimensionScaleException extends AbstractTextureException {

    private final Dimension dimension;
    private final double scale;

    public InvalidDimensionScaleException(Dimension dimension, double scale) {
        super("Texture has an invalid scale by %s: %.2f".formatted(dimension.name().toLowerCase(), scale));
        this.dimension = dimension;
        this.scale = scale;
    }

    public enum Dimension {
        WIDTH, HEIGHT
    }

}
