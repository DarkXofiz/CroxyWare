#version 150

in vec3 Position;
in vec4 Color;
in vec2 Size;
in vec4 Radius;
in float Thickness;
in vec2 VEC2F;
in vec2 Coord;

#moj_import <hex:transforms.glsl>

out vec2 FragCoord;
out vec4 FragColor;
out vec2 FragSize;
out vec4 FragRadius;
out float FragThickness;
out vec2 FragSmoothness;

void main() {
    FragCoord = Coord;
    FragColor = Color;
    FragSize = Size;
    FragRadius = Radius;
    FragSmoothness = VEC2F;
    FragThickness = Thickness;

    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
}