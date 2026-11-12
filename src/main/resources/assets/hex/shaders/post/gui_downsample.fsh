#version 330

uniform sampler2D InSampler;

in vec2 texCoord;

out vec4 fragColor;

const int GROUPS = max(1, BLUR_SCALE / 2);
const float INV_TAPS = 1.0 / float(GROUPS * GROUPS);

void main() {
    vec2 ts = 1.0 / vec2(textureSize(InSampler, 0));
    vec2 origin = texCoord / ts - vec2(float(BLUR_SCALE) * 0.5);

    vec3 col = vec3(0.0);
    for (int j = 0; j < GROUPS; j++) {
        for (int i = 0; i < GROUPS; i++) {
            vec2 tap = origin + vec2(float(i) * 2.0 + 1.0, float(j) * 2.0 + 1.0);
            col += texture(InSampler, tap * ts).rgb;
        }
    }

    fragColor = vec4(col * INV_TAPS, 1.0);
}
