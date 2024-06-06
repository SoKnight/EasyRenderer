package org.easylauncher.renderer.engine.graph.texture;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static org.lwjgl.opengl.GL11.*;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public final class TextureBase implements Texture {

    private final int textureId;

    @Override
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    @Override
    public void cleanup() {
        glDeleteTextures(textureId);
    }

}
