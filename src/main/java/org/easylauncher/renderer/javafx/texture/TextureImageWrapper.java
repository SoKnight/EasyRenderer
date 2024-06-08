package org.easylauncher.renderer.javafx.texture;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.easylauncher.renderer.engine.graph.texture.source.TextureSource;
import org.easylauncher.renderer.javafx.texture.transparency.TransparencyTestResult;
import org.easylauncher.renderer.javafx.texture.validation.exception.*;

import java.util.HashMap;
import java.util.Map;

import static org.easylauncher.renderer.javafx.texture.validation.TextureValidator.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TextureImageWrapper {

    private final Image image;
    private final TextureType textureType;
    private final boolean thinArms;
    private final int scale;

    public Map<TexturePart, TransparencyTestResult> doTransparencyTest() {
        
    }

    public TransparencyTestResult doTransparencyTest(TexturePart part) {
        Map<TexturePartFace, Float> results = new HashMap<>();

        for (TexturePartFace face : TexturePartFace.values())
            results.put(face, doTransparencyTest(part, face));

        return new TransparencyTestResult(part, results);
    }

    public float doTransparencyTest(TexturePart part, TexturePartFace face) {
        int[] bounds = part.getFaceTextureBounds(face, thinArms, scale);
        int x = bounds[0], y = bounds[1];
        int w = bounds[2], h = bounds[3];

        int[] buffer = new int[w * h * 4];
        image.getPixelReader().getPixels(x, y, w, h, PixelFormat.getIntArgbInstance(), buffer, 0, w * 4);

        int transparentPixels = 0;
        for (int pixel : buffer)
            if (((pixel >> 24) & 0xFF) != 0xFF)
                transparentPixels++;

        return (float) transparentPixels / bounds.length;
    }

    public TextureSource toTextureSource() {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        byte[] buffer = new byte[width * height * 4];
        image.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getByteBgraInstance(), buffer, 0, width * 4);

        // convert BGRA to RGBA
        byte temp;
        for (int i = 0; i < buffer.length; i += 4) {
            temp = buffer[i];           // read blue
            buffer[i] = buffer[i + 2];  // write red
            buffer[i + 2] = temp;       // write blue
        }

        return TextureSource.fromBytes(buffer);
    }

    public static TextureImageWrapper wrapCape(Image image) throws
            TooSmallTextureException,
            TooLargeTextureException,
            InvalidAspectRatioException,
            InvalidDimensionScaleException,
            UnsupportedTextureScaleException
    {
        requireLoadedImage(image);

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        validateTextureImageSize(width, height, TextureType.CAPE);

        return wrap(image, width, height, TextureType.CAPE, false);
    }

    public static TextureImageWrapper wrapSkin(Image image, boolean thinArms) throws
            TooSmallTextureException,
            TooLargeTextureException,
            InvalidAspectRatioException,
            InvalidDimensionScaleException,
            UnsupportedTextureScaleException
    {
        requireLoadedImage(image);

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        try {
            validateTextureImageSize(width, height, TextureType.MODERN_SKIN);
            return wrap(image, width, height, TextureType.MODERN_SKIN, thinArms);
        } catch (AbstractTextureException ignored) {
            validateTextureImageSize(width, height, TextureType.LEGACY_SKIN);
            return wrap(image, width, height, TextureType.LEGACY_SKIN, thinArms);
        }
    }

    private static TextureImageWrapper wrap(
            Image image,
            int width,
            int height,
            TextureType textureType,
            boolean thinArms
    ) throws UnsupportedTextureScaleException {
        int scale = width / textureType.minWidth();
        validateTextureScale(width, height, scale);
        return new TextureImageWrapper(image, textureType, thinArms, scale);
    }

}
