#version 150

in vec3 Position;
in vec4 Color;
in vec2 Coord;

#moj_import <hex:transforms.glsl>

out vec4 FragColor;
out vec2 FragCoord;

void main() {
    FragColor = Color;
    FragCoord = Coord;
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
}
