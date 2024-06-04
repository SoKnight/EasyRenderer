package org.easylauncher.framework.gamerender.engine.shader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public enum ShaderType {

    VERTEX("vert", GL_VERTEX_SHADER),
    GEOMETRY("geom", GL_GEOMETRY_SHADER),
    FRAGMENT("frag", GL_FRAGMENT_SHADER),
    ;

    private final String extension;
    private final int value;

}
