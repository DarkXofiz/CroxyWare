#version 330

#moj_import <hex:fx.glsl>

uniform sampler2D InSampler;

layout(std140) uniform GuiGlowPrep {
    float Textured;
    float Saturation;
    float R;
    float G;
    float B;
    float PadA;
    float PadB;
    float PadC;
};

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 s = texture(InSampler, texCoord);
    if (s.a < 0.1) {
        fragColor = vec4(0.0);
        return;
    }
    vec3 straight = s.rgb / max(s.a, 0.001);
    vec3 col = Textured > 0.5 ? boost(straight, Saturation) : vec3(R, G, B);
    fragColor = vec4(col, 1.0);
}
