//
//  Model.h
//  cg-projects
//
//  Created by HUJI Computer Graphics course staff, 2013.
//

#ifndef __ex0__Model__
#define __ex0__Model__

#include <iostream>
#include <vector>
#ifdef __APPLE__
#include <OpenGL/OpenGL.h>
#else
#include <GL/gl.h>
#endif

#define START_RADIUS 0.2
class Model {

    const static float startRadius = START_RADIUS;

	GLuint _vao, _vbo;

	// Attribute handle:
	GLint _posAttrib;

	GLint _offSetAttrib;

	GLint _lightPoint;

	GLint _resolution;

	// Uniform handle:
	GLint _fillColorUV;

    GLint _scaleAttrib;

	// View port frame:
	float _width, _height, _offsetX, _offsetY;

	float _xAspect, _yAspect;

    std::vector<float> ballsPosX;
    std::vector<float> ballsPosY;

    std::vector<float> ballsDirection;

    std::vector<float> ballsR;
    std::vector<float> ballsG;
    std::vector<float> ballsB;

	float shrinkBallBeforeCollision(unsigned int index);
public:
	Model();

public:
	virtual ~Model();

public:
	void init();
	
public:
	void draw();
    void addBall(int x, int y);
	
public:
	void resize(int width, int height);
	
};

#endif /* defined(__ex0__Model__) */
