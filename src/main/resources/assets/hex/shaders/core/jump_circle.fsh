#version 330

#moj_import <hex:jump_circle.glsl>

uniform sampler2D Sampler0;

in vec3 localPos;
in vec2 surface;
in vec4 centreClip;

out vec4 fragColor;

void main() {
    JumpSample s = jumpSample(localPos, surface);
    if (s.coverage < 0.004) {
        discard;
    }

    vec4 tint = jumpTint(s.dir);
    vec4 color = texture(Sampler0, s.texCoord) * vec4(tint.rgb, tint.a * s.coverage);
    if (color.a < 0.004) {
        discard;
    }
    fragColor = color;
}
