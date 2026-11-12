#version 330

uniform sampler2D InSampler;

in vec2 texCoord;

out vec4 fragColor;

#ifdef BLUR_VERTICAL
const vec2 DIR = vec2(0.0, 1.0);
#else
const vec2 DIR = vec2(1.0, 0.0);
#endif

const float SIGMA = max(1.0, BLUR_RADIUS * 0.5);
const float INV_2SIGMA2 = 1.0 / (2.0 * SIGMA * SIGMA);

void main() {
    vec2 px = (1.0 / vec2(textureSize(InSampler, 0))) * DIR;

    vec3 col = texture(InSampler, texCoord).rgb;
    float total = 1.0;

    for (float r = 1.0; r <= BLUR_RADIUS; r += 2.0) {
        float w1 = exp(-(r * r) * INV_2SIGMA2);
        float r2 = r + 1.0;
        float w2 = (r2 <= BLUR_RADIUS) ? exp(-(r2 * r2) * INV_2SIGMA2) : 0.0;
        float wsum = w1 + w2;
        vec2 o = px * ((r * w1 + r2 * w2) / wsum);
        col += (texture(InSampler, texCoord + o).rgb + texture(InSampler, texCoord - o).rgb) * wsum;
        total += 2.0 * wsum;
    }

    fragColor = vec4(col / total, 1.0);
}
