#version 330

const int MAX_POINT_LIGHTS = 3;
const float SPECULAR_POWER = 10;

in vec3 outPosition;
in vec3 outNormal;
in vec2 outTextureCoord;

out vec4 fragmentColor;

struct AmbientLight
{
    vec3 color;
    float intensity;
};

struct DirectedLight
{
    vec3 color;
    vec3 direction;
    float intensity;
};

uniform bool transparencyAllowed;
uniform sampler2D textureSampler;
uniform mat4 viewMatrix;
uniform AmbientLight ambientLight;
uniform DirectedLight directedLight;

void main() {
    vec4 color = texture(textureSampler, outTextureCoord);
    if (color.a == 0.0 && transparencyAllowed) {
        discard;
    } else {
        if (color.a != 1.0 && !transparencyAllowed) {
            color = vec4(0, 0, 0, 1);
        }
    }

    vec3 ambient = ambientLight.color * ambientLight.intensity;

    vec4 lightDirection = vec4(directedLight.direction, 0);
    vec4 vmLightDirection = viewMatrix * lightDirection;
    float diff = max(dot(normalize(outNormal), normalize(vmLightDirection.xyz)), 0.0);
    diff = clamp(diff, 0.0, 1.0);

    vec3 directed = diff * directedLight.color * directedLight.intensity;

    vec4 lighting = vec4(ambient + directed, 1.0);
    fragmentColor = lighting * color;
}