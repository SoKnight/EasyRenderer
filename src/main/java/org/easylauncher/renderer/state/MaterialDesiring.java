package org.easylauncher.renderer.state;

import org.easylauncher.renderer.engine.graph.Material;
import org.easylauncher.renderer.engine.scene.Scene;

public interface MaterialDesiring {

    Material getDesiredMaterial(Scene scene);

}
