package org.easylauncher.renderer.javafx.texture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public enum TextureType {

    CAPE        (64, 1024, 32, 512, 2),
    LEGACY_SKIN (64, 1024, 32, 512, 2),
    MODERN_SKIN (64, 1024, 64, 1024, 1),
    ;

    private final int minWidth, maxWidth;
    private final int minHeight, maxHeight;
    private final int aspectRatio;

    public boolean isCape() {
        return this == CAPE;
    }

    public boolean isSkin() {
        return !isCape();
    }

}
