package org.easylauncher.renderer.javafx.texture.transparency;

import org.easylauncher.renderer.javafx.texture.TexturePart;
import org.easylauncher.renderer.javafx.texture.TexturePartFace;

import java.util.Map;
import java.util.function.Predicate;

public record PartTestResult(TexturePart part, Map<TexturePartFace, FaceTestResult> results) {

    public TexturePartFace[] filterResults(Predicate<FaceTestResult> filter) {
        return results.entrySet().stream()
                .filter(entry -> filter.test(entry.getValue()))
                .map(Map.Entry::getKey)
                .toArray(TexturePartFace[]::new);
    }

    public boolean allMatch(Predicate<FaceTestResult> filter) {
        return results.values().stream().allMatch(filter);
    }

    public boolean anyMatch(Predicate<FaceTestResult> filter) {
        return results.values().stream().anyMatch(filter);
    }

    public boolean noneMatch(Predicate<FaceTestResult> filter) {
        return results.values().stream().noneMatch(filter);
    }

    public FaceTestResult get(TexturePartFace face) {
        return results.get(face);
    }

    public boolean hasFaces(Predicate<FaceTestResult> filter) {
        return results.values().stream().anyMatch(filter);
    }

}
