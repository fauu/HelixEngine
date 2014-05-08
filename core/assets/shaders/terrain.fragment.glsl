#ifdef GL_ES
precision mediump float;
#endif

varying vec3 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_texture;

void main() {
    gl_FragColor = vec4(v_color, 1.0) * texture2D(u_texture, v_texCoord0);
}