#version 330

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <hex:fx.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    if (texture(Sampler0, texCoord0).a == 0.0) {
        discard;
    }

    vec2 screenUv = gl_FragCoord.xy * TextureMat[3].xy;
    vec3 col = texture(Sampler1, screenUv).rgb;

    fragColor = vec4(handTint(col, screenUv, TextureMat[0], TextureMat[1], TextureMat[2].x, TextureMat[2].yz), 1.0);
}
