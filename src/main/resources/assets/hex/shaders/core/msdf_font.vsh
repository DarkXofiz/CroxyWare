#version 150

in vec3 Position;
in vec2 UV0;
in vec4 Color;
in float Thickness;
in float Smoothness;
in float Range;
#moj_import <hex:transforms.glsl>

out vec2 TexCoord;
out vec4 FragColor;
out float FragThickness;
out float FragSmoothness;
out float FragRange;

void main() {
    TexCoord = UV0;
    FragColor = Color;
    FragThickness = Thickness;
    FragSmoothness = Smoothness;
    FragRange = Range;

    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
}