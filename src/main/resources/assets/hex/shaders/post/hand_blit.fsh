#version 330

uniform sampler2D InSampler;
uniform sampler2D MaskSampler;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 g = texture(InSampler, texCoord);
    if (g.a < 0.0015) discard;
    float mask = texture(MaskSampler, texCoord).a;
    fragColor = vec4(g.rgb, g.a * (1.0 - mask));
}
