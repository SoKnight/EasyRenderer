package org.easylauncher.renderer.game.skin;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.easylauncher.renderer.engine.graph.texture.Texture;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class SkinTextureWrapper implements Texture {

    private final Texture texture;
    private final boolean thinArms;

    public static SkinTextureWrapper wrap(Texture texture, boolean thinArms) {
        return new SkinTextureWrapper(texture, thinArms);
    }

    @Override
    public int getId() {
        return texture.getId();
    }

    @Override
    public void bind() {
        texture.bind();
    }

    @Override
    public void cleanup() {
        texture.cleanup();
    }

}
