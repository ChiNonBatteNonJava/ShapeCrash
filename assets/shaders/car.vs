precision mediump float;

attribute vec4 vPosition ;
attribute vec2 vUvs;
attribute vec3 vNormal;
uniform mat4 uMVPMatrix;
uniform mat4 uNormalMatrix;
uniform vec4 uLightPos;
varying vec2 vUv;
varying float vIntensity;

void main()
{
	vec3 vNorm = normalize(vec3(uNormalMatrix * vec4(vNormal, 0.0)));
	
	vec3 lightDir = normalize(vec3(uLightPos));
	
	vIntensity = max(dot(vNorm, lightDir), 0.0);
	
	vUv = vUvs;
	
	gl_Position =  uMVPMatrix *  vPosition;
}
