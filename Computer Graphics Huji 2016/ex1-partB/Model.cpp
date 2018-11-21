//
//  Model.cpp
//  cg-projects
//
//  Created by HUJI Computer Graphics course staff, 2013.
//

#include "ShaderIO.h"
#include "Model.h"
#include <cmath>

#include <GL/glew.h>
#ifdef __APPLE__
#include <OpenGL/OpenGL.h>
#else
#include <GL/gl.h>
#endif

#include <glm/glm.hpp>
#include <glm/gtc/type_ptr.hpp>
#include "glm/gtc/matrix_transform.hpp"

#define SHADERS_DIR "shaders/"

#define START_ASPECT 1.0
#define NUMBER_OF_VERTICES 360
#define ATT_VERTEX 12
#define ANGLE_PER_TRIANGLE M_PI/12
#define START_SIZE 600
#define MAX_DIST 0.4f
#define SPEED 0.05
#define TOP_RIGHT 1.0f
#define BOTTOM_LEFT -1.0f
#define RAND_ANGLE 30
#define RAND_COLOR 100

Model::Model() :
        _vao(0), _vbo(0), ballsPosX(), ballsPosY(),ballsDirection(), ballsR(), ballsG(), ballsB()
{
    srand (time(NULL));
}

Model::~Model()
{
    if (_vao != 0)
        glDeleteVertexArrays(1, &_vao);
    if (_vbo != 0)
        glDeleteBuffers(1, &_vbo);
}

void Model::init()
{
    programManager::sharedInstance()
            .createProgram("default",
                           SHADERS_DIR "SimpleShader.vert",
                           SHADERS_DIR "SimpleShader.frag");

    GLuint program = programManager::sharedInstance().programWithID("default");

    // Obtain uniform variable handles:
    _fillColorUV  = glGetUniformLocation(program, "fillColor");
    _scaleAttrib = glGetUniformLocation(program, "scale");
    _offSetAttrib = glGetUniformLocation(program, "offset");
    _lightPoint = glGetUniformLocation(program, "lightPoint");
    _resolution = glGetUniformLocation(program, "resolution");

    _xAspect = START_ASPECT;
    _yAspect = START_ASPECT;
    // Initialize vertices buffer and transfer it to OpenGL
    {
        // For this example we create a single circle:
        float vertices[NUMBER_OF_VERTICES];
        float prevX = 0.75f, prevY = 0.0f;
        for (int i = 0; i < NUMBER_OF_VERTICES; i+=ATT_VERTEX) {
            vertices[i]=0.0f;
            vertices[i+1]=0.0f;
            vertices[i+2]=1.0f;
            vertices[i+3]=1.0f;

            vertices[i+4]=prevX;
            vertices[i+5]=prevY;
            vertices[i+6]=1.0f;
            vertices[i+7]=1.0f;

            float x = prevX*cos(ANGLE_PER_TRIANGLE) - prevY*sin(ANGLE_PER_TRIANGLE);
            float y = prevY*cos(ANGLE_PER_TRIANGLE) + prevX*sin(ANGLE_PER_TRIANGLE);

            vertices[i+8]=x;
            vertices[i+9]=y;
            vertices[i+10]=1.0f;
            vertices[i+11]=1.0f;

            prevX = x;
            prevY = y;
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

float Model::shrinkBallBeforeCollision(unsigned int index)
{
    _xAspect = START_SIZE / _width;
    _yAspect = START_SIZE / _height;
    float distance = MAX_DIST;
    unsigned int i;
    for (i = 0; i < ballsPosX.size(); ++i)
    {
            if(index != i)
            {
                float deltaX = ballsPosX[i] - ballsPosX[index];
                float deltaY = ballsPosY[i] - ballsPosY[index];

                float currDis = sqrt(deltaX*deltaX + deltaY*deltaY);

                distance = currDis < distance ? currDis : distance;
        }
    }
    return (distance/MAX_DIST);
}



void Model::draw()
{
    // Set the program to be used in subsequent lines:
    GLuint program = programManager::sharedInstance().programWithID("default");
    glUseProgram(program);

    GLenum polygonMode = GL_FILL;   // Also try using GL_FILL and GL_POINT
    glPolygonMode(GL_FRONT_AND_BACK, polygonMode);

    glBindVertexArray(_vao);

    size_t numberOfVertices = NUMBER_OF_VERTICES;



    unsigned int i;
    for(i = 0; i < ballsPosX.size(); i++)
    {

        glUniform4f(_resolution , _width, _height, 0.0f, 0.0f);
        // Set uniform variable with RGB values:
        glUniform4f(_fillColorUV, ballsR[i], ballsG[i], ballsB[i], 1.0);

        float newAsp = shrinkBallBeforeCollision(i);
        // set the scale
        glUniform4f(_scaleAttrib, _xAspect * startRadius * newAsp, _yAspect * startRadius* newAsp, 0.0f, 0.0f);

        ballsPosX[i] += SPEED*cos(ballsDirection[i])*_xAspect;
        ballsPosY[i] += SPEED*sin(ballsDirection[i])*_yAspect;

        if((ballsPosY[i] >= TOP_RIGHT && (ballsDirection[i] > 0 && ballsDirection[i] < M_PI))
           ||(ballsPosY[i] <= BOTTOM_LEFT && (ballsDirection[i] > M_PI && ballsDirection[i] < 2*M_PI)))
        {
                ballsDirection[i] = 2*M_PI - ballsDirection[i];
        }


        if((ballsPosX[i] >= TOP_RIGHT&&(ballsDirection[i] < M_PI/2 || ballsDirection[i] > 1.5*M_PI))
           || (ballsPosX[i] <= BOTTOM_LEFT && (ballsDirection[i] > M_PI/2 || ballsDirection[i] < 1.5*M_PI)))
        {
            ballsDirection[i] = (M_PI - ballsDirection[i]);
            if(ballsDirection[i] < 0)
    ballsDirection[i]+=2*M_PI;
}


if(ballsPosX[i])
{
glUniform4f(_offSetAttrib, ballsPosX[i], ballsPosY[i], 0.0f, 0.0f);
// set uniform variable for highlight point
glUniform4f(_lightPoint , (float)ballsPosX[i],(float)ballsPosY[i] , 1.0f, 1.0f);

}

// Draw using the state stored in the Vertex Array object:
glDrawArrays(GL_TRIANGLES, 0, numberOfVertices);
}


// Unbind the Vertex Array object
glBindVertexArray(0);

// Cleanup, not strictly necessary
glUseProgram(0);

}


void Model::addBall(int x, int y)
{
    float currX = (float)x/(_width/2) - 1;
    float currY = 1 - (float)y/(_height/2);

    ballsPosX.push_back(currX);
    ballsPosY.push_back(currY);
    ballsDirection.push_back(((rand()%RAND_ANGLE)/(float)RAND_ANGLE)*2*M_PI);

    float red = (rand()%RAND_COLOR)/(float)RAND_COLOR;
    float green = (rand()%RAND_COLOR)/(float)RAND_COLOR;
    float blue = (rand()%RAND_COLOR)/(float)RAND_COLOR;

    ballsR.push_back(red);
    ballsG.push_back(green);
    ballsB.push_back(blue);
}

void Model::resize(int width, int height)
{
    _width	= width;
    _height = height;

    _offsetX = 0;
    _offsetY = 0;
}
