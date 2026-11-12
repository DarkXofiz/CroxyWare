#version 330

uniform sampler2D InSampler;

layout(std140) uniform BlurConfig {
    vec2 BlurDir;
    float Radius;
    float Boost;
};

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec2 px = (1.0 / vec2(textureSize(InSampler, 0))) * BlurDir;
    float sigma = max(1.0, Radius * 0.5);
    float inv2s2 = 1.0 / (2.0 * sigma * sigma);

    vec4 c = texture(InSampler, texCoord);
    float total = 1.0;
    float a = c.a;
    vec3 col = c.rgb * c.a;

    for (float r = 1.0; r <= Radius; r += 2.0) {
        float w1 = exp(-(r * r) * inv2s2);
        float r2 = r + 1.0;
        float w2 = (r2 <= Radius) ? exp(-(r2 * r2) * inv2s2) : 0.0;
        float wsum = w1 + w2;
        vec2 o = px * ((r * w1 + r2 * w2) / wsum);
        vec4 s1 = texture(InSampler, texCoord + o);
        vec4 s2 = texture(InSampler, texCoord - o);
        a += (s1.a + s2.a) * wsum;
        col += (s1.rgb * s1.a + s2.rgb * s2.a) * wsum;
        total += 2.0 * wsum;
    }

    a /= total;
    col /= total;
    vec3 outCol = a > 0.0008 ? col / a : vec3(0.0);
    float outA = clamp(a * Boost, 0.0, 1.0);
    if (BlurDir.y > 0.5 && outA > 0.0) {
        float noise = fract(sin(dot(gl_FragCoord.xy, vec2(12.9898, 78.233))) * 43758.5453);
        outA = clamp(outA + (noise - 0.5) * (2.5 / 255.0), 0.0, 1.0);
    }
    fragColor = vec4(outCol, outA);
}
