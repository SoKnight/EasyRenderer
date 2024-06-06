package org.easylauncher.renderer.context;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ViewDesire {

    CAPE(210F),
    SKIN(-30F),
    ;

    private final float sceneAngleY;

}
