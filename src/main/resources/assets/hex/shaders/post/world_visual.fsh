#version 330

uniform sampler2D InSampler;

in vec2 texCoord;

layout(std140) uniform VisualConfig {
    vec4 Tint;
    vec4 Misc;
};

out vec4 fragColor;

const vec3 LUMA = vec3(0.299, 0.587, 0.114);

void main() {
    vec4 scene = texture(InSampler, texCoord);
    vec3 col = scene.rgb;

    float luma = dot(col, LUMA);
    col = mix(vec3(luma), col, Misc.x);

    float toned = dot(col, LUMA);
    float tintLuma = dot(Tint.rgb, LUMA);
    vec3 recolor = (tintLuma > 0.001) ? Tint.rgb * (toned / tintLuma) : vec3(toned);
    col = mix(col, recolor, Tint.a);

    fragColor = vec4(col, scene.a);
}
