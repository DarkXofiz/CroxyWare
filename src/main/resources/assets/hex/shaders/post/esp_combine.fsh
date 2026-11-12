#version 330

#moj_import <hex:fx.glsl>

uniform sampler2D InSampler;
uniform sampler2D MaskSampler;

layout(std140) uniform OutlineConfig {
    float Soft;
    float Thickness;
    float Gate;
    float TargetId;
};

layout(std140) uniform PrepConfig {
    float Textured;
    float Saturation;
    float PrepTargetId;
    float PrepPad;
};

in vec2 texCoord;

out vec4 fragColor;

int idOf(vec4 c) {
    return int(c.a * 255.0 + 0.5) & 0x07;
}

void main() {
    vec4 g = texture(InSampler, texCoord);
    int tid = int(TargetId + 0.5);

    vec4 mc = texture(MaskSampler, texCoord);
    if (mc.a > 0.5 && idOf(mc) == tid) {
        fragColor = vec4(0.0);
        return;
    }
    if (Gate > 0.5 && g.a < 0.002) {
        fragColor = vec4(0.0);
        return;
    }

    vec2 px = 1.0 / vec2(textureSize(MaskSampler, 0));
    float outlineA = 0.0;
    vec3 outlineCol = vec3(0.0);
    float t2 = Thickness * Thickness;
    int t = int(min(Thickness, 8.0) + 0.5);

    for (int dy = -t; dy <= t; dy++) {
        for (int dx = -t; dx <= t; dx++) {
            if (float(dx * dx + dy * dy) <= t2) {
                vec4 s = texture(MaskSampler, texCoord + vec2(float(dx), float(dy)) * px);
                if (s.a > 0.5 && idOf(s) == tid) {
                    outlineA = 1.0;
                    outlineCol = s.rgb;
                    break;
                }
            }
        }
        if (outlineA > 0.5) break;
    }

    if (outlineA > 0.5 && Textured > 0.5) {
        outlineCol = boost(outlineCol, Saturation);
    }

    if (Soft < 0.5) {
        fragColor = vec4(outlineCol, outlineA);
        return;
    }

    float glowA = clamp(g.a, 0.0, 1.0);
    vec3 col = outlineA > 0.5 ? outlineCol : g.rgb;
    fragColor = vec4(col, max(outlineA, glowA));
}
