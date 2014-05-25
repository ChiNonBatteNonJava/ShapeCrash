precision mediump float;

attribute vec4 vPosition;
attribute vec3 vNormal;
varying float vIntensity;
uniform mat4 uMVPMatrix;
uniform mat4 uNormalMatrix;

void main()
{
	vec3 vNorm = normalize(vec3(uNormalMatrix * vec4(vNormal, 0.0)));
	
	vIntensity = max(dot(vNorm, vec3(1.0, 1.0, 1.0)), 0.0);
	
	gl_Position = uMVPMatrix * vPosition;
}
