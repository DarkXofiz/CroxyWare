#version 330

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>
#moj_import <hex:jump_circle.glsl>

in vec3 Position;
in vec2 UV0;

out vec3 localPos;
out vec2 surface;
out vec4 centreClip;

void main() {
    localPos = Position;
    surface = UV0;
    centreClip = ProjMat * ModelViewMat * vec4(Centre.xyz, 1.0);
    gl_Position = ProjMat * ModelViewMat * vec4(Centre.xyz + Position, 1.0);
}
