package org.easylauncher.renderer.javafx.texture.transparency;

public record FaceTestResult(int transparentPixels, int totalPixels) {

    public float computeTransparencyPercent() {
        return (float) transparentPixels / totalPixels;
    }

    public boolean isTransparent() {
        return transparentPixels == totalPixels;
    }

    public boolean isSemitransparent() {
        return transparentPixels > 0 && transparentPixels < totalPixels;
    }

    public boolean isOpaque() {
        return transparentPixels == 0;
    }

    public boolean isNotOpaque() {
        return transparentPixels != 0;
    }

}
