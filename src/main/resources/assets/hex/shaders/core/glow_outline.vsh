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
out vec4 FragColor;
out vec2 FragSize;
out vec4 FragRadius;
out float FragStrength;
out float FragSpread;

void main() {
    FragCoord = Coord;
    FragColor = Color;
    FragSize = Size;
    FragRadius = Radius;
    FragStrength = Smoothness;
    FragSpread = Range;

    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
}
