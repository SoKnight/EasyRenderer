package org.easylauncher.framework.gamerender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
public final class RenderOptions {

    public static final int ALL_SKIN_LAYERS = 4095;

    private int visibleLayersMask;
    private boolean hideCape;

    public RenderOptions() {
        this.visibleLayersMask = ALL_SKIN_LAYERS;
        this.hideCape = false;
    }

    public boolean isInnerLayerVisible(SkinLayer layer) {
        return (visibleLayersMask & layer.innerBit()) == layer.innerBit();
    }

    public boolean isOuterLayerVisible(SkinLayer layer) {
        return (visibleLayersMask & layer.outerBit()) == layer.outerBit();
    }

    @Getter
    @AllArgsConstructor
    public enum SkinLayer {

        HEAD        (1,     2),
        BODY        (4,     8),
        LEFT_ARM    (16,    32),
        RIGHT_ARM   (64,    128),
        LEFT_LEG    (256,   512),
        RIGHT_LEG   (1024,  2048),
        ;

        private final int innerBit;
        private final int outerBit;

    }

}
