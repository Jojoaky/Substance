#version 150

#define PI 3.14159
#define TAU (2.0 * PI)

uniform sampler2D SamplerA;
uniform sampler2D SamplerB;

uniform vec2 OutSize;

uniform float ShaderGameTime;

uniform float Frequency;
uniform float Strength;
uniform float Bias;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 a = texture(SamplerA, texCoord);
    vec4 b = texture(SamplerB, texCoord);

    float phase = ShaderGameTime * TAU * Frequency;
    float range = 1.0 - abs(clamp(Bias, 0.0, 9.999));

    float wave = (sin(phase) + 1.0) * 0.5;

    wave = wave / range;
    wave = wave * Strength + Bias;
    wave = clamp(wave, 0.0, 1.0);

    fragColor = mix(a, b, wave);
}