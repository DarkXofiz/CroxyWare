#version 330

#moj_import <minecraft:dynamictransforms.glsl>

in float sphericalVertexDistance;
in float cylindricalVertexDistance;
in vec4 vertexColor;
noperspective in float edge;

out vec4 fragColor;

void main() {
    vec4 color = vertexColor * ColorModulator;

    float dist = 1.0 - abs(edge);
    float aa = fwidth(edge);
    float coverage = aa > 0.0 ? clamp(dist / aa, 0.0, 1.0) : 1.0;

    fragColor = vec4(color.rgb, color.a * coverage);
}
