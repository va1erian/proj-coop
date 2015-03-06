#version 330

in vec3 positionWorldspace;
in vec3 normalCameraspace;
in vec3 eyeDirCameraspace;
in vec3 lightDirCameraspace;

uniform vec3 diffuse;
uniform vec3 lightPosWorldspace;

out vec3 outputColor;

void main(){
    vec3 lightColor = vec3(0.7,0.7,0.7);
    float lightPow = 50.0f;

   // Material properties
    vec3 matDiffuseColor = diffuse.rgb;
    vec3 matAmbientColor = vec3(0.2,0.2,0.2) * matDiffuseColor;
    vec3 matSpecularColor = vec3(0.4,0.4,0.4);

    // Distance to the light
    float distance = length( lightPosWorldspace - positionWorldspace );

    // Normal of the computed fragment, in camera space
    vec3 n = normalize( normalCameraspace );
    //vec3 n = normalize(vec3(0.7, 1.0, 0.5));
    // Direction of the light (from the fragment to the light)
    vec3 l = normalize( lightDirCameraspace );
    // Cosine of the angle between the normal and the light direction,
    // clamped above 0
    // - light is at the vertical of the triangle -> 1
    // - light is perpendicular to the triangle -> 0
    // - light is behind the triangle -> 0
    float cosTheta = clamp( dot( n,l ), 0,1 );
    // Eye vector (towards the camera)
    vec3 E = normalize(eyeDirCameraspace);
    // Direction in which the triangle reflects the light
    vec3 R = reflect(-l,n);
    // Cosine of the angle between the Eye vector and the Reflect vector,
    // clamped to 0
    // - Looking into the reflection -> 1
    // - Looking elsewhere -> < 1
    float cosAlpha = clamp( dot( E,R ), 0,1 );

    
    outputColor =
    // Ambient : simulates indirect lighting
    matAmbientColor +
    // Diffuse : "color" of the object
    matDiffuseColor * lightColor * lightPow * cosTheta / (distance*distance)
    // Specular : reflective highlight, like a mirror
    + matSpecularColor * lightColor * lightPow * pow(cosAlpha,5) / (distance*distance);

}