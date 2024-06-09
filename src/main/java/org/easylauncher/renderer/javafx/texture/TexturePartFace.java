package org.easylauncher.renderer.javafx.texture;

public enum TexturePartFace {

    LEFT,
    FRONT,
    RIGHT,
    BACK,
    TOP,
    BOTTOM,
    ;

    // [ x, y, w, h ]
    public int[] getTextureBounds(TexturePart part, boolean thinArms, int scale) {
        int w = part.getWidth();
        int h = part.getHeight();
        int d = part.getDepth();

        int[] bounds = switch (this) {
            case LEFT -> new int[]      {0,         d,  d,  h};
            case FRONT -> new int[]     {d,         d,  w,  h};
            case RIGHT -> new int[]     {d + w,     d,  d,  h};
            case BACK -> new int[]      {d + w + d, d,  w,  h};
            case TOP -> new int[]       {d,         0,  w,  d};
            case BOTTOM -> new int[]    {d + w,     0,  w,  d};
        };

        // adjust X and Y with texture part start-coords
        bounds[0] += part.getStartX();
        bounds[1] += part.getStartY();

        // multiple bounds with scale factor
        for (int i = 0; i < bounds.length; i++)
            bounds[i] *= scale;

        return bounds;
    }

}
