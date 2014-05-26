precision mediump float;

attribute vec4 vPosition;

uniform mat4 uMVPMatrix;

void main ()
{
	gl_Position = uMVPMatrix * (vec4(1.07, 1.07, 1.07, 1) * vPosition);
}
