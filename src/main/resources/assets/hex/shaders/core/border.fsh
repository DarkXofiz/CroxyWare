#version 150

#moj_import <hex:common.glsl>

in vec2 FragCoord;
in vec4 FragColor;
in vec2 FragSize;
in vec4 FragRadius;
in float FragThickness;
in vec2 FragSmoothness;

out vec4 OutColor;

void main() {
    vec2 center = FragSize * 0.5;
    float dist = rdist(center - (FragCoord.xy * FragSize), center - 1.0, FragRadius);
    float alpha = smoothstep(1.0 - FragThickness - FragSmoothness.x - FragSmoothness.y, 1.0 - FragThickness - FragSmoothness.y, dist);
    alpha *= 1.0 - smoothstep(1.0 - FragSmoothness.y, 1.0, dist);
    vec4 color = vec4(FragColor.rgb, FragColor.a * alpha);

    if (color.a == 0.0) discard;

    OutColor = color;
}

