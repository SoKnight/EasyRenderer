package org.easylauncher.renderer.engine.exception.shader;

import org.easylauncher.renderer.engine.shader.ShaderModule;

public class ShaderLoadException extends ShaderExceptionBase {

    public ShaderLoadException(ShaderModule module, String cause) {
        super("Unable to load shader module: %s (%s)".formatted(module, cause), null);
    }

    public ShaderLoadException(ShaderModule module, Throwable cause) {
        super("Unable to load shader module: %s".formatted(module), cause);
    }

}
