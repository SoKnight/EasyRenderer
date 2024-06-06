package org.easylauncher.renderer.engine.graph.mesh;

import org.easylauncher.renderer.engine.Uniforms;
import org.easylauncher.renderer.engine.exception.shader.ShaderGLException;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class MeshBase implements Mesh {

    private final int vaoId;
    private final int[] vboIds;
    private int verticesCount;

    protected MeshBase() {
        this.vaoId = glGenVertexArrays();
        this.vboIds = new int[4];
    }

    public MeshBase(float[] positions, float[] normals, float[] textureCoords, int[] indices) {
        this();
        initVAO(positions, normals, textureCoords, indices);
    }

    protected final void initVAO(float[] positions, float[] normals, float[] textureCoords, int[] indices) {
        this.verticesCount = indices.length;

        try (MemoryStack stack = stackPush()) {
            glBindVertexArray(vaoId);

            // positions VBO
            int vboId = createVBO(0);
            FloatBuffer positionsBuffer = stack.callocFloat(positions.length);
            positionsBuffer.put(0, positions);
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // normals VBO
            vboId = createVBO(1);
            FloatBuffer normalsBuffer = stack.callocFloat(normals.length);
            normalsBuffer.put(0, normals);
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

            // texture coords VBO
            vboId = createVBO(2);
            FloatBuffer textureCoordsBuffer = stack.callocFloat(textureCoords.length);
            textureCoordsBuffer.put(0, textureCoords);
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, textureCoordsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(2);
            glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);

            // indices VBO
            vboId = createVBO(3);
            IntBuffer indicesBuffer = stack.callocInt(indices.length);
            indicesBuffer.put(0, indices);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }
    }

    @Override
    public void bind() {
        glBindVertexArray(vaoId);
    }

    @Override
    public void cleanup() {
        for (int vboId : vboIds)
            glDeleteBuffers(vboId);

        glDeleteVertexArrays(vaoId);
    }

    @Override
    public void draw(Uniforms uniforms) throws ShaderGLException {
        glDrawElements(GL_TRIANGLES, verticesCount, GL_UNSIGNED_INT, 0);
    }

    private int createVBO(int index) {
        int vboId = glGenBuffers();
        this.vboIds[index] = vboId;
        return vboId;
    }

}
