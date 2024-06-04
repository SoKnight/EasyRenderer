package org.easylauncher.framework.gamerender.game.skin;

import lombok.Getter;
import org.easylauncher.framework.gamerender.engine.object.Mesh;

@Getter
public final class SkinPartMesh extends Mesh {

    private static final TexturedFace[] FACES = new TexturedFace[] {
            new TexturedFace(2, 1, new float[] {-1F, 0F, 0F}, (w, h, d) -> 0, (w, h, d) -> d, 4, 6, 1, 0),
            new TexturedFace(0, 1, new float[] {0F, 0F, 1F}, (w, h, d) -> d, (w, h, d) -> d,0, 1, 2, 3),
            new TexturedFace(2, 1, new float[] {1F, 0F, 0F}, (w, h, d) -> d + w, (w, h, d) -> d,3, 2, 7, 5),
            new TexturedFace(0, 1, new float[] {0F, 0F, -1F}, (w, h, d) -> d + w + d, (w, h, d) -> d,5, 7, 6, 4),
            new TexturedFace(0, 2, new float[] {0F, 1F, 0F}, (w, h, d) -> d, (w, h, d) -> 0,4, 0, 3, 5),
            new TexturedFace(0, 2, new float[] {0F, -1F, 0F}, (w, h, d) -> d + w, (w, h, d) -> 0,6, 1, 2, 7)
    };

    private final boolean transparencyAllowed;

    private SkinPartMesh(float[] positions, float[] normals, float[] textureCoords, int[] indices, boolean transparencyAllowed) {
        super(positions, normals, textureCoords, indices);
        this.transparencyAllowed = transparencyAllowed;
    }

    public static SkinPartMesh create(
            int width, int height, int depth,
            int startX, int startY,
            float extra,
            boolean transparencyAllowed
    ) {
        float[] positions = new float[6 * 4 * 3];
        float[] normals = new float[6 * 3 * 2];
        float[] textureCoords = new float[6 * 4 * 2];
        int[] indices = new int[6 * 6];

        float u = 0.015625F;
        float v = 0.015625F;

        float wu = width * u;
        float wv = width * v;
        float hu = height * u;
        float hv = height * v;
        float du = depth * u;
        float dv = depth * v;

        float su = startX * u;
        float sv = startY * v;

        int positionIndex = 0;
        int normalIndex = 0;
        int textureCoordIndex = 0;
        int indexIndex = 0;

        float[] vertexPositions = constructPositions(width, height, depth, extra);
        int indexOffset = 0;

        for (TexturedFace face : FACES) {
            float faceU = su + face.uCoord().compute(wu, hu, du);
            float faceV = sv + face.vCoord().compute(wv, hv, dv);

            float[] duTypes = new float[] { wu, hu, du };
            float[] dvTypes = new float[] { wv, hv, dv };

            for (int vertexId : face.vertexIds()) {
                int offset = vertexId * 3;
                positions[positionIndex++] = vertexPositions[offset];
                positions[positionIndex++] = vertexPositions[offset + 1];
                positions[positionIndex++] = vertexPositions[offset + 2];
            }

            for (TexturedVertex vertex : face.vertices()) {
                textureCoords[textureCoordIndex++] = vertex.uCoord().compute(faceU, duTypes[face.duType()]);
                textureCoords[textureCoordIndex++] = vertex.vCoord().compute(faceV, dvTypes[face.dvType()]);
            }

            for (float normal : face.normal()) {
                normals[normalIndex++] = normal;
            }

            for (float normal : face.normal()) {
                normals[normalIndex++] = normal;
            }

            indices[indexIndex++] = indexOffset;
            indices[indexIndex++] = indexOffset + 1;
            indices[indexIndex++] = indexOffset + 3;
            indices[indexIndex++] = indexOffset + 3;
            indices[indexIndex++] = indexOffset + 1;
            indices[indexIndex++] = indexOffset + 2;
            indexOffset += 4;
        }

        return new SkinPartMesh(positions, normals, textureCoords, indices, transparencyAllowed);
    }

    private static float[] constructPositions(float width, float height, float depth, float extra) {
        float dx = width / 2F + extra;
        float dy = height / 2F + extra;
        float dz = depth / 2F + extra;

        return new float[] {
                -dx,  dy,  dz, // 0
                -dx, -dy,  dz, // 1
                 dx, -dy,  dz, // 2
                 dx,  dy,  dz, // 3
                -dx,  dy, -dz, // 4
                 dx,  dy, -dz, // 5
                -dx, -dy, -dz, // 6
                 dx, -dy, -dz, // 7
        };
    }

    public record TexturedFace(int duType, int dvType, int[] vertexIds, float[] normal, int[] indices, FaceUVCoord uCoord, FaceUVCoord vCoord, TexturedVertex... vertices) {

        public TexturedFace(int duType, int dvType, float[] normal, FaceUVCoord uCoord, FaceUVCoord vCoord, int... vertexIds) {
            this(
                    duType,
                    dvType,
                    vertexIds,
                    normal,
                    new int[] { vertexIds[0], vertexIds[1], vertexIds[3], vertexIds[3], vertexIds[1], vertexIds[2] },
                    uCoord,
                    vCoord,
                    TexturedVertex.topLeft(vertexIds[0]),
                    TexturedVertex.bottomLeft(vertexIds[1]),
                    TexturedVertex.bottomRight(vertexIds[2]),
                    TexturedVertex.topRight(vertexIds[3])
            );
        }

    }

    public record TexturedVertex(int vertexId, VertexUVCoord uCoord, VertexUVCoord vCoord) {

        public static TexturedVertex topLeft(int vertexId) {
            return new TexturedVertex(vertexId, (u0, du) -> u0 + 0.001f, (v0, dv) -> v0 + 0.001f);
        }

        public static TexturedVertex topRight(int vertexId) {
            return new TexturedVertex(vertexId, (u0, du) -> u0 + du - 0.001f, (v0, dv) -> v0 + 0.001f);
        }

        public static TexturedVertex bottomLeft(int vertexId) {
            return new TexturedVertex(vertexId, (u0, du) -> u0 + 0.001f, (v0, dv) -> v0 + dv - 0.001f);
        }

        public static TexturedVertex bottomRight(int vertexId) {
            return new TexturedVertex(vertexId, (u0, du) -> u0 + du - 0.001f, (v0, dv) -> v0 + dv - 0.001f);
        }

    }

    @FunctionalInterface
    public interface VertexUVCoord {

        float compute(float start, float delta);

    }

    @FunctionalInterface
    public interface FaceUVCoord {

        float compute(float w, float h, float d);

    }

}
