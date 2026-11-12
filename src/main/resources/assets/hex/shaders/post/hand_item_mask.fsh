#version 330

#moj_import <hex:fx.glsl>

uniform sampler2D InSampler;
uniform sampler2D MaskSampler;

layout(std140) uniform MaskConfig {
    vec4 TintTop;
    vec4 TintBottom;
    vec4 Params;
};

in vec2 texCoord;

out vec4 fragColor;

void main() {
    float mask = texture(MaskSampler, texCoord).a;
    if (mask < 0.0015) discard;

    vec3 col = texture(InSampler, texCoord).rgb;
    fragColor = vec4(handTint(col, texCoord, TintTop, TintBottom, Params.x, Params.yz), mask);
}
