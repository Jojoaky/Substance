#version 150

#define PI 3.14159
#define TAU (2.0 * PI)

uniform sampler2D DiffuseSampler;
uniform vec2 OutSize;

in vec2 texCoord;
out vec4 fragColor;

uniform float Intensity;

float rand(vec2 co) {
    return fract(sin(dot(co.xy, vec2(12.9898, 78.233))) * 43758.5453);
}

vec3 warmGrade(vec3 color, float intensity) {
    vec3 warm = mix(vec3(1), vec3(1.3, 1.1, 0.8), intensity);
    vec3 cool = mix(vec3(1), vec3(0.8, 1.00, 1.1), intensity);

    float luminance = dot(color, vec3(0.299, 0.587, 0.114));

    vec3 graded = mix(cool, warm, luminance);
    return color * graded;
}

void main() {
    float intensity = Intensity;

    vec2 uv = texCoord;

    vec3 col = texture(DiffuseSampler, uv).rgb;

    col = warmGrade(col, intensity);
    col += vec3(0.05, 0.04, 0.02) * intensity;

    fragColor = vec4(col, 1.0);
}