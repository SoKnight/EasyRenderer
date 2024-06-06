package org.easylauncher.renderer.engine.graph.texture;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.lwjgl.opengl.GL11.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public final class TextureBase implements Texture {

    private final int id;

    @Override
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    @Override
    public void cleanup() {
        glDeleteTextures(id);
    }

}
