#version 330

#moj_import <minecraft:globals.glsl>
#moj_import <hex:jump_circle.glsl>

const float CHROMA = 0.35;
const float TINT = 0.45;
const float SCREEN_WARP = 0.015;
const float SPREAD_BASE = 0.35;

uniform sampler2D Sampler0;

in vec3 localPos;
in vec2 surface;
in vec4 centreClip;

out vec4 fragColor;

vec3 tap(vec2 uv) {
    return texture(Sampler0, clamp(uv, vec2(0.001), vec2(0.999))).rgb;
}

vec3 smear(vec2 uv, vec2 offset, vec2 spread) {
    return (tap(uv + offset - spread) + tap(uv + offset + spread)) * 0.5;
}

void main() {
    JumpSample s = jumpSample(localPos, surface);
    float fall = smoothstep(0.0, 0.6, s.coverage);
    if (fall < 0.004 || centreClip.w <= 0.0) {
        discard;
    }

    vec2 uv = gl_FragCoord.xy / ScreenSize;
    vec2 centreUv = centreClip.xy / centreClip.w * 0.5 + 0.5;
    vec2 radial = uv - centreUv;
    vec2 push = dot(radial, radial) > 1.0e-10 ? normalize(radial) : vec2(0.0);

    vec2 warp = push * fall * smoothstep(0.0, 0.18, s.frac) * Wave.z * SCREEN_WARP;
    vec2 offset = warp * s.ripple;
    vec2 spread = warp * (SPREAD_BASE + (1.0 - SPREAD_BASE) * abs(s.ripple)) * 0.5;

    vec3 scene = vec3(
            smear(uv, offset * (1.0 + CHROMA), spread).r,
            smear(uv, offset, spread).g,
            smear(uv, offset * (1.0 - CHROMA), spread).b);

    vec4 tint = jumpTint(s.dir);
    float bend = min(1.0, length(offset) * 120.0);
    vec3 tinted = mix(scene, scene * (vec3(0.5) + tint.rgb), TINT * fall * bend);

    fragColor = vec4(tinted, fall);
}
