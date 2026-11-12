#version 330

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:globals.glsl>
#moj_import <hex:fx.glsl>

uniform sampler2D Sampler0;

in vec2 texCoord0;
in vec4 vertexColor;
in vec3 viewNormal;
in vec3 viewDir;
in float worldHeight;

out vec4 fragColor;

const float DAY_SECONDS = 1200.0;
const float SKIN_CUTOUT = 0.06;
const float RIM_POWER = 1.7;
const float RIM_GAIN = 2.2;
const float CORE_LEVEL = 0.34;
const float SCAN_BANDS = 3.6;
const float SCAN_SPEED = 0.42;
const float SCAN_DEPTH = 0.26;
const float DRIFT_BANDS = 1.1;
const float DRIFT_SPEED = 0.13;
const float DRIFT_DEPTH = 0.12;
const float PULSE_SPEED = 0.5;
const float PULSE_DEPTH = 0.12;
const float DETAIL_WASH = 0.78;
const float RIM_TINT = 0.6;
const float MIN_COVERAGE = 0.004;

void main() {
    vec4 skin = texture(Sampler0, texCoord0);
    if (skin.a < SKIN_CUTOUT) {
        discard;
    }

    float time = GameTime * DAY_SECONDS;
    float facing = abs(dot(normalize(viewNormal), normalize(viewDir)));
    float rim = pow(1.0 - facing, RIM_POWER) * RIM_GAIN;

    float scan = 0.5 + 0.5 * sin((worldHeight * SCAN_BANDS - time * SCAN_SPEED) * TAU);
    float drift = 0.5 + 0.5 * sin((worldHeight * DRIFT_BANDS + time * DRIFT_SPEED) * TAU);
    float pulse = 1.0 + PULSE_DEPTH * sin(time * PULSE_SPEED * TAU);

    float body = CORE_LEVEL * mix(1.0 - SCAN_DEPTH, 1.0, scan) * mix(1.0 - DRIFT_DEPTH, 1.0, drift);
    float coverage = (body + rim) * pulse * skin.a * vertexColor.a * ColorModulator.a;
    if (coverage < MIN_COVERAGE) {
        discard;
    }

    vec3 tint = vertexColor.rgb * ColorModulator.rgb;
    float luma = dot(skin.rgb, vec3(0.299, 0.587, 0.114));
    vec3 shade = tint * mix(luma, 1.0, DETAIL_WASH);

    fragColor = vec4(shade * (1.0 + rim * RIM_TINT), min(coverage, 1.0));
}
