#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 OutSize;
uniform float ShaderGameTime;
uniform float Intensity;
uniform float NauseatingIntensity;

in vec2 texCoord;
out vec4 fragColor;

const float TAU = 6.2831853;

float hash(float value) {
    return fract(sin(value * 127.1) * 43758.5453);
}

float speedLines(vec2 radial, float time, float count, float seed) {
    float radius = length(radial);
    float angle = atan(radial.y, radial.x + 0.00001);
    // Each layer sweeps around the view as its individual streaks fly outward.
    angle += 0.065 * sin(time * 0.83 + seed)
           + 0.035 * sin(angle * 3.0 - time * 1.17 + seed);
    float lane = (angle / TAU + 0.5) * count;
    float id = mod(floor(lane), count) + seed;
    float random = hash(id);
    float phase = fract(time * (0.65 + 0.45 * random) + hash(id + 19.0));
    float head = mix(0.12, 1.65, phase);
    float lineLength = mix(0.24, 0.58, hash(id + 43.0));
    float tail = head - lineLength;
    float along = smoothstep(tail, tail + lineLength * 0.65, radius)
                * (1.0 - smoothstep(head - 0.08, head, radius));
    float width = mix(0.07, 0.19, random) * (0.35 + 0.65 * along);
    float across = abs(fract(lane) - 0.5);
    float aa = max(fwidth(lane), 0.001);
    float line = 1.0 - smoothstep(width, width + aa, across);
    float lifetime = smoothstep(0.0, 0.12, phase)
                   * (1.0 - smoothstep(0.84, 1.0, phase));
    return line * along * lifetime * mix(0.65, 1.0, random);
}

void main() {
    vec4 source = texture(DiffuseSampler, texCoord);
    // MobEffectShader sends level^0.6: undo that compression for a visible
    // level 1, prominent levels 2-3, and strong levels 4+, with a bounded cap.
    float level = pow(max(Intensity, 0.0), 1.0 / 0.6);
    float strength = 1.0 - exp(-level * 0.32);
    float motionScale = clamp(NauseatingIntensity / max(Intensity, 0.0001), 0.0, 1.0);
    float motion = strength * motionScale;
    if (motion == 0.0) {
        fragColor = source;
        return;
    }

    vec2 size = max(OutSize, vec2(1.0));
    vec2 centered = texCoord - vec2(0.5);
    vec2 radial = centered * size / min(size.x, size.y);
    float radius = length(radial);
    float edge = smoothstep(0.18, 0.48, radius);
    vec2 halfTexel = 0.5 / size;

    // A short radial trail leaves the crosshair and central view undistorted.
    vec3 trail = source.rgb;
    for (int i = 1; i <= 4; i++) {
        vec2 sampleUv = texCoord - centered * (float(i) * 0.009 * edge * motion);
        trail += texture(DiffuseSampler, clamp(sampleUv, halfTexel, 1.0 - halfTexel)).rgb;
    }
    vec3 color = mix(source.rgb, trail / 5.0, edge * motion * 0.65);

    // Two independent sets avoid a fixed spoke pattern. Neutral light and
    // dark accents keep the streaks readable against both terrain and sky.
    float lines = speedLines(radial, ShaderGameTime, 56.0, 0.0);
    float fineLines = speedLines(radial, ShaderGameTime * 1.13, 83.0, 71.0);
    float streaks = max(lines, fineLines * 0.7) * edge * motion;
    vec2 shadowRadial = mat2(0.99995, -0.01, 0.01, 0.99995) * radial;
    float shadow = speedLines(shadowRadial, ShaderGameTime, 56.0, 0.0);
    color *= 1.0 - shadow * edge * motion * 0.30;
    color = mix(color, vec3(1.0), streaks * 0.95);

    fragColor = vec4(clamp(color, 0.0, 1.0), source.a);
}
