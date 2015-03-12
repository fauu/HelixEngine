#ifdef GL_ES
#define LOWP lowp
#define MED mediump
precision lowp float;
#else
#define LOWP  
#define MED 
#endif
uniform sampler2D u_texture0;
uniform vec2 treshold;
varying MED vec2 v_texCoords;
void main()
{	
 	gl_FragColor = (texture2D(u_texture0, v_texCoords) - vec4(treshold.r))  * treshold.g;
}