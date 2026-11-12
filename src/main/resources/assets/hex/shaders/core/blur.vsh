#version 150

in vec3 Position;
in vec4 Color;
in vec2 Size;
in vec4 Radius;
in float Smoothness;
in float Range;
in vec2 Coord;

#moj_import <hex:transforms.glsl>

out vec2 FragCoord;
out vec2 TexCoord;
out vec4 FragColor;
out vec2 FragSize;
out vec4 FragRadius;
out float FragSmoothness;
out float FragBlurRadius;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    FragCoord = Coord;
    TexCoord = gl_Position.xy * 0.5 + 0.5;
    FragColor = Color;
    FragSize = Size;
    FragRadius = Radius;
    FragSmoothness = Smoothness;
    FragBlurRadius = Range;
}