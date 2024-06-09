package org.easylauncher.renderer.engine.graph.texture;

import org.easylauncher.renderer.engine.exception.texture.TextureLoadException;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public final class TextureLoader {

    public static Texture loadFrom(URL url) throws TextureLoadException {
        try (InputStream stream = url.openStream()) {
            return loadFrom(stream);
        } catch (IOException ex) {
            throw new TextureLoadException(ex);
        }
    }

    public static Texture loadFrom(Path path) throws TextureLoadException {
        try {
            long size = Files.size(path);
            try (InputStream stream = Files.newInputStream(path)) {
                return loadFrom(stream, Math.toIntExact(size));
            }
        } catch (IOException ex) {
            throw new TextureLoadException(ex);
        }
    }

    public static Texture loadFrom(InputStream stream) throws TextureLoadException {
        try {
            return loadFrom(stream, stream.available());
        } catch (IOException ex) {
            throw new TextureLoadException(ex);
        }
    }

    public static Texture loadFrom(InputStream stream, int size) throws TextureLoadException {
        ByteBuffer nativeBytes = null;

        try {
            nativeBytes = MemoryUtil.memAlloc(size);

            byte[] buffer = new byte[256];
            for (int i = 0; i < size; i += 256) {
                int read = stream.read(buffer);
                if (read == -1)
                    break;

                nativeBytes.put(i, buffer, 0, read);
            }

            return loadFromMemory(nativeBytes);
        } catch (IOException ex) {
            throw new TextureLoadException(ex);
        } finally {
            if (nativeBytes != null) {
                MemoryUtil.memFree(nativeBytes);
            }
        }
    }

    public static Texture loadFromMemory(ByteBuffer nativeBytes) throws TextureLoadException {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer buffer = stbi_load_from_memory(nativeBytes, pWidth, pHeight, channels, 4);
            if (buffer == null)
                throw new TextureLoadException(stbi_failure_reason());

            int width = pWidth.get();
            int height = pHeight.get();

            int textureId = generateTexture(width, height, buffer);
            stbi_image_free(buffer);
            return new TextureBase(textureId);
        }
    }

    public static Texture loadFromPixelBuffer(int width, int height, byte[] pixelBuffer) {
        ByteBuffer buffer = null;

        try {
            buffer = MemoryUtil.memAlloc(pixelBuffer.length);
            buffer.put(pixelBuffer);
            buffer.flip();

            int textureId = generateTexture(width, height, buffer);
            return new TextureBase(textureId);
        } finally {
            if (buffer != null) {
                MemoryUtil.memFree(buffer);
            }
        }
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
