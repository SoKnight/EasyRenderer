package org.easylauncher.framework.gamerender.engine.object;

import lombok.Getter;
import org.easylauncher.framework.gamerender.engine.exception.texture.TextureLoadException;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

@Getter
public class Texture {

    private final int textureId;

    public Texture(InputStream resource) throws TextureLoadException {
        ByteBuffer nativeBytes = null;
        try {
            try {
                int available = resource.available();
                nativeBytes = MemoryUtil.memAlloc(available);

                byte[] buffer = new byte[256];
                for (int i = 0; i < available; i += 256) {
                    int read = resource.read(buffer);
                    if (read == -1)
                        break;

                    nativeBytes.put(i, buffer, 0, read);
                }
            } catch (IOException ex) {
                throw new TextureLoadException(ex);
            }

            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer pWidth = stack.mallocInt(1);
                IntBuffer pHeight = stack.mallocInt(1);
                IntBuffer channels = stack.mallocInt(1);

                ByteBuffer buffer = stbi_load_from_memory(nativeBytes, pWidth, pHeight, channels, 4);
                if (buffer == null)
                    throw new TextureLoadException(stbi_failure_reason());

                int width = pWidth.get();
                int height = pHeight.get();

                this.textureId = generateTexture(width, height, buffer);
                stbi_image_free(buffer);
            }
        } finally {
            if (nativeBytes != null) {
                MemoryUtil.memFree(nativeBytes);
            }
        }
    }

    public void cleanup() {
        glDeleteTextures(textureId);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    private static int generateTexture(int width, int height, ByteBuffer buffer) {
        int textureId = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, textureId);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(GL_TEXTURE_2D);

        return textureId;
    }

}
