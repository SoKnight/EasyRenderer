package org.easylauncher.renderer.game.skin.resolver;

import org.easylauncher.renderer.engine.exception.texture.TextureLoadException;
import org.easylauncher.renderer.engine.graph.texture.Texture;
import org.easylauncher.renderer.engine.scene.Scene;

import java.util.UUID;

@FunctionalInterface
public interface DefaultSkinResolver {

    DefaultSkinResolver ALWAYS_STEVE = (scene, playerUUID) -> scene.getSteveSkinTexture();

    DefaultSkinResolver PLAYER_UUID_BASED = (scene, playerUUID) -> playerUUID == null || playerUUID.hashCode() % 2 == 0
            ? scene.getSteveSkinTexture()
            : scene.getAlexSkinTexture();

    Texture resolveSkinTexture(Scene scene, UUID playerUUID) throws TextureLoadException;

}
