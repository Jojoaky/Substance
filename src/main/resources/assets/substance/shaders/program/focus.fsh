#version 150

#define PI 3.14159
#define TAU (2.0 * PI)

uniform sampler2D DiffuseSampler;
uniform vec2 OutSize;

in vec2 texCoord;
out vec4 fragColor;

const float size = 0.5;
const float strength = 0.5;
const vec4 vignetteColor = vec4(0.0, 0.0, 0.0, 1.0);

void main() {
    vec2 uv = (texCoord*2.0) - vec2(1.0, 1.0);
    float len = length(uv);

    float v = (len - size) / (sqrt(2.0) - size) * strength;
    v = max(v, 0.0);


    vec4 color = vec4(v, 0.0, 0.0, 1.0);

    vec4 pixelColor = texture(DiffuseSampler, texCoord);

    fragColor = mix(pixelColor, vignetteColor, v);
}