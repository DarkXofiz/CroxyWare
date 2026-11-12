#version 330

uniform sampler2D InSampler;
uniform sampler2D MaskSampler;

layout(std140) uniform GuiCombine {
    float Soft;
    float Thickness;
    float PadA;
    float PadB;
};

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 self = texture(MaskSampler, texCoord);
    if (self.a > 0.5) {
        fragColor = vec4(0.0);
        return;
    }

    vec2 px = 1.0 / vec2(textureSize(MaskSampler, 0));
    float t2 = Thickness * Thickness;
    int t = int(min(Thickness, 8.0) + 0.5);

    float outlineA = 0.0;
    vec3 outlineCol = vec3(0.0);
    for (int dy = -t; dy <= t; dy++) {
        for (int dx = -t; dx <= t; dx++) {
            if (float(dx * dx + dy * dy) <= t2) {
                vec4 s = texture(MaskSampler, texCoord + vec2(float(dx), float(dy)) * px);
                if (s.a > 0.5) {
                    outlineA = 1.0;
                    outlineCol = s.rgb;
                    break;
                }
            }
        }
        if (outlineA > 0.5) break;
    }

    vec3 col;
    float a;
    if (Soft < 0.5) {
        col = outlineCol;
        a = outlineA;
    } else {
        vec4 g = texture(InSampler, texCoord);
        a = max(outlineA, g.a);
        col = outlineA > 0.5 ? outlineCol : g.rgb;
    }

    fragColor = vec4(col * a, a);
}
