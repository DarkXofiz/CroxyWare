#version 150

#moj_import <hex:common.glsl>

in vec2 FragCoord;
in vec4 FragColor;
in vec2 FragSize;
in vec4 FragRadius;
in float FragStrength;
in float FragSpread;

out vec4 OutColor;

void main() {
    float dist = rdist(FragCoord, FragSize * 0.5 - 1.0, FragRadius) - 1.0;

    float aa = max(fwidth(dist), 1e-4);
    float spread = max(FragSpread, 1e-4);

    float t = clamp(dist / spread, 0.0, 1.0);
    float fall = 1.0 - t;
    fall *= fall;

    float inner = smoothstep(-2.0 * aa, 0.0, dist);

    float alpha = clamp(FragStrength, 0.0, 1.0) * fall * inner;

    vec4 color = vec4(FragColor.rgb, FragColor.a * alpha);

    if (color.a <= 0.0) discard;

    OutColor = color;
}
