#version 150

#define PI 3.14159
#define TAU (2.0 * PI)

uniform sampler2D DiffuseSampler;
uniform sampler2D PrevSampler;

uniform vec2 OutSize;

uniform float FrameTime;

in vec2 texCoord;
out vec4 fragColor;

uniform float Intensity;
uniform vec3 Retention; // % left after 1s
uniform float EdgeAttenuation;

void main() {
    vec4 current = texture(DiffuseSampler, texCoord);
    vec4 previous = texture(PrevSampler, texCoord);

    // reduce effect at center of screen
    vec2 centeredUV = (texCoord - vec2(0.5)) * 2.0;
    float edgeMask = length(centeredUV) / sqrt(2);
    edgeMask = clamp(edgeMask, 0.0, 1.0);
    edgeMask = mix(1.0, edgeMask, EdgeAttenuation);

    vec3 weight = pow(Retention, vec3(FrameTime / Intensity));
    weight *= edgeMask;
    weight = clamp(weight, 0.0, 1.0);

    vec3 color = mix(current.rgb, previous.rgb, weight);

    fragColor = vec4(color.rgb, current.a);
}