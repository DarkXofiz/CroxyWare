#version 150

#moj_import <hex:common.glsl>

in vec2 FragCoord;
in vec2 TexCoord;
in vec4 FragColor;
in vec2 FragSize;
in vec4 FragRadius;
in float FragSmoothness;
in float FragBlurRadius;

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

const float FULL_BLUR_RANGE = 8.0;

out vec4 OutColor;

vec2 clampUv(vec2 uv, vec2 texel) {
    return clamp(uv, texel * 0.5, vec2(1.0) - texel * 0.5);
}

void main() {
    vec2 texel = 1.0 / vec2(textureSize(Sampler0, 0));
    vec2 uv = clampUv(TexCoord, texel);
    float strength = clamp(FragBlurRadius / FULL_BLUR_RANGE, 0.0, 1.0);
    vec3 sampled = mix(texture(Sampler1, uv).rgb, texture(Sampler0, uv).rgb, strength);

    vec4 color = vec4(sampled, 1.0) * FragColor;
    float dist = rdistBox(FragSize, FragCoord, FragRadius);
    color.a *= rfill(dist, FragSmoothness, fwidth(dist));

    if (color.a == 0.0) discard;

    OutColor = color;
}
