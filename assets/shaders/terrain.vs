attribute vec4 vPosition ;
attribute vec2 vUvs;
attribute vec3 vNormal;
varying vec3 color;
uniform mat4 uMVPMatrix ;
void main()
{
	gl_Position =  uMVPMatrix *  vPosition;
	float c=0.0;
	if( vPosition.y > -1.0 ){
		c=1.0;
	}
	color=vec3(0,0,c);

}