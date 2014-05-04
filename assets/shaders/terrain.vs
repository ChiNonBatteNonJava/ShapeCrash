attribute vec4 vPosition ;
attribute vec2 vUvs;
attribute vec3 vNormal;
varying vec3 color;
uniform mat4 uMVPMatrix ;
varying vec2 vUv;
void main()
{
	vec3 n = vNormal;
	
	gl_Position =  uMVPMatrix *  vPosition;
	float c=0.0;
	vec3 col1=vec3(0,0,1);
	if( vPosition.y > 1.0 ){
		c=1.0;
		col1=vec3(1,0,1);
	}
	color=vec3(col1);
	vUv=vUvs;

}