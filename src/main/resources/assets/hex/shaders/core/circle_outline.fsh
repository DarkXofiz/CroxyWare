#version 150

in vec4 FragColor;
in vec2 FragCoord;

out vec4 OutColor;

void main() {
    float edge = FragCoord.x;
    float aa = fwidth(edge);
    float coverage = aa > 0.0 ? clamp((1.0 - abs(edge)) / aa, 0.0, 1.0) : 1.0;

    vec4 color = vec4(FragColor.rgb, FragColor.a * coverage);
    if (color.a == 0.0) {
        discard;
    }

    OutColor = color;
}
