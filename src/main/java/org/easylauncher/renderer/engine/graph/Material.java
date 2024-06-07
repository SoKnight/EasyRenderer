package org.easylauncher.renderer.engine.graph;

import lombok.Getter;
import org.easylauncher.renderer.engine.graph.texture.Texture;

@Getter
public final class Material {

    private Texture texture;

    public boolean hasTexture() {
        return texture != null;
    }

    public Texture updateTexture(Texture texture) {
        int currentTextureId = this.texture != null ? this.texture.getId() : -1;
        int newTextureId = texture != null ? texture.getId() : -1;

        if (currentTextureId != newTextureId) {
            Texture previous = this.texture;
            this.texture = texture;
            return previous;
        } else {
            return null;
        }
    }

}
