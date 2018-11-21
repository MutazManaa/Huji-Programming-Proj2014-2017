
#version 330

#define PHONG 1
#define GUARAUD 2
#define REGULAR 3
#define VERTEXNORMAL 0

layout(location = 0) in vec4 position;
layout(location = 1) in vec4 vertexNormal;
layout(location = 2) in vec4 faceNormal;

uniform mat4 finalMat;
uniform mat4 projection;
uniform mat4 translate;
uniform mat4 rotate;



uniform vec3 lightColor1 = vec3(1.0, 0.9, 0.7); // Firts light colour
uniform vec3 lightColor2 = vec3(0.6, 0.6, 1.0); // Second light colour
uniform vec3 ambientColor = vec3(1.0, 1.0, 1.0);// Ambient light color
uniform vec3 light_position1 = vec3(  3.0, 2.0,  1.0); // Firts light position
uniform vec3 light_position2 = vec3( -3.0, 0.0,  1.0); // Second light position
uniform vec3 ka = vec3(0.1, 0.1, 0.1); // Ambient coefficient
uniform vec3 kd = vec3(0.3, 0.3, 0.3); // Diffuse coefficient
uniform vec3 ks = vec3(0.3, 0.3, 0.3); // Specular coefficient

uniform float specExp;
uniform float estimationMode;
uniform float colourModel;

uniform vec4 objectCenter;
uniform vec4 upperRight;
uniform vec4 lowerLeft;

out vec4 shaderVertexcolour;
out vec3 shadingPosition;
out vec3 tempnormal;
out vec3 normalPosition;


void main()
{

    vec4 normal;

    if(estimationMode == VERTEXNORMAL)
    {
        normal = vertexNormal;
    }else{
        normal = faceNormal;
    }



	if(colourModel == PHONG)
	{


	    tempnormal = (rotate * translate * normal).xyz;
	    shadingPosition = (rotate * translate  * position).xyz;

		gl_Position = finalMat * position;
        normalPosition.x = abs((position.x + objectCenter.x) - lowerLeft.x) / (upperRight.x - lowerLeft.x);
        normalPosition.y = abs((position.y + objectCenter.y) - lowerLeft.y) / (upperRight.y - lowerLeft.y);
        normalPosition.z = abs((position.z + objectCenter.z) - lowerLeft.z) / (upperRight.z - lowerLeft.z);


	}

	else if(colourModel == GUARAUD)
	{
		gl_Position  = finalMat  * position;

		vec3 tempnormal = normalize((rotate * translate  * normal).xyz);
		vec3 shadingPosition = (rotate * translate  * position).xyz;


		vec3 toLight1 = normalize(light_position1 - shadingPosition);
		vec3 toLight2 = normalize(light_position2 - shadingPosition);
		vec3 toEye = normalize(vec3(0.0, 0.0, 3.0) - shadingPosition);

		vec3 diffColor =  ka*ambientColor + lightColor1 * kd * max(dot(tempnormal, toLight1), 0.0)
		+ lightColor2 * kd * max(dot(tempnormal, toLight2), 0.0);

		vec3 r1 = normalize(reflect(-toLight1, tempnormal));
		vec3 r2 = normalize(reflect(-toLight2, tempnormal));

		vec3 specularColor
		= ks * pow(max(dot(toEye, r1), 0.0), specExp) * lightColor1
		+ ks * pow(max(dot(toEye, r2), 0.0), specExp) * lightColor2;

		shaderVertexcolour = vec4(diffColor + specularColor, 1.0);
	}
	//as part 2
	else if(colourModel == REGULAR){
        gl_Position = finalMat * position;

        shaderVertexcolour = position + 0.2f;

	}
}