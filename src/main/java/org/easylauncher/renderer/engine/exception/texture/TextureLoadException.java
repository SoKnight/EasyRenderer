package org.easylauncher.renderer.engine.exception.texture;

public class TextureLoadException extends TextureExceptionBase {

    public TextureLoadException(String cause) {
        super("Unable to load texture (%s)".formatted(cause), null);
    }

    public TextureLoadException(Throwable cause) {
        super("Unable to load texture", cause);
    }

}
