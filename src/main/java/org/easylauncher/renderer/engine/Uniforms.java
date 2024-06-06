package org.easylauncher.renderer.engine;

import org.easylauncher.renderer.engine.exception.shader.ShaderGLException;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public final class Uniforms {

    private final int programId;
    private final Map<String, Integer> uniforms;

    public Uniforms(int programId) {
        this.programId = programId;
        this.uniforms = new HashMap<>();
    }

    public void create(String name) throws ShaderGLException {
        int uniformLocation = glGetUniformLocation(programId, name);
        if (uniformLocation < 0)
            throw new ShaderGLException("Uniform '%s' not found in shader program %d".formatted(name, programId));

        uniforms.put(name, uniformLocation);
    }

    private int get(String name) throws ShaderGLException {
        Integer location = uniforms.get(name);
        if (location == null)
            throw new ShaderGLException("Uniform '%s' not found".formatted(name));

        return location;
    }

    public void put(String name, boolean value) throws ShaderGLException {
        glUniform1i(get(name), value ? 1 : 0);
    }

    public void put(String name, int value) throws ShaderGLException {
        glUniform1i(get(name), value);
    }

    public void put(String name, float value) throws ShaderGLException {
        glUniform1f(get(name), value);
    }

    public void put(String name, Vector3f value) throws ShaderGLException {
        glUniform3f(get(name), value.x, value.y, value.z);
    }

    public void put(String name, Vector4f value) throws ShaderGLException {
        glUniform4f(get(name), value.x, value.y, value.z, value.w);
    }

    public void put(String name, Matrix4f value) throws ShaderGLException {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(get(name), false, value.get(stack.mallocFloat(16)));
        }
    }

}
