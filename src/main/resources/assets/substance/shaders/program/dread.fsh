#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 OutSize;
uniform float ShaderGameTime;
uniform float Intensity;
uniform float NauseatingIntensity;
in vec2 texCoord;
out vec4 fragColor;

void main() {
    float strength = clamp(Intensity, 0.0, 1.5);
    float motion = clamp(NauseatingIntensity, 0.0, 1.5);
    float t = ShaderGameTime;
    vec2 centered = texCoord - 0.5;
    float edge = smoothstep(0.15, 0.68, length(centered));
    vec2 inset = 0.5 / OutSize;
    vec2 uv = clamp(texCoord + centered * sin(t * 0.8) * 0.003 * edge * motion,
                    inset, 1.0 - inset);
    vec4 color = texture(DiffuseSampler, uv);
    float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    color.rgb = mix(color.rgb, vec3(gray) * vec3(0.88, 0.94, 1.0), min(0.8, strength * 0.65));
    float shadow = 0.5 + 0.5 * sin(texCoord.x * 13.0 + sin(texCoord.y * 9.0 + t * 0.3) + t * 0.2);
    color.rgb *= 1.0 - edge * (0.25 + 0.14 * shadow) * strength;
    fragColor = color;
}
