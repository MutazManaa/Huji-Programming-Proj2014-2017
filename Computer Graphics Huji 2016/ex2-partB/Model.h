//
//  Model.h
//  cg-projects
//
//  Created by HUJI Computer Graphics course staff, 2013.
//

#ifndef __ex0__Model__
#define __ex0__Model__

#include <iostream>
#ifdef __APPLE__
#include <OpenGL/OpenGL.h>

#else
#include <GL/gl.h>
#endif

#include "OpenMesh/Core/IO/MeshIO.hh"
#include "OpenMesh/Core/Mesh/PolyMesh_ArrayKernelT.hh"
#include "glm/gtc/matrix_transform.hpp"
#include <glm/gtx/transform.hpp>

#define PERSPECTIVE true
#define ORTHO false

typedef OpenMesh::PolyMesh_ArrayKernelT<> Mesh;

Mesh::Point computeCenterAndBoundingBox(Mesh& mesh);

class Model {
	
	GLuint _vao, _vbo, program ,_textureMapAttrib, _textureBumpAttrib,_textureSphereAttrib;

	// Attribute handle:
	GLint _posAttrib;
    GLint _vertexNormalAttrib, _faceNormalAttrib,_estimationModeAttrib, _rotateAttrib, _translateAttrib, _scaleAttrib, _projectionAttrib;
    GLint _shadingMode,_specExp ,_colouringAttrib,_specExpAttrib;
    GLint _texturingModeAttrib, _texturScaleAttrib, _turbulenceCoeffAttrib;
    GLint _texturingMode;
    GLfloat  _texturingValue, _turbulenceValue;
    GLint _meshcenterAttrib, _upperRightAttrib,_lowerLeftAttrib;



	// Uniform handle:
	GLint _fillColorUV;
	GLint _finalMat;
    GLint _estimationMode;

    float* _vertices = NULL;

	// View port frame:
	GLfloat _width, _height, _offsetX, _offsetY;
    Mesh::Point _normalize;

    int _numberOfVertices;

    bool _mode = PERSPECTIVE;


    bool _isclicked = false;
    int _currentX;
	int _currentY;


    Mesh::Point _objectCenter;
    Mesh::Point _upperRight;
    Mesh::Point _lowerLeft;


    glm::mat4 _rotateMat;
    glm::mat4 _scaleMat;
    glm::mat4 _translateMat;
    glm::mat4 _viewMat;
    glm::mat4 _modeMat;
    glm::mat4 _modelMat;

    glm::mat4 _modelMatrix;



    GLenum polygonMode;

    void computeCenterAndBoundingBox(Mesh& mesh);

    const char* meshPath;
    void loadMesh();
public:
	Model(const char* path);
	Model();

public:
	virtual ~Model();

public:
	void init();
    void initBuffers();
	void initMatrices();


public:
	void draw();
	
public:
	void resize(int width, int height);
    void setRotationMatrix(int x, int y);
    void setScale(int x, int y);
	void setTranlationMatrix(int x, int y);
    void getClick(bool isclicked,int x,int y);
    void changePolygonMod();
    void changeProjectionMode();
	void changeEstimateMode();
    void setShading(GLint mode);
    void shinessUP();
    void shinessDown();
    void changeTexturingMode();
    void decreaseTexturing();
    void increaseTexturing();
    void decreaseTurbulence();
    void increaeTurbulence();
};

#endif /* defined(__ex0__Model__) */
