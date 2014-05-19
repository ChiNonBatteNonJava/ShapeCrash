attribute vec4 vPosition ;
attribute vec3 vNormal;
attribute vec2 vUvs;
varying vec3 vNorm;
uniform mat4 uMVPMatrix ;
varying vec2 vUv;

void main()
{
	gl_Position =  uMVPMatrix *  vPosition;
	
	vNorm=vNormal;
	vUv=vUvs;

	
	

}