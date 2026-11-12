#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec4 vertexColor;

out vec4 fragColor;

const float radius = 8.0;

void main() {
    vec2 texel = 1.0 / textureSize(DiffuseSampler, 0);

    vec4 result = vec4(0.0);
    float total = 0.0;

    for(int x = -8; x <= 8; x++) {
        for(int y = -8; y <= 8; y++) {

            vec2 offset = vec2(float(x), float(y));

            float weight =
                exp(
                    -(dot(offset, offset))
                    / (2.0 * radius * radius)
                );

            result += texture(
                DiffuseSampler,
                texCoord + offset * texel
            ) * weight;

            total += weight;
        }
    }

    result /= total;

    vec4 original = texture(DiffuseSampler, texCoord);

    vec4 bloom = result * 1.5;

    fragColor =
        original +
        bloom * original.a;
}