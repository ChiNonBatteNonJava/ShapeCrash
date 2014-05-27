precision mediump float;

attribute vec4 vPosition;
attribute vec2 vUvs;

uniform mat4 uMVPMatrix;

varying vec2 vUv;

void main ()
{
	vUv = vUvs;
	
	gl_Position = uMVPMatrix * vPosition;
}
