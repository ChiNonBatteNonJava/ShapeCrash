attribute vec4 vPosition;
attribute vec3 vNormal;
varying vec3 vNorm;
uniform mat4 uMVPMatrix;
uniform float uRadius;

void main()
{
	gl_Position =  uMVPMatrix * (uRadius * vPosition);
	
	vNorm = vNormal;
}
