#version 330

#moj_import <hex:fx.glsl>

uniform sampler2D MaskSampler;

layout(std140) uniform GlossConfig {
    vec4 ColorA;
    vec4 ColorB;
    vec4 Params;
    vec4 Params2;
};

in vec2 texCoord;

out vec4 fragColor;

void main() {
    float mask = texture(MaskSampler, texCoord).a;
    if (mask < 0.0015) discard;

    vec4 band = glossBand(texCoord * Params.z, Params2.x * Params.y, Params.w,
            ColorA.rgb, ColorB.rgb, Params2.y > 0.5);

    fragColor = vec4(band.rgb, band.a * Params.x * mask);
}
