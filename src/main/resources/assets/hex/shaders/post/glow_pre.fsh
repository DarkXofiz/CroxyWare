#version 330

#moj_import <hex:fx.glsl>

uniform sampler2D InSampler;

layout(std140) uniform PrepConfig {
    float Textured;
    float Saturation;
    float TargetId;
    float PrepPad;
};

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 s = texture(InSampler, texCoord);
    if (s.a < 0.5 || (int(s.a * 255.0 + 0.5) & 0x07) != int(TargetId + 0.5)) {
        fragColor = vec4(0.0);
        return;
    }
    vec3 col = Textured > 0.5 ? boost(s.rgb, Saturation) : s.rgb;
    fragColor = vec4(col, 1.0);
}
