#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 OutSize;
uniform float ShaderGameTime;
uniform float Intensity;
uniform float NauseatingIntensity;
in vec2 texCoord;
out vec4 fragColor;

void main() {
    float strength = clamp(Intensity, 0.0, 2.0);
    float motion = clamp(NauseatingIntensity, 0.0, 2.0);
    float t = ShaderGameTime;
    vec2 inset = 0.5 / OutSize;
    vec2 uv = texCoord + vec2(sin(texCoord.y * 19.0 + t * 0.7),
                              cos(texCoord.x * 16.0 - t * 0.5)) * 0.0018 * motion;
    uv = clamp(uv, inset, 1.0 - inset);
    vec4 color = texture(DiffuseSampler, uv);
    float split = 0.0008 * motion;
    color.r = texture(DiffuseSampler, clamp(uv + vec2(split, 0.0), inset, 1.0 - inset)).r;
    color.b = texture(DiffuseSampler, clamp(uv - vec2(split, 0.0), inset, 1.0 - inset)).b;
    float floaters = 0.0;
    for (int i = 0; i < 7; i++) {
        float n = float(i);
        vec2 center = vec2(0.5 + 0.43 * sin(n * 5.7 + t * 0.023),
                           0.5 + 0.42 * cos(n * 3.1 + t * 0.031));
        vec2 delta = (texCoord - center) * vec2(OutSize.x / OutSize.y, 1.0);
        float ring = abs(length(delta) - (0.009 + 0.003 * sin(n)));
        floaters += (1.0 - smoothstep(0.001, 0.005, ring)) * 0.035;
    }
    color.rgb *= 1.0 - floaters * strength;
    fragColor = color;
}
