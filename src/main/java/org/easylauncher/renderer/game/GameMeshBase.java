package org.easylauncher.renderer.game;

import org.easylauncher.renderer.engine.graph.mesh.TexturedCuboidMesh;

public class GameMeshBase extends TexturedCuboidMesh {

    public GameMeshBase(int width, int height, int depth, float enlarge, float[] textureCoords) {
        super(width, height, depth, enlarge, textureCoords);
    }

    protected static float[] computeTextureCoords(int width, int height, int depth, int u0, int v0, float u, float v) {
        float wu = width * u;
        float hv = height * v;
        float du = depth * u;
        float dv = depth * v;

        float u1 = u0 * u, u2 = u1 + du, u3 = u2 + wu, u4 = u3 + du, u5 = u4 + wu;
        float w1 = u2, w2 = w1 + wu, w3 = w2 + wu;
        float v1 = v0 * v, v2 = v1 + dv, v3 = v2 + hv;

        return new float[] {
                // left
                u1, v2, u1, v3, u2, v3, // 0 3 7
                u1, v2, u2, v3, u2, v2, // 0 7 4
                // front
                u3, v3, u3, v2, u2, v2, // 6 5 4
                u3, v3, u2, v2, u2, v3, // 6 4 7
                // right
                u4, v3, u4, v2, u3, v2, // 2 1 5
                u4, v3, u3, v2, u3, v3, // 2 5 6
                // back
                u5, v2, u4, v2, u4, v3, // 0 1 2
                u5, v2, u4, v3, u5, v3, // 0 2 3
                // top
                w2, v1, w1, v1, w1, v2, // 1 0 4
                w2, v1, w1, v2, w2, v2, // 1 4 5
                // bottom
                w2, v1, w3, v1, w3, v2, // 3 2 6
                w2, v1, w3, v2, w2, v2, // 3 6 7
        };
    }

}
