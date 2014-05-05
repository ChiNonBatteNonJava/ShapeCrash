attribute vec4 vPosition ;
attribute vec2 vUvs;
attribute vec3 vNormal;
varying vec3 vNorm;
uniform mat4 uMVPMatrix ;
varying vec2 vUv;
varying vec3 pos;
void main()
{
	gl_Position =  uMVPMatrix *  vPosition;
	
	vNorm=vNormal;
	
	vUv=vUvs;
	pos=gl_Position.xyz;


}