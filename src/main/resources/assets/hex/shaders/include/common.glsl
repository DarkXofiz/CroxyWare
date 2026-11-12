float rdist(vec2 pos, vec2 size, vec4 radius) {
    radius.xy = (pos.x > 0.0) ? radius.xy : radius.wz;
    radius.x  = (pos.y > 0.0) ? radius.x : radius.y;
    radius.x  = (radius.x > 0.0) ? min(radius.x, min(size.x, size.y)) : -1.0;

    vec2 v = abs(pos) - size + radius.x;
    return min(max(v.x, v.y), 0.0) + length(max(v, 0.0)) - radius.x;
}

float rdistBox(vec2 size, vec2 coord, vec4 radius) {
    vec2 center = size * 0.5;
    return rdist(center - (coord * size), center - 1.0, radius);
}

float rfill(float dist, float smoothness, float aa) {
    if (smoothness < -0.5) {
        return clamp(0.5 + (1.0 - dist) / max(aa, 1e-5), 0.0, 1.0);
    }
    if (smoothness <= 0.0) {
        return 1.0 - step(1.0, dist);
    }
    float w = smoothness > 4.0 ? smoothness : aa * (smoothness + 1.0);
    return 1.0 - smoothstep(1.0 - w, 1.0, dist);
}

const vec2[4] RECT_VERTICES_COORDS = vec2[] (
    vec2(0.0, 0.0), 
    vec2(0.0, 1.0), 
    vec2(1.0, 1.0),
    vec2(1.0, 0.0)
);

vec2 rvertexcoord(int id) {
    return RECT_VERTICES_COORDS[id % 4];
}