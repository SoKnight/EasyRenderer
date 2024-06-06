package org.easylauncher.renderer.engine.shader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public enum ShaderType {

    VERTEX("vert", GL_VERTEX_SHADER),
    FRAGMENT("frag", GL_FRAGMENT_SHADER),
    ;

    private final String extension;
    private final int value;

}
