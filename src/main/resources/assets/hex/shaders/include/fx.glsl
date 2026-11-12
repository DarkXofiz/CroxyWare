const float TAU = 6.28318530718;

vec3 boost(vec3 c, float s) {
    float l = dot(c, vec3(0.299, 0.587, 0.114));
    vec3 sat = mix(vec3(l), c, s);
    return clamp(sat * (1.0 + (s - 1.0) * 0.4), 0.0, 1.0);
}

vec3 hue2rgb(float h) {
    vec3 c = abs(fract(h + vec3(0.0, 2.0 / 3.0, 1.0 / 3.0)) * 6.0 - 3.0);
    return clamp(c - 1.0, 0.0, 1.0);
}

vec3 handTint(vec3 col, vec2 uv, vec4 top, vec4 bottom, float time, vec2 dir) {
    if (top.a <= 0.5) {
        return col;
    }
    vec3 tint = top.rgb;
    if (bottom.a > 0.5) {
        if (dot(dir, dir) < 0.5) dir = vec2(0.0, 1.0);
        vec2 perp = vec2(-dir.y, dir.x);
        float a = dot(uv, dir);
        float b = dot(uv, perp);
        float wave = sin((a * 5.0 - b * 1.5 - time * 0.6) * TAU)
                   + 0.5 * sin((a * 8.0 + b * 2.5 + time * 0.9) * TAU);
        float w = clamp(0.5 + 0.5 * (wave / 1.5), 0.0, 1.0);
        tint = mix(bottom.rgb, top.rgb, w);
    }
    float luma = dot(col, vec3(0.299, 0.587, 0.114));
    return luma * tint * 1.6;
}

vec4 glossBand(vec2 uv, float t, float sharpness, vec3 colorA, vec3 colorB, bool rainbow) {
    vec2 dir = vec2(0.85, 0.53);
    float a = dot(uv, dir);
    float b = dot(uv, vec2(-dir.y, dir.x));

    float wave = sin((a - t) * TAU) * 0.55
               + sin((a * 0.6 + b * 0.5 - t * 1.35) * TAU) * 0.30
               + sin((b * 0.8 + t * 0.5) * TAU) * 0.15;

    float norm = clamp(wave * 0.5 + 0.5, 0.0, 1.0);
    vec3 color = rainbow ? hue2rgb(fract(a * 0.15 + t * 0.05 + wave * 0.1)) : mix(colorB, colorA, norm);
    return vec4(color, pow(norm, sharpness));
}
