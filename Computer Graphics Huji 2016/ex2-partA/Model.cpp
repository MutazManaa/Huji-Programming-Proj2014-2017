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
#include "bimage.h"

#define SHADERS_DIR "shaders/"


#define OBJECT_DEPTH 4.f
#define OBJECT_B_RAD 1.5f
#define FAR (OBJECT_DEPTH + OBJECT_B_RAD)
#define NEAR (OBJECT_DEPTH - OBJECT_B_RAD)
#define START_FOV 30.0f
#define RADIUS 0.8
#define TRANSLATE_LESS_COORD 10
#define ROTATE_LESS_COORD 300
#define VERTEX_NORMAL_EST 0
#define FACES_NORMAL_EST 1
#define START_SHINESS 200
#define MAX_SHINESS 500
#define MIN_SHINESS 0
#define SHINESS_INCREMENT 15
#define PHONG 1
#define MARBLE 1
#define START_TEXTURE 6
#define MAX_TEXTURE 60
#define MIN_TEXTURE 0
#define TEXTURE_INCREMENT 2
#define START_TURBULENCE 4.4
#define MAX_TURBULENCE 10
#define MIN_TURBULENCE 0
#define TURBULENCE_INCREMENT 0.2


Mesh::Point euclidian3D(Mesh::Point p1,Mesh::Point p2);
glm::vec3 calculateV(float x, float y, float r);
bool notInBall(float x, float y, float ratio);

Model::Model(const char* path) :
_vao(0), _vbo(0), meshPath(path), polygonMode(GL_FILL)
{

}

Model::Model() : _vao(0), _vbo(0)
{}

Model::~Model()
{
	if (_vao != 0)
		glDeleteVertexArrays(1, &_vao);
	if (_vbo != 0)
		glDeleteBuffers(1, &_vbo);
}

void Model::initMatrices()
{
    _modeMat = glm::perspective(float(glm::radians(START_FOV)), 1.0f, NEAR, FAR);

    glm::vec3 eye(0.f,0.f,OBJECT_DEPTH);
    glm::vec3 center(0.0f, 0.0f, 0.f);
    glm::vec3 up(0, 1, 0);

    _viewMat = glm::lookAt(eye, center, up);

    //initalize the whole matrices

    _modelMat = glm::mat4(1.0f);

    _translateMat = glm::mat4(1.0f);
    _rotateMat = glm::mat4(1.0f);
    _modelMatrix  = glm::mat4(1.0f);
    _scaleMat = glm::mat4(1.f);
}

//load mesh points vericies
void Model::loadMesh() {
    Mesh mesh;
    if (!OpenMesh::IO::read_mesh(mesh, meshPath)) {
        std::cout << "Path Mesh Error" << std::endl;
    }


    computeCenterAndBoundingBox(mesh);
    // number of faces*3 is the number of vertices we need to display
    _numberOfVertices = mesh.n_faces() * 3;

    // each face have 3 vertices and each vertices have 4 floats x,y,z,1
    _vertices = (float *) malloc(_numberOfVertices * 12 * sizeof(float));


    Mesh::FaceVertexIter fvIter;
    Mesh::FaceIter f_it, f_end(mesh.faces_end());

    mesh.request_face_normals();
    mesh.request_vertex_normals();
    mesh.update_normals();

    Mesh::Normal verticesNormalsTemp;
    Mesh::Normal facesNormalsTemp;
    _normalize = euclidian3D(_upperRight, _lowerLeft);

// normailze center and corners and centerd the cornaers

    for (int it = 0; it < 3; it++) {
    _lowerLeft[it] /= _normalize[it];
    _upperRight[it] /= _normalize[it];
    _objectCenter[it] /= _normalize[it];
        _lowerLeft[it] -= _objectCenter[it];
        _upperRight[it] -= _objectCenter[it];

    }



    int i = 0;
    //iterate on the faces of the mesh


    for (f_it = mesh.faces_begin(); f_it != f_end; ++f_it) {
        Mesh::FaceHandle faceH = f_it.handle();

        int j;
        // iterate on the face vertices and adding them to the vertices array
        for (fvIter = mesh.fv_iter(faceH); fvIter; ++fvIter) {
            j = 0;
            Mesh::Point p = mesh.point(fvIter.handle());
//normalize the points and center them
            _vertices[j + i] = p[j++]/ _normalize[0] - _objectCenter[0];
            _vertices[j + i] = p[j++]/ _normalize[1] - _objectCenter[1];
            _vertices[j + i] = p[j++]/ _normalize[2] - _objectCenter[2];
            _vertices[i + j++] = 1.0f;//colour

            //get the normals of vertices
            verticesNormalsTemp = mesh.normal(fvIter.handle());
            _vertices[i + j++] = verticesNormalsTemp[0];
            _vertices[i + j++] = verticesNormalsTemp[1];
            _vertices[i + j++] = verticesNormalsTemp[2];
            _vertices[i + j++] = 1.0f;// not matter


            //get the normals of faces
            facesNormalsTemp = mesh.normal(faceH);
            _vertices[i + j++] = facesNormalsTemp[0];
            _vertices[i + j++] = facesNormalsTemp[1];
            _vertices[i + j++] = facesNormalsTemp[2];
            _vertices[i + j++] = 1.0f;//not matter


            i += 12;


        }
    }

    mesh.release_vertex_normals();
    mesh.release_face_normals();



}

void Model::init()
{
	programManager::sharedInstance()
	.createProgram("default",
				   SHADERS_DIR "SimpleShader.vert",
				   SHADERS_DIR "SimpleShader.frag");

	program = programManager::sharedInstance().programWithID("default");
		
	// Obtain uniform variable handles:
	_fillColorUV  = glGetUniformLocation(program, "fillColor");
    _finalMat  = glGetUniformLocation(program, "finalMat");
    _posAttrib = glGetAttribLocation(program, "position");
    _vertexNormalAttrib = glGetAttribLocation(program, "vertexNormal");
    _faceNormalAttrib = glGetAttribLocation(program, "faceNormal");
    _estimationModeAttrib = glGetUniformLocation(program,"estimationMode");
    _rotateAttrib = glGetUniformLocation(program, "rotate");
    _translateAttrib = glGetUniformLocation(program, "translate");
    _scaleAttrib = glGetUniformLocation(program, "scale");
    _projectionAttrib = glGetUniformLocation(program, "projection");
    _specExpAttrib = glGetUniformLocation(program,"specExp");
    _colouringAttrib = glGetUniformLocation(program, "colourModel");
    _texturingModeAttrib = glGetUniformLocation(program, "texturingMode");
    _texturScaleAttrib = glGetUniformLocation(program, "textureScale");
    _turbulenceCoeffAttrib = glGetUniformLocation(program, "turbulenceCoeff");
    _textureMapAttrib = glGetUniformLocation(program, "textureMap");
    _textureBumpAttrib =  glGetUniformLocation(program, "textureBump");
    _textureSphereAttrib = glGetUniformLocation(program, "textureSphere");
    _meshcenterAttrib = glGetUniformLocation(program, "objectCenter");
    _upperRightAttrib = glGetUniformLocation(program, "upperRight");
    _lowerLeftAttrib = glGetUniformLocation(program, "lowerLeft");


    initMatrices();
    _specExp = START_SHINESS;
    _shadingMode = PHONG;
    _estimationMode = VERTEX_NORMAL_EST;
    _texturingMode = MARBLE;
    _texturingValue = START_TEXTURE;
    _turbulenceValue = START_TURBULENCE;




}

void Model::initBuffers(){

        loadMesh();
	// Initialize vertices buffer and transfer it to OpenGL
        // Create and bind the object's Vertex Array Object:
		glGenVertexArrays(1, &_vao);
		glBindVertexArray(_vao);
		
		// Create and load vertex data into a Vertex Buffer Object:
		glGenBuffers(1, &_vbo);
		glBindBuffer(GL_ARRAY_BUFFER, _vbo);

		
		// Tells OpenGL that there is vertex data in this buffer object and what form that vertex data takes:
        glBufferData(GL_ARRAY_BUFFER, _numberOfVertices * 12 * sizeof(float), _vertices, GL_STATIC_DRAW);

		// Obtain attribute handles:

		glEnableVertexAttribArray(_posAttrib);
		glVertexAttribPointer(_posAttrib, // attribute handle
							  4,          // number of scalars per vertex
							  GL_FLOAT,   // scalar type
							  GL_FALSE,
                              12 * sizeof(GL_FLOAT),
                              0);


        //face estimation
        glEnableVertexAttribArray(_faceNormalAttrib);
        glVertexAttribPointer(_faceNormalAttrib, // attribute handle
                              4,          // number of scalars per vertex
                              GL_FLOAT,   // scalar type
                              GL_FALSE, 12 * sizeof(GL_FLOAT),
                                  (GLvoid*)(8 * sizeof(GL_FLOAT)));

        //vertex normal estimation
        glEnableVertexAttribArray(_vertexNormalAttrib);
        glVertexAttribPointer(_vertexNormalAttrib, // attribute handle
                                  4,          // number of scalars per vertex
                                  GL_FLOAT,   // scalar type
                                  GL_FALSE,
                                  12 * sizeof(GL_FLOAT),
                                  (GLvoid*)(4 * sizeof(GL_FLOAT)));


        glUseProgram(program);

		// Unbind vertex array:
		glBindVertexArray(0);

        GLuint textureAttributes[3] = {_textureMapAttrib,_textureBumpAttrib,_textureSphereAttrib};
        const char* paths[3] = {"textures/brickwork-texture.bmp", "textures/brickwork-bump-map.bmp", "textures/spheremap2.bmp"};
        const char* attNames[3] = {"textureMap", "textureBump" , "textureSphere"};

        glGenTextures(3,textureAttributes);

        for(int idx = 0; idx < 3; idx++)
        {
            BImage image(paths[idx]);

            switch(idx)
            {
                case 0: glActiveTexture(GL_TEXTURE0); glBindTexture(GL_TEXTURE_2D, textureAttributes[idx]); break;
                case 1: glActiveTexture(GL_TEXTURE1); glBindTexture(GL_TEXTURE_2D, textureAttributes[idx]); break;
                case 2: glActiveTexture(GL_TEXTURE2); glBindTexture(GL_TEXTURE_2D, textureAttributes[idx]); break;
                default:std::cerr << "no more textures! \n " ; break;


            }


            // Setting texture properties
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

            // data pushed in the rexture
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.width(), image.height(), 0, GL_RGB, GL_UNSIGNED_BYTE, image.getImageData());
            //get the uniform location to save
            glUniform1i(glGetUniformLocation(program, attNames[idx]), idx);


        }

    glUseProgram(0);
    //free(paths);
    //free(attNames);



	}




/** DDDDDDDDDDRRRRRRRRRRRRRRRRRRRRRRRRRRRRAAAAAWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW*/
void Model::draw()
{


    // Set the program to be used in subsequent lines:
   // GLuint program = programManager::sharedInstance().programWithID("default");
    glUseProgram(program);
    glPolygonMode(GL_FRONT_AND_BACK, polygonMode);

    //the tranformation ++++++++++++++++++++++
    _modelMatrix =  _modeMat * _viewMat* _translateMat * _rotateMat * _scaleMat;


    glUniformMatrix4fv(_finalMat, 1,GL_FALSE, &_modelMatrix[0][0]);
    // Set uniform variable with RGB values:
    float red = 0.3f; float green = 0.5f; float blue = 0.7f;
    glUniform4f(_fillColorUV, red, green, blue, 1.0);
    glUniformMatrix4fv(_rotateAttrib,1,GL_FALSE, &_rotateMat[0][0]);
    glUniformMatrix4fv(_translateAttrib,1,GL_FALSE,&_translateMat[0][0]);
    glUniformMatrix4fv(_scaleAttrib,1,GL_FALSE,&_scaleMat[0][0]);
    glUniformMatrix4fv(_projectionAttrib,1,GL_FALSE,&_modeMat[0][0]);
    glUniform1f(_estimationModeAttrib,_estimationMode);
    glUniform1f(_specExpAttrib, _specExp);
    glUniform1f(_colouringAttrib, _shadingMode);
    glUniform1f(_texturingModeAttrib, _texturingMode);
    glUniform1f(_texturScaleAttrib, _texturingValue);
    glUniform1f(_turbulenceCoeffAttrib, _turbulenceValue);
    glUniform4f(_meshcenterAttrib,_objectCenter[0],_objectCenter[1],_objectCenter[2],1.0f);
    glUniform4f(_upperRightAttrib,_upperRight[0],_upperRight[1],_upperRight[2],1.0f);
    glUniform4f(_lowerLeftAttrib,_lowerLeft[0],_lowerLeft[1],_lowerLeft[2],1.0f);

    // Draw using the state stored in the Vertex Array object:
    glBindVertexArray(_vao);

    glDrawArrays(GL_TRIANGLES, 0, _numberOfVertices);

    // Unbind the Vertex Array object
    glBindVertexArray(0);



    // Cleanup, not strictly necessary
    glUseProgram(0);

}


/** end of DRWaw #############################################  */


void Model::changePolygonMod()
{
    if (polygonMode  == GL_FILL)
        polygonMode  = GL_LINE;
    else
        polygonMode  = GL_FILL;
}

void Model::resize(int width, int height)
{
    _width	= width;
    _height = height;
    _offsetX = 0;
    _offsetY = 0;
}

void Model::changeProjectionMode()
{
    _mode = !_mode;
    if(_mode == PERSPECTIVE)
    {
        _modeMat = glm::perspective(float(glm::radians(START_FOV)), float(float(_width)/_height), float(NEAR), float(FAR));
    }
    else
    {

        _modeMat  = glm::ortho(-1.f,1.f,-1.f,1.f,float(NEAR),float(FAR));
       //_modeMat  = glm::ortho(_lowerLeft[0],_upperRight[0],
                              //_lowerLeft[1] ,_upperRight[1], float(NEAR), float(FAR));

    }
}


/** This function computes the geometrical center and the axis aligned bounding box of the
object. The bounding box is represented by the lower left and upper right corners. */
void Model::computeCenterAndBoundingBox(Mesh& mesh)
{
    // Vertex iterator is an iterator which goes over all the vertices of the mesh:
    Mesh::VertexIter vertexIter;
    // This is used to store the geometrical position of the vertices of the mesh:
    Mesh::Point p, center(0,0,0);
    const float fm = std::numeric_limits<float>::max();
    Mesh::Point lowerLeft( fm, fm, fm);
    Mesh::Point upperRight( 0, 0,  0);
    // number of vertices in the mesh:
    int vNum = mesh.n_vertices();
    vertexIter = mesh.vertices_begin();
    lowerLeft = upperRight = mesh.point(vertexIter);
    // This is how to go over all the vertices in the mesh:
    for (vertexIter = mesh.vertices_begin(); vertexIter != mesh.vertices_end(); ++vertexIter)
    {
    // This is how to get the point associated with the vertex:
        p = mesh.point(vertexIter);
        center += p;
        for (int i = 0; i < 3; i++)
        {
            lowerLeft[i] = std::min(lowerLeft[i], p[i]);
            upperRight[i] = std::max(upperRight[i], p[i]);
        }
    }
    center /= (double) vNum;

    _objectCenter = center;
    _upperRight=upperRight;
    _lowerLeft=lowerLeft;
}

Mesh::Point  euclidian3D(Mesh::Point p1,Mesh::Point p2)
{
    Mesh::Point deltaP;
    for (int i=0; i < 3; i++)
    {
        deltaP[i] = (p1[i] - p2[i]);

    }

    //float a = glm::max(deltaP[0],deltaP[1]);
    //const float b =   glm::max(a,deltaP[3]);
   // std::cout<<a<<std::endl;
    //Mesh::Point delta(b,b,b);
    //return delta;
    return deltaP;
}

void Model::setRotationMatrix(int x, int y)
{
    float ratio = (float) (_width)/(float) _height;

    // convert pixel to be between 1 and -1:
    float pX = (2 * _currentX/ float(_width)) - 1;
    float pY = (-2 * _currentY/ float(_height)) + 1;
    float nX = (2 * x / float(_width)) - 1;
    float nY = (-2 * y / float(_height)) + 1;

    if (notInBall(nX,nY,ratio))
        return;


    glm::vec3 v1 = calculateV(-pX/ROTATE_LESS_COORD,-pY/ROTATE_LESS_COORD, RADIUS);
    glm::vec3 v2 = calculateV(-nX/ROTATE_LESS_COORD,-nY/ROTATE_LESS_COORD, RADIUS);


    float dotProduct = (float) glm::dot(v1, v2);
    if(!dotProduct){
        return;
    }
    float angle = acos(std::min(1.f,dotProduct));
    angle = glm::degrees(2.0f * angle);
    glm::vec3 rotationAxis = glm::cross(v1, v2);

    _rotateMat = glm::rotate(_rotateMat, angle, rotationAxis);

}

void Model::setTranlationMatrix(int x, int y)
{
    glm::mat4 temp1 =  _translateMat;
    Mesh::Point normalize = euclidian3D(_upperRight,_lowerLeft);
    _translateMat =  glm::translate(glm::vec3(2 * (x - _currentX)/(float)_width * (normalize[0]/TRANSLATE_LESS_COORD),
                                              -2 * (y - _currentY)/(float)_height * (normalize[1]/TRANSLATE_LESS_COORD) ,
                                              0.0)) * temp1;
}

void Model::setScale(int x, int y)
{
    int xDis = (_currentX - x) * (_currentX - x);
    int yDis = (_currentY - y) * (_currentY - y);
    float scale = std::sqrt(xDis + yDis)/100;
    if (y < _currentY || x < _currentX)
        scale = -scale;

    _scaleMat *= glm::scale(glm::vec3(1-scale));
}



glm::vec3 calculateV(float x, float y, float r)
{
    glm::vec3 v ;
    float temp = pow(x,2) + pow(y,2);
    if(temp > pow(r,2))
    {
        v = glm::vec3(x,y,0.0f);
        v = glm::normalize(v);
    }
    else
        v = glm::vec3(x,y, sqrt(r*r - temp));


    return v;
}

void Model::getClick(bool isclicked,int x,int y)
{
    _isclicked = isclicked;
    _currentX = x;
    _currentY = y;
}

bool notInBall(float x, float y, float ratio)
{

    if (ratio > 1)
        return (pow(x,2) + pow(y,2))/pow(ratio,2) > pow(RADIUS,2);
    else
        return (pow(x,2) + pow(y,2)* pow(ratio,2)) > pow(RADIUS,2);


}

/* Function for Part 2 **************** */
 void Model::changeEstimateMode()
{
    if(_estimationMode){ _estimationMode = VERTEX_NORMAL_EST; }
    else{  _estimationMode = FACES_NORMAL_EST;}

}

void Model::setShading(GLint mode)
{
    _shadingMode = mode;

}

void Model::shinessUP()
{
    _specExp += SHINESS_INCREMENT;
    _specExp = _specExp > MAX_SHINESS ? MAX_SHINESS:_specExp;

}

void Model::shinessDown()
{
    _specExp -= SHINESS_INCREMENT;
    _specExp = _specExp < MIN_SHINESS ? MIN_SHINESS:_specExp;

}


/**functions for part 3*/
void Model::changeTexturingMode()
{
    _texturingMode++ ;
    _texturingMode = _texturingMode > 4? 0:_texturingMode;
}

void Model::increaseTexturing()
{
    _texturingValue += TEXTURE_INCREMENT;
    _texturingValue = _texturingValue > MAX_TEXTURE? MAX_TEXTURE:_texturingValue;
}

void Model::decreaseTexturing()
{
    _texturingValue -= TEXTURE_INCREMENT;
    _texturingValue = _texturingValue < MIN_TEXTURE? MIN_TEXTURE:_texturingValue;
}


void Model::increaeTurbulence()
{
    _turbulenceValue += TURBULENCE_INCREMENT;
    _turbulenceValue = _turbulenceValue > MAX_TURBULENCE? MAX_TURBULENCE:_turbulenceValue;
}
void Model::decreaseTurbulence()
{
    _turbulenceValue -= TURBULENCE_INCREMENT;
    _turbulenceValue = _turbulenceValue < MIN_TURBULENCE? MIN_TURBULENCE:_turbulenceValue;

}



