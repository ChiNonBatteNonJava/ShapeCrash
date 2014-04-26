attribute vec4 vPosition ;
attribute vec2 vUvs;
attribute vec3 vNormal;
varying vec3 color;
uniform mat4 uMVPMatrix ;
void main()
{
	gl_Position =  uMVPMatrix *  vPosition;
	color=vec3(vUvs,1);

}