package org.easylauncher.renderer.game.skin;

import lombok.Getter;
import org.easylauncher.renderer.engine.Uniforms;
import org.easylauncher.renderer.engine.exception.shader.ShaderGLException;
import org.easylauncher.renderer.game.GameMeshBase;

@Getter
public final class SkinPartMesh extends GameMeshBase {

    private static final float U = 0.015625F;
    private static final float V = 0.015625F;

    private static final float UV_OFFSET = 0.001F;
    private static final int TL = 3, TR = 2, BL = 1, BR = 0;
    private static final int[][] FACE_OFFSET_MASKS = new int[][] {
            new int[] { TL, BL, BR, TL, BR, TR },
            new int[] { BR, TR, TL, BR, TL, BL },
            new int[] { BR, TR, TL, BR, TL, BL },
            new int[] { TR, TL, BL, TR, BL, BR },
            new int[] { TR, TL, BL, TR, BL, BR },
            new int[] { TL, TR, BR, TL, BR, BL },
    };

    private final boolean transparencyAllowed;

    public SkinPartMesh(int width, int height, int depth, float enlarge, int startX, int startY, boolean transparencyAllowed) {
        super(width, height, depth, enlarge, computeTextureCoords(width, height, depth, startX, startY));
        this.transparencyAllowed = transparencyAllowed;
    }

    @Override
    public void draw(Uniforms uniforms) throws ShaderGLException {
        uniforms.put("transparencyAllowed", transparencyAllowed);
        super.draw(uniforms);
    }

    private static float[] computeTextureCoords(int width, int height, int depth, int startX, int startY) {
        float[] textureCoords = computeTextureCoords(width, height, depth, startX, startY, U, V);

        int textureCoordIndex = 0;
        for (int faceIndex = 0; faceIndex < 6; faceIndex++) {
            for (int uvIndex = 0; uvIndex < 6; uvIndex++) {
                int mask = FACE_OFFSET_MASKS[faceIndex][uvIndex];
                boolean a = (mask & 0b01) == 0b01;
                boolean b = (mask & 0b10) == 0b10;
                textureCoords[textureCoordIndex++] += a ? UV_OFFSET : -UV_OFFSET;
                textureCoords[textureCoordIndex++] += b ? UV_OFFSET : -UV_OFFSET;
            }
        }

        return textureCoords;
    }

}
