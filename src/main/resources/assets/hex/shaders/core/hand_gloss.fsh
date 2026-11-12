#version 330

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <hex:fx.glsl>

uniform sampler2D Sampler0;

in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    if (texture(Sampler0, texCoord0).a == 0.0) {
        discard;
    }

    vec2 uv = gl_FragCoord.xy * TextureMat[3].xy * TextureMat[2].x;
    float t = TextureMat[2].z * TextureMat[1].w;
    vec4 band = glossBand(uv, t, TextureMat[2].y, TextureMat[0].xyz, TextureMat[1].xyz, TextureMat[2].w > 0.5);

    fragColor = vec4(band.rgb, band.a * TextureMat[0].w);
}
