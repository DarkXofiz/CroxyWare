#version 330

#moj_import <hex:fx.glsl>

uniform sampler2D InSampler;
uniform sampler2D GlowSampler;
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
    vec4 glow = texture(GlowSampler, texCoord);

    float itemAlpha = mask < 0.0015 ? 0.0 : mask;
    float glowAlpha = glow.a < 0.0015 ? 0.0 : glow.a * (1.0 - mask);

    float outAlpha = itemAlpha + glowAlpha - itemAlpha * glowAlpha;
    if (outAlpha <= 0.0) {
        discard;
    }

    vec3 item = vec3(0.0);
    if (itemAlpha > 0.0) {
        item = handTint(texture(InSampler, texCoord).rgb, texCoord, TintTop, TintBottom, Params.x, Params.yz);
    }

    vec3 premultiplied = glow.rgb * glowAlpha + item * itemAlpha * (1.0 - glowAlpha);
    fragColor = vec4(premultiplied / outAlpha, outAlpha);
}
