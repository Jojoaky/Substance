#version 150

#define PI 3.14159
#define TAU (2.0 * PI)

uniform sampler2D DiffuseSampler;
uniform vec2 OutSize;

in vec2 texCoord;
out vec4 fragColor;

uniform float ShaderGameTime;

uniform float Intensity;
uniform float Speed;

void main() {
    float s = Intensity * 0.003;

    float t = ShaderGameTime * TAU * Speed;

    float waveX = sin(texCoord.y * 10.0 + t);
    float waveY = cos(texCoord.x * 10.0 + t);

    vec2 distortion = vec2(waveX, waveY);

    vec2 redOffset  = distortion * (s * 0.5);
    vec2 blueOffset = distortion * (s * -0.5);

    vec4 base = texture(DiffuseSampler, texCoord);

    float r = texture(DiffuseSampler, texCoord + redOffset).r;
    float g = base.g;
    float b = texture(DiffuseSampler, texCoord + blueOffset).b;

    fragColor = vec4(r, g, b, base.a);
}