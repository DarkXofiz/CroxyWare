#version 150

in vec3 Position;
in vec2 UV0;
in vec4 Color;
in vec4 Radius;
in vec2 Size;
in float Smoothness;
in vec2 Coord;

#moj_import <hex:transforms.glsl>

out vec2 FragCoord;
out vec2 TexCoord;
out vec4 FragColor;
out vec2 FragSize;
out vec4 FragRadius;
out float FragSmoothness;


void main() {
    FragCoord = Coord;
    TexCoord = UV0;
    FragColor = Color;
    FragSmoothness = Smoothness;
    FragRadius = Radius;
    FragSize = Size;

    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
}