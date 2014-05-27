precision mediump float;

attribute vec4 vPosition;
attribute vec2 vUvs;
attribute vec3 vNormal;
varying vec3 vNorm;
varying vec2 vUv;
uniform mat4 uMVPMatrix;
uniform mat4 uNormalMatrix;

void main()
{
	vNorm = normalize(vec3(uNormalMatrix * vec4(vNormal, 0.0)));
	
	vUv = vUvs;
	
	gl_Position =  uMVPMatrix *  vPosition;
}
