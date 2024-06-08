package org.easylauncher.renderer.javafx.texture.transparency;

import org.easylauncher.renderer.javafx.texture.TexturePart;
import org.easylauncher.renderer.javafx.texture.TexturePartFace;

import java.util.Map;

public record TransparencyTestResult(TexturePart part, Map<TexturePartFace, Float> results) {

    public TexturePartFace[] getTransparentFaces() {
        return results.entrySet().stream()
                .filter(entry -> entry.getValue() == 1F)
                .map(Map.Entry::getKey)
                .toArray(TexturePartFace[]::new);
    }

    public TexturePartFace[] getSemitransparentFaces() {
        return results.entrySet().stream()
                .filter(entry -> entry.getValue() < 1F)
                .map(Map.Entry::getKey)
                .toArray(TexturePartFace[]::new);
    }

    public TexturePartFace[] getNonOpaqueFaces() {
        return results.entrySet().stream()
                .filter(entry -> entry.getValue() != 0F)
                .map(Map.Entry::getKey)
                .toArray(TexturePartFace[]::new);
    }

    public TexturePartFace[] getOpaqueFaces() {
        return results.entrySet().stream()
                .filter(entry -> entry.getValue() == 0F)
                .map(Map.Entry::getKey)
                .toArray(TexturePartFace[]::new);
    }

    public boolean hasTransparentFaces() {
        return results.values().stream().anyMatch(v -> v == 1F);
    }

    public boolean hasSemitransparentFaces() {
        return results.values().stream().anyMatch(v -> v < 1F);
    }

    public boolean hasNonOpaqueFaces() {
        return results.values().stream().anyMatch(v -> v != 0F);
    }

    public boolean hasOpaqueFaces() {
        return results.values().stream().anyMatch(v -> v == 0F);
    }

}
