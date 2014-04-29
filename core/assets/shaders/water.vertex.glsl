attribute vec3 a_position;
attribute vec2 a_texCoord0;

uniform mat4 u_worldTrans;
uniform mat4 u_projTrans;
uniform vec4 u_color;

varying vec2 v_texCoord0;
varying vec4 v_color;
varying vec3 v_vertexColor;

void main() {
    v_texCoord0 = a_texCoord0;
    v_color = u_color;

    vec3 newPos = vec3(a_position.x, a_position.y, a_position.z);

    gl_Position = u_projTrans * u_worldTrans * vec4(newPos, 1.0);
}