package org.easylauncher.renderer.context;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static org.easylauncher.renderer.context.SkinPart.ALL_LAYERS;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
public final class RenderOptions {

    private int visibleLayersMask;
    private boolean showCape;
    private boolean skinThinArms;
    private boolean legacySkinTexture;
    private int capeScale, skinScale;

    public RenderOptions() {
        this.visibleLayersMask = ALL_LAYERS;
        this.showCape = true;
        this.skinThinArms = false;
        this.legacySkinTexture = false;
        this.capeScale = 1;
        this.skinScale = 1;
    }

    public boolean isInnerLayerVisible(SkinPart layer) {
        return isLayerVisible(layer.getInnerBit());
    }

    public boolean isOuterLayerVisible(SkinPart layer) {
        return isLayerVisible(layer.getOuterBit());
    }

    private boolean isLayerVisible(int layerBit) {
        return (visibleLayersMask & layerBit) == layerBit;
    }

}
