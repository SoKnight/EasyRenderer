package org.easylauncher.framework.gamerender.engine.light;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Attenuation {

    private float constant;
    private float exponent;
    private float linear;

}
