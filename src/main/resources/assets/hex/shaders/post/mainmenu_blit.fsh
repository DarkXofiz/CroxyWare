#version 330

uniform sampler2D InSampler;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    fragColor = vec4(texture(InSampler, vec2(texCoord.x, 1.0 - texCoord.y)).rgb, 1.0);
}
