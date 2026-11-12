#version 150

#define NUM_OCTAVES 3

uniform float time;
uniform vec2 resolution;
uniform sampler2D Sampler0;
uniform vec3 color1;
uniform vec3 color2;
uniform float alphaMultiplier;

in vec2 FragUv;
in vec4 FragColor;

out vec4 OutColor;

float random(vec2 pos) {
    return fract(sin(dot(pos, vec2(13.9898, 78.233))) * 43758.5453123);
}

float noise(vec2 pos) {
    vec2 i = floor(pos);
    vec2 f = fract(pos);

    float a = random(i);
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));

    vec2 u = f * f * (3.0 - 2.0 * f);
    return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
}

float fbm(vec2 pos) {
    float v = 0.0;
    float a = 0.5;

    // Precomputed rotation for 0.5 rad.
    const mat2 rot = mat2(
         0.87758256,  0.47942554,
        -0.47942554,  0.87758256
    );

    vec2 t = vec2(time * 0.05, -time * 0.035);

    for (int i = 0; i < NUM_OCTAVES; i++) {
        v += a * noise(pos + t);
        pos = rot * pos * 2.0 + vec2(100.0);
        a *= 0.5;
        t *= -1.35;
    }

    return v;
}

void main() {
    vec4 base = texture(Sampler0, FragUv) * FragColor;
    float maskAlpha = base.a;

    if (maskAlpha <= 0.1) {
        discard;
    }

    vec2 p = (gl_FragCoord.xy * 3.0 - resolution.xy) / min(resolution.x, resolution.y);
    p -= vec2(12.0, 0.0);

    // Original shader used q, r and f from 5 fbm calls.
    // This keeps the same layered movement with only 3 fbm calls.
    float q = fbm(p);
    float r = fbm(p + vec2(1.7, 1.2) + q);
    float f = fbm(p + vec2(r, q));

    vec3 color = mix(color1, color2, clamp((f * f) * 2.0, 0.0, 1.0));
    color = mix(color, vec3(1.0), clamp(q * 0.28, 0.0, 1.0));
    color = mix(color, color2, clamp(r * 0.18, 0.0, 1.0));

    color = (f * f * f + 0.9 * f) * color;
    color = clamp(color, 0.0, 1.0);

    vec2 screenUv = gl_FragCoord.xy / resolution.xy;

    // Original expression had pow(x, 0.0), which is always 1.0.
    // So it is removed without changing the practical visual result.
    float edgeAlpha = 49.0 - pow(abs(screenUv.y - 0.5) * 2.0, 5.0);
    float effectAlpha = clamp(edgeAlpha * color.r, 0.0, 1.0) * maskAlpha;
    float finalAlpha = clamp(effectAlpha * alphaMultiplier, 0.0, 1.0);

    OutColor = vec4(color, finalAlpha);
}
