package org.easylauncher.framework.gamerender.engine.scene;

import lombok.Getter;
import org.easylauncher.framework.gamerender.engine.light.AmbientLight;
import org.easylauncher.framework.gamerender.engine.light.DirectionalLight;
import org.easylauncher.framework.gamerender.engine.light.PointLight;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class SceneLights {

    private final AmbientLight ambientLight;
    private final List<PointLight> pointLights;
    private final DirectionalLight directionalLight;

    public SceneLights() {
        this.ambientLight = new AmbientLight();
        this.pointLights = new ArrayList<>();
        this.directionalLight = new DirectionalLight(
                new Vector3f(1F, 1F, 1F),
                new Vector3f(0.678F, 0.284F, 0.678F),
                0.3F
        );
    }

    public void addPointLight(PointLight pointLight) {
        this.pointLights.add(pointLight);
    }

}
