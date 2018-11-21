#ifndef _H_SPHERE
#define _H_SPHERE

#include <iostream>
#ifdef __APPLE__
#include <OpenGL/OpenGL.h>

#else
#include <GL/gl.h>
#endif

#define RADIUS 0.8

class Sphere {

private:
    float radius;

    GLuint _vao, _vbo;

    // Attribute handle:
    GLint _posAttrib;

    // View port frame:
    float _width, _height, _offsetX, _offsetY;

public:
    Sphere();
    ~Sphere();

    void init();
    void draw();

    void resize(int width, int height);
};


#endif