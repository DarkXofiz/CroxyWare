#version 150

#moj_import <hex:common.glsl>

in vec2 FragCoord;
in vec2 TexCoord;
in vec4 FragColor;
in vec2 FragSize;
in vec4 FragRadius;
in float FragSmoothness;

uniform sampler2D Sampler0;

out vec4 OutColor;

void main() {
    float dist = rdistBox(FragSize, FragCoord, FragRadius);
    float alpha = rfill(dist, FragSmoothness, fwidth(dist));
    vec4 color = vec4(1.0, 1.0, 1.0, alpha) * texture(Sampler0, TexCoord) * FragColor;

    if (color.a == 0.0) {
        discard;
    }

    OutColor = color;
}