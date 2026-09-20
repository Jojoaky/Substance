#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 OutSize;
uniform float Intensity;

in vec2 texCoord;
out vec4 fragColor;

const vec4 vignetteColor = vec4(0.2, 0.2, 0.2, 1.0);
const float maxDarkness = 0.9;
const float strength = 0.6;

void main() {
    vec2 uv = texCoord - vec2(0.5);
    float dist = dot(uv, uv);

    float vignette = clamp(dist * 2.0 * Intensity * strength, 0.0, 1.0);
    vignette = pow(vignette, 2.0);

    vignette *= maxDarkness;

    vec4 pixelColor = texture(DiffuseSampler, texCoord);

    fragColor = mix(pixelColor, vignetteColor, vignette);
}