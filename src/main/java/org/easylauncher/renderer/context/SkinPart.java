package org.easylauncher.renderer.context;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SkinPart {

    HEAD(1, 2),
    BODY(4, 8),
    LEFT_ARM(16, 32),
    RIGHT_ARM(64, 128),
    LEFT_LEG(256, 512),
    RIGHT_LEG(1024, 2048),
    ;

    public static final int ALL_LAYERS = 4095;
    public static final int INNER_LAYERS = 1365;
    public static final int OUTER_LAYERS = 2730;

    private final int innerBit, outerBit;

}
