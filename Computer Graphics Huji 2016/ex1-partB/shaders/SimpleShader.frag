#version 330
#define RADIUS 120

uniform vec4 fillColor;
uniform vec4 lightPoint;
uniform vec4 resolution;

out vec4 outColor;

void main()
{
	vec2 pos = gl_FragCoord.xy;

	vec2 res = resolution.xy;

	vec2 lc = vec2(lightPoint.x*(resolution.x/2)+resolution.x/2+20,lightPoint.y*(resolution.y/2)+resolution.y/2+20);

	float d = distance(pos,lc);

	float intence = 1.0 - d/(RADIUS/2);

	outColor = fillColor + intence*intence;
} 
