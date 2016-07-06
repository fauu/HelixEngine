attribute vec3 a_position;
attribute vec2 a_texCoord0;

uniform mat4 u_worldTrans;
uniform mat4 u_projTrans;
uniform vec4 u_color;

varying vec4 v_color;
varying vec2 v_texCoord0;

void main() {
	v_color = u_color;
	v_texCoord0 = a_texCoord0;
	
	gl_Position = u_projTrans * u_worldTrans * vec4(a_position, 1.0);
}