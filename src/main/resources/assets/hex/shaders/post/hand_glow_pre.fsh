#version 330

#moj_import <hex:fx.glsl>

#moj_import <minecraft:globals.glsl>

uniform sampler2D InSampler;
uniform sampler2D ColorSampler;

layout(std140) uniform OutlineConfig {
    vec4 Params;
    vec4 GradientTop;
    vec4 GradientBottom;
    vec4 Params2;
};

in vec2 texCoord;

out vec4 fragColor;

const float GRADIENT_SCALE = 2.0;
const float GRADIENT_SCROLL = 0.25;
const float DAY_SECONDS = 1200.0;

vec3 gradient() {
    vec2 dir = vec2(Params2.x, Params2.y);
    if (dot(dir, dir) < 0.5) dir = vec2(0.0, 1.0);
    float p = dot(texCoord - 0.5, dir) * GRADIENT_SCALE
            + GameTime * DAY_SECONDS * Params2.z * GRADIENT_SCROLL;
    if (Params.w > 0.5) {
        return hue2rgb(fract(p));
    }
    float w = abs(fract(p * 0.5) * 2.0 - 1.0);
    return mix(GradientTop.rgb, GradientBottom.rgb, w);
}

void main() {
    vec4 s = texture(InSampler, texCoord);
    vec3 col;
    if (Params.x > 0.5) {
        col = boost(texture(ColorSampler, texCoord).rgb, Params.y);
    } else if (Params.z > 0.5) {
        col = gradient();
    } else {
        col = s.a > 0.0008 ? s.rgb / s.a : vec3(0.0);
    }
    fragColor = vec4(col, s.a);
}
