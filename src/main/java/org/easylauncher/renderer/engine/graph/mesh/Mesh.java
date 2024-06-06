package org.easylauncher.renderer.engine.graph.mesh;

import org.easylauncher.renderer.engine.exception.shader.ShaderGLException;
import org.easylauncher.renderer.engine.type.Bindable;
import org.easylauncher.renderer.engine.type.Cleanable;
import org.easylauncher.renderer.engine.Uniforms;

public interface Mesh extends Bindable, Cleanable {

    void draw(Uniforms uniforms) throws ShaderGLException;

}
