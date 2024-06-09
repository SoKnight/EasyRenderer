package org.easylauncher.renderer.javafx.texture.transparency;

import org.easylauncher.renderer.javafx.texture.TexturePart;
import org.easylauncher.renderer.javafx.texture.TextureType;

import java.util.Map;
import java.util.function.Predicate;

public record TextureTestResult(TextureType type, Map<TexturePart, PartTestResult> results) {

    public TexturePart[] filterResults(Predicate<PartTestResult> filter) {
        return results.entrySet().stream()
                .filter(entry -> filter.test(entry.getValue()))
                .map(Map.Entry::getKey)
                .toArray(TexturePart[]::new);
    }

    public PartTestResult get(TexturePart part) {
        return results.get(part);
    }

    public boolean hasParts(Predicate<PartTestResult> filter) {
        return results.values().stream().anyMatch(filter);
    }

}
