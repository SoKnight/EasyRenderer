#version 330

const int MAX_POINT_LIGHTS = 3;
const float SPECULAR_POWER = 10;

in vec3 outPosition;
in vec3 outNormal;
in vec2 outTextureCoord;

out vec4 fragmentColor;

struct Attenuation
{
    float constant;
    float exponent;
    float linear;
};

struct Material
{
    vec4 ambientColor;
    vec4 diffuseColor;
    vec4 specularColor;
    float reflectance;
};

struct AmbientLight
{
    vec3 color;
    float intensity;
};

struct PointLight {
    Attenuation attenuation;
    vec3 color;
    vec3 position;
    float intensity;
};

struct DirectionalLight
{
    vec3 color;
    vec3 direction;
    float intensity;
};

uniform bool transparencyAllowed;
uniform sampler2D textureSampler;
uniform Material material;
uniform AmbientLight ambientLight;
uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform DirectionalLight directionalLight;

vec4 calcAmbient(AmbientLight ambientLight, vec4 ambient) {
    return vec4(ambientLight.intensity * ambientLight.color, 1) * ambient;
}

vec4 calcLightColor(vec4 diffuse, vec4 specular, vec3 lightColor, float lightIntensity, vec3 position, vec3 lightDirection, vec3 normal) {
    // diffuse light
    float diffuseFactor = max(dot(normal, lightDirection), 0.0);
    vec4 diffuseColor = diffuse * vec4(lightColor, 1.0) * lightIntensity * diffuseFactor;

    // specular light
    vec3 cameraDirection = normalize(-position);
    vec3 invertedLightDirection = -lightDirection;
    vec3 reflectedLight = normalize(reflect(invertedLightDirection, normal));
    float specularFactor = pow(max(dot(cameraDirection, reflectedLight), 0.0), SPECULAR_POWER);
    vec4 specularColor = specular * lightIntensity * specularFactor * material.reflectance * vec4(lightColor, 1.0);

    return (diffuseColor + specularColor);
}

vec4 calcPointLight(vec4 diffuse, vec4 specular, PointLight light, vec3 position, vec3 normal) {
    vec3 lightDirection = light.position - position;
    vec3 normalizedLightDirection = normalize(lightDirection);
    vec4 lightColor = calcLightColor(diffuse, specular, light.color, light.intensity, position, normalizedLightDirection, normal);

    // apply attenuation
    Attenuation att = light.attenuation;
    float distance = length(lightDirection);
    float attenuationInv = att.constant + att.linear * distance + att.exponent * distance * distance;
    return lightColor / attenuationInv;
}

vec4 calcDirectionalLight(vec4 diffuse, vec4 specular, DirectionalLight light, vec3 position, vec3 normal) {
    return calcLightColor(diffuse, specular, light.color, light.intensity, position, normalize(light.direction), normal);
}

void main() {
    vec4 textureColor = texture(textureSampler, outTextureCoord);
    if (textureColor.a == 0.0) {
        discard;
    } else {
        if (textureColor.a != 1.0 && !transparencyAllowed) {
            textureColor = vec4(0, 0, 0, 1);
        }

        vec4 ambient = calcAmbient(ambientLight, textureColor + material.ambientColor);
        vec4 diffuse = textureColor + material.diffuseColor;
        vec4 specular = textureColor + material.specularColor;

        vec4 diffuseSpecularComp = calcDirectionalLight(diffuse, specular, directionalLight, outPosition, outNormal);
        for (int i = 0; i < MAX_POINT_LIGHTS; i++) {
            if (pointLights[i].intensity > 0) {
                calcPointLight(diffuse, specular, pointLights[i], outPosition, outNormal);
            }
        }

        fragmentColor = ambient + diffuseSpecularComp;
    }
}