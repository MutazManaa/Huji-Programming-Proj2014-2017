#include "ShaderIO.h"
#include "Sphere.h"

#include <GL/glew.h>
#ifdef __APPLE__
#include <OpenGL/OpenGL.h>
#else
#include <GL/gl.h>
#endif

#include <glm/glm.hpp>
#include <glm/gtc/type_ptr.hpp>

#define SHADERS_DIR "shaders/"

Sphere::Sphere() : _vao(0), _vbo(0), radius(RADIUS) { }

Sphere::~Sphere()
{
    if (_vao != 0)
        glDeleteVertexArrays(1, &_vao);
    if (_vbo != 0)
        glDeleteBuffers(1, &_vbo);
}

void Sphere::init()
{
    programManager::sharedInstance().createProgram("defaultSphere",
                                                   SHADERS_DIR  "SphereShader.vert",
                                                   SHADERS_DIR  "SphereShader.frag");

    GLuint program = programManager::sharedInstance().programWithID("defaultSphere");

    {
        // For this example we create a single triangle:
        float vertices[864 * 4];
        float angel = 2 * M_PI / (864.0f);
        int k = 0;
        for (int i = 0; i < 864 * 4; i+=4) {
            vertices[i]= RADIUS * cos(k * angel);
            vertices[i+1]= RADIUS * sin(k * angel);
            vertices[i+2]=0.0f;
            vertices[i+3]=1.0f;
            k++;

        }

        // Create and bind the object's Vertex Array Object:
        glGenVertexArrays(1, &_vao);
        glBindVertexArray(_vao);

        // Create and load vertex data into a Vertex Buffer Object:
        glGenBuffers(1, &_vbo);
        glBindBuffer(GL_ARRAY_BUFFER, _vbo);
        glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);

        // Tells OpenGL that there is vertex data in this buffer object and what form that vertex data takes:

        // Obtain attribute handles:
        _posAttrib = glGetAttribLocation(program, "position");
        glEnableVertexAttribArray(_posAttrib);
        glVertexAttribPointer(_posAttrib, // attribute handle
                              4,          // number of scalars per vertex
                              GL_FLOAT,   // scalar type
                              GL_FALSE,
                              0,
                              0);

        // Unbind vertex array:
        glBindVertexArray(0);
    }
}

void Sphere::draw()
{
    // Set the program to be used in subsequent lines:
    GLuint program = programManager::sharedInstance().programWithID("defaultSphere");
    glUseProgram(program);

    GLenum polygonMode = GL_LINE;   // Also try using GL_FILL and GL_POINT
    glPolygonMode(GL_FRONT_AND_BACK, polygonMode);

    // Draw using the state stored in the Vertex Array object:
    glBindVertexArray(_vao);

    size_t numberOfVertices = 864;
    glDrawArrays(GL_TRIANGLES, 0, numberOfVertices);

    // Unbind the Vertex Array object
    glBindVertexArray(0);

    // Cleanup, not strictly necessary
    glUseProgram(0);
}

void Sphere::resize(int width, int height)
{
    _width	= width;
    _height = height;
    _offsetX = 0;
    _offsetY = 0;
}