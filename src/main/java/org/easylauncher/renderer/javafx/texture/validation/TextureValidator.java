package org.easylauncher.renderer.javafx.texture.validation;

import javafx.scene.image.Image;
import org.easylauncher.renderer.javafx.texture.TextureType;
import org.easylauncher.renderer.javafx.texture.validation.exception.*;
import org.easylauncher.renderer.javafx.texture.validation.exception.InvalidDimensionScaleException.Dimension;

import java.util.Objects;

public final class TextureValidator {

    private static final int[] SUPPORTED_SCALE_FACTORS = new int[] { 1, 2, 4, 8, 16 };

    public static void validateTextureImageSize(int width, int height, TextureType textureType) throws
            TooSmallTextureException,
            TooLargeTextureException,
            InvalidAspectRatioException,
            InvalidDimensionScaleException
    {
        int minWidth = textureType.minWidth();
        int minHeight = textureType.minHeight();

        if (width < minWidth || height < minHeight)
            throw new TooSmallTextureException(width, height);

        if (width > textureType.maxWidth() || height > textureType.maxHeight())
            throw new TooLargeTextureException(width, height);

        int aspectRatio = width / height;
        if (aspectRatio != textureType.aspectRatio() || (width % height) != 0)
            throw new InvalidAspectRatioException(width, height);

        if (width / minWidth == 0 || width % minWidth != 0)
            throw new InvalidDimensionScaleException(Dimension.WIDTH, (double) width / minWidth);

        if (height / minHeight == 0 || height % minHeight != 0)
            throw new InvalidDimensionScaleException(Dimension.HEIGHT, (double) height / minHeight);
    }

    public static void validateTextureScale(int width, int height, int scale) throws UnsupportedTextureScaleException {
        if (scale >= 1 && scale <= 16)
            for (int supportedScaleFactor : SUPPORTED_SCALE_FACTORS)
                if (supportedScaleFactor == scale)
                    return;

        throw new UnsupportedTextureScaleException(width, height, scale);
    }

    public static void requireLoadedImage(Image image) {
        Objects.requireNonNull(image);

        if (image.isBackgroundLoading())
            throw new IllegalArgumentException("Image must be fully loaded!");

        if (image.isError())
            throw new IllegalArgumentException("Image wasn't loaded due to an error!", image.getException());
    }

}
