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
    mat4 worldMatrix = sceneRotationMatrix * modelTransformMatrix * modelRotationMatrix;

    mat4 mwMatrix = viewMatrix * worldMatrix;
    vec4 mwPosition = mwMatrix * vec4(position, 1);
    outPosition = mwPosition.xyz;
    gl_Position = projectionMatrix * mwPosition;

    mat4 nwMatrix = transpose(inverse(worldMatrix));
    vec4 nwNormal = nwMatrix * vec4(normal, 0);
    outNormal = nwNormal.xyz;

    outTextureCoord = textureCoord;
}