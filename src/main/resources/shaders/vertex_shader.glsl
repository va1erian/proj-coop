#version 330

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 normal;

uniform mat4 MVP;
uniform mat4 V;
uniform mat4 M;
uniform vec3 lightPosWorldspace;

out vec3 positionWorldspace;
out vec3 normalCameraspace;
out vec3 eyeDirCameraspace;
out vec3 lightDirCameraspace;

void main(){    
    positionWorldspace = (M * vec4(position, 1)).xyz;

    vec3 vertexPosCameraspace = ( V * M * vec4(position,1)).xyz;
    eyeDirCameraspace = vec3(0,0,0) - vertexPosCameraspace;

    vec3 lightPosCameraspace = ( V * vec4(lightPosWorldspace,1)).xyz;
    lightDirCameraspace = lightPosCameraspace + eyeDirCameraspace;

    //normalCameraspace = ( V * M * vec4(normal,0)).xyz;
    normalCameraspace = ( V * M * vec4(normal, 0)).xyz;

    gl_Position = MVP * vec4(position, 1.0);
}