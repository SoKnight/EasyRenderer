package org.easylauncher.renderer.engine.graph.mesh;

import org.joml.Vector3f;

public class TexturedCuboidMesh extends MeshBase {

    private static final float[] VERTEX_MASKS = new float[] {
            -1F, +1F, -1F,  // v0
            +1F, +1F, -1F,  // v1
            +1F, -1F, -1F,  // v2
            -1F, -1F, -1F,  // v3
            -1F, +1F, +1F,  // v4
            +1F, +1F, +1F,  // v5
            +1F, -1F, +1F,  // v6
            -1F, -1F, +1F,  // v7
    };

    private static final int[] FACES_INDICES = new int[] {
            0, 3, 7,    0, 7, 4,    // left
            6, 5, 4,    6, 4, 7,    // front
            2, 1, 5,    2, 5, 6,    // right
            0, 1, 2,    0, 2, 3,    // back
            1, 0, 4,    1, 4, 5,    // top
            3, 2, 6,    3, 6, 7,    // bottom
    };

    private static final int[] INDICES = new int[FACES_INDICES.length];

    protected static final int VERTICES_COUNT = INDICES.length;

    static {
        for (int i = 0; i < FACES_INDICES.length; i++) {
            INDICES[i] = i;
        }
    }

    public TexturedCuboidMesh(int width, int height, int depth, float enlarge, float[] textureCoords) {
        float[] vertexCoords = computeVertexCoords(width, height, depth, enlarge);

        float[] positions = new float[VERTICES_COUNT * 3];
        float[] normals = new float[VERTICES_COUNT * 3];
        computePositionsAndNormals(positions, normals, vertexCoords);

        initVAO(positions, normals, textureCoords, INDICES);
    }

    private static void computePositionsAndNormals(float[] positions, float[] normals, float[] vertexCoords) {
        Vector3f[] points = new Vector3f[3];
        for (int i = 0; i < points.length; i++)
            points[i] = new Vector3f();

        Vector3f side2to3, side2to1;
        int positionIndex = 0, normalIndex = 0;

        for (int faceVertexIndex = 0; faceVertexIndex < FACES_INDICES.length; faceVertexIndex += 3) {
            // gather triangle points coords
            for (int i = 0; i < 3; i++) {
                int vertexCoordStart = FACES_INDICES[faceVertexIndex + i] * 3;
                points[i].x = vertexCoords[vertexCoordStart];
                points[i].y = vertexCoords[vertexCoordStart + 1];
                points[i].z = vertexCoords[vertexCoordStart + 2];

                // write position for every triangle point
                positions[positionIndex++] = points[i].x;
                positions[positionIndex++] = points[i].y;
                positions[positionIndex++] = points[i].z;
            }

            // compute normal
            side2to3 = points[2].sub(points[1]); // side p2 -> p3
            side2to1 = points[0].sub(points[1]); // side p2 -> p1
            Vector3f normal = side2to3.cross(side2to1).normalize();

            // construct arrays
            for (int i = 0; i < 3; i++) {
                // write normal for every triangle point
                normals[normalIndex++] = normal.x;
                normals[normalIndex++] = normal.y;
                normals[normalIndex++] = normal.z;
            }
        }
    }

    private static float[] computeVertexCoords(int width, int height, int depth, float enlarge) {
        float[] vertices = new float[8 * 3];

        float w = width / 2F + enlarge;
        float h = height / 2F + enlarge;
        float d = depth / 2F + enlarge;
        float[] dims = new float[] { w, h, d };

        for (int i = 0; i < VERTEX_MASKS.length; i += 3) {
            for (int j = 0; j < 3; j++) {
                int vertexIndex = i + j;
                vertices[vertexIndex] = VERTEX_MASKS[vertexIndex] * dims[j];
            }
        }

        return vertices;
    }

}
