#version 330

uniform vec4 fillColor;

out vec4 outColor;

void main()
{
	outColor = vec4(0.0, 0.0, 0.0, 1.0);
	
	vec2 pos = mod(gl_FragCoord.xy, vec2(25));
	if ((pos.x > 12.5) && (pos.y > 12.5))
	{
		outColor=fillColor;
	}

	if ((pos.x < 12.5) && (pos.y < 12.5))
	{
		outColor=fillColor;
	}
} 
