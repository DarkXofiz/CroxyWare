#version 150

#moj_import <hex:common.glsl>

in vec2 FragCoord;
in vec4 FragColor;
in vec2 FragSize;
in vec4 FragRadius;
in float FragSmoothness;

out vec4 OutColor;

void main() {
    float alpha;
    if (FragSmoothness <= -1.5) {
        vec2 paddedSize = FragSize + 2.0;
        vec2 pos = (FragCoord - 0.5) * paddedSize;
        vec2 halfExtent = FragSize * 0.5;
        vec2 aa = max(fwidth(pos), vec2(1e-5));
        vec2 minHalf = max(halfExtent, aa * 0.5);
        vec2 cov = clamp((minHalf - abs(pos)) / aa + 0.5, 0.0, 1.0);
        alpha = min(cov.x, cov.y);
    } else {
        float dist = rdistBox(FragSize, FragCoord, FragRadius);
        alpha = rfill(dist, FragSmoothness, fwidth(dist));
    }

    vec4 color = vec4(FragColor.rgb, FragColor.a * alpha);

    if (color.a == 0.0) {
        discard;
    }

    OutColor = color;
}
