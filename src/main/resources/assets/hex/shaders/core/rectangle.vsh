#version 150

in vec3 Position;
in vec4 Color;
in vec2 Size;
in vec4 Radius;
in float Smoothness;
in vec2 Coord;

#moj_import <hex:transforms.glsl>

out vec2 FragCoord;
out vec4 FragColor;
out vec2 FragSize;
out vec4 FragRadius;
out float FragSmoothness;

void main() {
    FragCoord = Coord;
    FragColor = Color;
    FragSize = Size;
    FragRadius = Radius;
    FragSmoothness = Smoothness;

    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
}
