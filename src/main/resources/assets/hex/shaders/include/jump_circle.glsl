#version 330

const float TAU = 6.28318530718;
const float SEGMENTS = 48.0;
const float LIFT = 0.01;
const float CLIMB_FADE_START = 2.0;
const float CLIMB_FADE_END = 2.8;
const float WALL_GLOW = 0.8;
const float UV_WARP = 0.05;
const float RIPPLE_FREQ = 16.0;
const float RIPPLE_SPEED = 6.0;

layout(std140) uniform JumpCircle {
    vec4 Centre;
    vec4 World;
    vec4 FadeFirst;
    vec4 FadeSecond;
    vec4 Wave;
};

float jumpWaveNoise(vec3 p, float t) {
    float a = sin(p.x * 2.3 + p.z * 1.7 + p.y * 1.1 + t * 0.9);
    float b = sin(p.x * 1.3 - p.z * 2.9 + p.y * 2.6 - t * 1.3);
    float c = sin(p.x * 3.7 + p.z * 0.9 - p.y * 1.8 + t * 0.6);
    return (a + b + c) / 3.0;
}

struct JumpSample {
    float coverage;
    float mask;
    float frac;
    float ripple;
    vec2 dir;
    vec2 texCoord;
};

JumpSample jumpSample(vec3 local, vec2 surface) {
    float radius = Centre.w;
    float alpha = World.w;
    float kind = surface.y;

    float dist = length(local.xz);
    float frac = dist / radius;
    float shapeFrac = min(frac, 1.0);
    vec2 dir = dist > 1.0e-4 ? local.xz / dist : vec2(0.0);
    float rise = local.y - LIFT * (1.0 - kind);

    float coverage;
    if (kind < 0.5) {
        float topFade = 1.0 - smoothstep(CLIMB_FADE_START, CLIMB_FADE_END, rise);
        float ring = (0.5 + 0.5 * shapeFrac) * (1.0 - smoothstep(0.72, 1.0, frac));
        coverage = alpha * surface.x * topFade * ring;
    } else {
        vec3 world = World.xyz + vec3(local.x, rise, local.z);
        float t = Wave.y;
        float n = jumpWaveNoise(world, t);
        float riseN = rise + 0.45 * n;
        float fracN = frac + 0.12 * n;
        float maxRise = min(2.6, max(0.0, radius - dist) * 1.6 + 1.0);
        float riseFade = maxRise > 0.05 ? 1.0 - smoothstep(maxRise * 0.55, maxRise, riseN) : 0.0;
        float glow = WALL_GLOW * smoothstep(0.0, 0.3, rise) * riseFade * (1.0 - smoothstep(0.85, 1.05, fracN));
        float mottle = 0.82 + 0.18 * jumpWaveNoise(vec3(world.x * 1.9 + 7.3, world.y * 1.6, world.z * 2.1 - 3.1), t * 1.4);
        coverage = alpha * glow * mottle;
    }

    JumpSample result;
    result.coverage = clamp(coverage, 0.0, 1.0);
    result.mask = smoothstep(0.02, 0.28, coverage);
    result.frac = frac;
    result.ripple = sin(frac * RIPPLE_FREQ - Wave.y * RIPPLE_SPEED);
    result.dir = dir;
    result.texCoord = vec2(0.5) + dir * shapeFrac * 0.5
            + dir * result.ripple * result.mask * Wave.z * UV_WARP;
    return result;
}

vec4 jumpTint(vec2 dir) {
    float segment = mod(floor(atan(dir.y, dir.x) / TAU * SEGMENTS + 0.5), SEGMENTS);
    float phase = mod(Wave.x + floor(segment * 360.0 / SEGMENTS), 360.0);
    phase = phase >= 180.0 ? 360.0 - phase : phase;
    return mix(FadeFirst, FadeSecond, phase / 180.0);
}
