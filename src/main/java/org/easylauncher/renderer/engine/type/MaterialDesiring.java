package org.easylauncher.renderer.engine.type;

import org.easylauncher.renderer.engine.graph.Material;
import org.easylauncher.renderer.engine.scene.Scene;

public interface MaterialDesiring {

    Material getDesiredMaterial(Scene scene);

}
