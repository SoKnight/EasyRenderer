package org.easylauncher.renderer.engine.scene;

import org.easylauncher.renderer.engine.light.AmbientLight;
import org.easylauncher.renderer.engine.light.DirectedLight;
import org.joml.Vector3f;

public record SceneLights(AmbientLight ambientLight, DirectedLight directedLight) {

    public SceneLights() {
        this(
                new AmbientLight(new Vector3f(1F, 1F, 1F), 0.7F),
                new DirectedLight(new Vector3f(1F, 1F, 1F), new Vector3f(0.678F, 0.284F, 0.678F), 0.3F)
        );
    }

}
