#version 150

#define PI 3.14159
#define TAU 2 * PI

uniform sampler2D DiffuseSampler;
uniform vec2 OutSize;

in vec2 texCoord;
out vec4 fragColor;

uniform float ShaderGameTime;

uniform float Intensity;
uniform float Speed;
uniform float IntensityMultiplier;

void main() {
    float s = Intensity * IntensityMultiplier * 0.002;
    float t = ShaderGameTime * TAU * Speed;

    float offsetX = sin(texCoord.y * 10.0 + t) * s;
    float offsetY = cos(texCoord.x * 10.0 + t) * s;

    vec2 distortedCoord = texCoord + vec2(offsetX, offsetY);

    vec4 color = texture(DiffuseSampler, distortedCoord);

    fragColor = color;
}