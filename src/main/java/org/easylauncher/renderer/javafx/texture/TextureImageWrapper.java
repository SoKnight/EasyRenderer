package org.easylauncher.renderer.javafx.texture;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import lombok.Getter;
import org.easylauncher.renderer.engine.graph.texture.source.TextureSource;
import org.easylauncher.renderer.javafx.texture.transparency.FaceTestResult;
import org.easylauncher.renderer.javafx.texture.transparency.PartTestResult;
import org.easylauncher.renderer.javafx.texture.transparency.TextureTestResult;
import org.easylauncher.renderer.javafx.texture.validation.exception.*;

import java.util.HashMap;
import java.util.Map;

import static org.easylauncher.renderer.context.SkinPart.ALL_LAYERS;
import static org.easylauncher.renderer.javafx.texture.TexturePart.*;
import static org.easylauncher.renderer.javafx.texture.validation.TextureValidator.*;

@Getter
public class TextureImageWrapper {

    public static final TexturePart[] CAPE_TEXTURE_PARTS;
    public static final TexturePart[] LEGACY_SKIN_TEXTURE_PARTS;
    public static final TexturePart[] MODERN_SKIN_TEXTURE_PARTS;

    private final Image image;
    private final TextureType textureType;
    private final boolean thinArms;
    private final int scale;
    private int visibleLayersMask;

    private TextureImageWrapper(Image image, TextureType textureType, boolean thinArms, int scale) {
        this.image = image;
        this.textureType = textureType;
        this.thinArms = thinArms;
        this.scale = scale;
        this.visibleLayersMask = textureType.isSkin() ? ALL_LAYERS : 0;
    }

    public void runVisibleLayersTest() {
        if (textureType.isCape()) {
            this.visibleLayersMask = 0;
            return;
        }

        int newMask = 0;
        TextureTestResult textureResult = doTransparencyTest();
        for (PartTestResult partResult : textureResult.results().values())
            if (!partResult.part().isOuterLayer() || partResult.anyMatch(FaceTestResult::isNotOpaque))
                newMask |= partResult.part().getLayerBit();

        this.visibleLayersMask = newMask;
    }

    public TextureTestResult doTransparencyTest() {
        return switch (textureType) {
            case CAPE -> doTransparencyTest(CAPE_TEXTURE_PARTS);
            case LEGACY_SKIN -> doTransparencyTest(LEGACY_SKIN_TEXTURE_PARTS);
            case MODERN_SKIN -> doTransparencyTest(MODERN_SKIN_TEXTURE_PARTS);
        };
    }

    public PartTestResult doTransparencyTest(TexturePart part) {
        if (part == null)
            return null;

        Map<TexturePartFace, FaceTestResult> results = new HashMap<>();
        for (TexturePartFace face : TexturePartFace.values())
            results.put(face, doTransparencyTest(part, face));

        return new PartTestResult(part, results);
    }

    public FaceTestResult doTransparencyTest(TexturePart part, TexturePartFace face) {
        if (part == null || face == null)
            return null;

        int[] bounds = part.getFaceTextureBounds(face, thinArms, scale);
        int x = bounds[0], y = bounds[1];
        int w = bounds[2], h = bounds[3];

        int[] buffer = new int[w * h];
        image.getPixelReader().getPixels(x, y, w, h, PixelFormat.getIntArgbInstance(), buffer, 0, w);

        int transparentPixels = 0;
        for (int pixel : buffer)
            if (((pixel >> 24) & 0xFF) != 0xFF)
                transparentPixels++;

        return new FaceTestResult(transparentPixels, buffer.length);
    }

    private TextureTestResult doTransparencyTest(TexturePart[] textureParts) {
        if (textureParts == null || textureParts.length == 0)
            return null;

        Map<TexturePart, PartTestResult> results = new HashMap<>();
        for (TexturePart texturePart : textureParts)
            results.put(texturePart, doTransparencyTest(texturePart));

        return new TextureTestResult(textureType, results);
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

        return TextureSource.fromPixelBuffer(width, height, buffer);
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

    static {
        CAPE_TEXTURE_PARTS = new TexturePart[] {
                CAPE,
        };

        LEGACY_SKIN_TEXTURE_PARTS = new TexturePart[] {
                HEAD_INNER, HEAD_OUTER,
                BODY_INNER,
                RIGHT_ARM_INNER,
                RIGHT_LEG_INNER,
        };

        MODERN_SKIN_TEXTURE_PARTS = new TexturePart[] {
                HEAD_INNER, HEAD_OUTER,
                BODY_INNER, BODY_OUTER,
                LEFT_ARM_INNER, LEFT_ARM_OUTER,
                RIGHT_ARM_INNER, RIGHT_ARM_OUTER,
                LEFT_LEG_INNER, LEFT_LEG_OUTER,
                RIGHT_LEG_INNER, RIGHT_LEG_OUTER,
        };
    }

}
