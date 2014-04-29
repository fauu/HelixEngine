#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_texture;
uniform sampler2D u_reflectionTexture;

uniform vec2 u_waveData;

void main() {
    vec2 v_newCoords = vec2(v_texCoord0.x + 0.1 * u_waveData.x * cos(u_waveData.y + v_texCoord0.y),
        v_texCoord0.y + u_waveData.x * sin(u_waveData.y + 8.0 * v_texCoord0.x));

    gl_FragColor = mix(texture2D(u_texture, v_texCoord0),
        texture2D(u_reflectionTexture, v_newCoords), 0.08) +
        texture2D(u_reflectionTexture, v_newCoords) * 0.2;
}