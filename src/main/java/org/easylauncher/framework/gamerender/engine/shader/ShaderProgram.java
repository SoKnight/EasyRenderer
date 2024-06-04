package org.easylauncher.framework.gamerender.engine.shader;

import lombok.Getter;
import org.easylauncher.framework.gamerender.engine.exception.shader.ShaderGLException;
import org.easylauncher.framework.gamerender.engine.exception.shader.ShaderLoadException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL20.*;

@Getter
public final class ShaderProgram {

    private final int programId;

    public ShaderProgram(ShaderModule... modules) throws ShaderGLException, ShaderLoadException {
        this.programId = glCreateProgram();
        if (programId == 0)
            throw new ShaderGLException("Failed to create shader program");

        int[] shaderHandles = new int[modules.length];
        int index = 0;

        for (ShaderModule module : modules) {
            String resourcePath = "/shaders/%s.%s".formatted(module.name(), module.type().extension());
            try (InputStream resource = getClass().getResourceAsStream(resourcePath)) {
                if (resource == null)
                    throw new ShaderLoadException(module, "resource not found");

                String shaderCode = new String(resource.readAllBytes(), StandardCharsets.UTF_8);
                int shaderHandle = createShader(shaderCode, module.type());
                shaderHandles[index++] = shaderHandle;
            } catch (IOException ex) {
                throw new ShaderLoadException(module, ex);
            }
        }

        link(shaderHandles);
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();

        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }

    private int createShader(String shaderCode, ShaderType type) throws ShaderGLException {
        int shaderId = glCreateShader(type.value());
        if (shaderId == 0)
            throw new ShaderGLException("Failed to create shader with type '%s'".formatted(type));

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0)
            throw new ShaderGLException("Failed to compile shader: %s".formatted(glGetShaderInfoLog(shaderId, 1024)));

        glAttachShader(programId, shaderId);
        return shaderId;
    }

    private void link(int[] shaderHandles) throws ShaderGLException {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0)
            throw new ShaderGLException("Failed to link shader program: %s".formatted(glGetProgramInfoLog(programId, 1024)));

        for (int shaderHandle : shaderHandles) {
            glDetachShader(programId, shaderHandle);
            glDeleteShader(shaderHandle);
        }
    }

    public void validate() throws ShaderGLException {
        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            throw new ShaderGLException("Failed to validate shader program: %s".formatted(glGetProgramInfoLog(programId, 1024)));
        }
    }

}
