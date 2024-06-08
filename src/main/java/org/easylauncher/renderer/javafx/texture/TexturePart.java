package org.easylauncher.renderer.javafx.texture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.easylauncher.renderer.context.SkinPart;

import static org.easylauncher.renderer.context.SkinPart.*;

@Getter
@AllArgsConstructor
public enum TexturePart {

    HEAD_INNER      ( 8,  8,  8,  0,  0, HEAD,      false),
    HEAD_OUTER      ( 8,  8,  8, 32,  0, HEAD,      true),
    BODY_INNER      ( 8, 12,  4, 16, 16, BODY,      false),
    BODY_OUTER      ( 8, 12,  4, 16, 32, BODY,      true),
    LEFT_ARM_INNER  ( 4, 12,  4, 32, 48, LEFT_ARM,  false),
    LEFT_ARM_OUTER  ( 4, 12,  4, 48, 48, LEFT_ARM,  true),
    RIGHT_ARM_INNER ( 4, 12,  4, 40, 16, RIGHT_ARM, false),
    RIGHT_ARM_OUTER ( 4, 12,  4, 40, 32, RIGHT_ARM, true),
    LEFT_LEG_INNER  ( 4, 12,  4, 16, 48, LEFT_LEG,  false),
    LEFT_LEG_OUTER  ( 4, 12,  4,  0, 48, LEFT_LEG,  true),
    RIGHT_LEG_INNER ( 4, 12,  4,  0, 16, RIGHT_LEG, false),
    RIGHT_LEG_OUTER ( 4, 12,  4,  0, 32, RIGHT_LEG, true),
    CAPE            (10, 16,  1,  0,  0, null,      false),
    ;

    private final int width, height, depth;
    private final int startX, startY;
    private final SkinPart skinPart;
    private final boolean outerLayer;

    public int[] getFaceTextureBounds(TexturePartFace face, boolean thinArms, int scale) {
        return face.getTextureBounds(this, thinArms, scale);
    }

    public int getLayerBit() {
        if (isCape())
            return 0;

        return outerLayer ? skinPart.getOuterBit() : skinPart.getInnerBit();
    }

    public boolean isArm() {
        return isSkin() && (skinPart == LEFT_ARM || skinPart == RIGHT_ARM);
    }

    public boolean isLeg() {
        return isSkin() && (skinPart == LEFT_LEG || skinPart == RIGHT_LEG);
    }

    public boolean isCape() {
        return this == CAPE;
    }

    public boolean isSkin() {
        return !isCape();
    }

}
