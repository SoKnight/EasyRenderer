package org.easylauncher.renderer.context;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
public final class RenderOptions {

    private int visibleLayersMask;
    private boolean showCape;
    private boolean thinArms;

    public RenderOptions() {
        this.visibleLayersMask = SkinPart.ALL_LAYERS;
        this.showCape = true;
        this.thinArms = false;
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
