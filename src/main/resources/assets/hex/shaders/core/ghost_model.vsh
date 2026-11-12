#version 330

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>
#moj_import <minecraft:globals.glsl>

in vec3 Position;
in vec2 UV0;
in vec4 Color;
in vec3 Normal;

out vec2 texCoord0;
out vec4 vertexColor;
out vec3 viewNormal;
out vec3 viewDir;
out float worldHeight;

void main() {
    vec4 viewPos = ModelViewMat * vec4(Position, 1.0);
    gl_Position = ProjMat * viewPos;

    texCoord0 = UV0;
    vertexColor = Color;
    viewNormal = mat3(ModelViewMat) * Normal;
    viewDir = -viewPos.xyz;
    worldHeight = Position.y + float(CameraBlockPos.y) - CameraOffset.y;
}
