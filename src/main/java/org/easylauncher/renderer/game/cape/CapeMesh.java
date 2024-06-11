package org.easylauncher.renderer.game.cape;

import org.easylauncher.renderer.game.GameMeshBase;

import static org.easylauncher.renderer.game.cape.CapeModel.*;

public final class CapeMesh extends GameMeshBase {

    private static final int START_X = 0;
    private static final int START_Y = 0;

    public CapeMesh() {
        super(WIDTH, HEIGHT, DEPTH, 0F, computeTextureCoords());
    }

    private static float[] computeTextureCoords() {
        return computeTextureCoords(WIDTH, HEIGHT, DEPTH, START_X, START_Y, UV64, UV32, false);
    }

}
