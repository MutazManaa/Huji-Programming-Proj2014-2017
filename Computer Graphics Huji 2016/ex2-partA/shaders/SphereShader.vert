#version 330

uniform vec4 scale;
uniform vec4 offset;
uniform mat4 finalMat;

layout(location = 0) in vec4 position;

out vec4 fragmentColor;

void main()
{
    gl_Position = position;
   
}
