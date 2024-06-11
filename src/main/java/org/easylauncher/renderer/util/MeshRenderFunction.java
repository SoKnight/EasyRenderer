package org.easylauncher.renderer.util;

import org.easylauncher.renderer.engine.exception.shader.ShaderGLException;
import org.easylauncher.renderer.engine.graph.mesh.Mesh;

@FunctionalInterface
public interface MeshRenderFunction {

    void render(Mesh mesh) throws ShaderGLException;

}
