#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec2 textureCoord;

out vec3 outPosition;
out vec3 outNormal;
out vec2 outTextureCoord;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 sceneRotationMatrix;
uniform mat4 modelTransformMatrix;
uniform mat4 modelRotationMatrix;

void main() {
    mat4 mvMatrix = viewMatrix * sceneRotationMatrix * modelTransformMatrix * modelRotationMatrix;
    vec4 mvPosition = mvMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * mvPosition;

    outPosition = mvPosition.xyz;
    outNormal = normalize(mvMatrix * vec4(normal, 0.0)).xyz;
    outTextureCoord = textureCoord;
}