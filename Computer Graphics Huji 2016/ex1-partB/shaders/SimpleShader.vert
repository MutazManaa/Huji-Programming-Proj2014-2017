#version 330

uniform vec4 scale;
uniform vec4 offset;

layout(location = 0) in vec4 position;

void main()
{
    vec4 pos = position;
    pos.x *= scale.x;
    pos.y *= scale.y;
    pos.x += offset.x;
    pos.y += offset.y;
    gl_Position = pos;
}
