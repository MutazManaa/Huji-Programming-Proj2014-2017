//
// Created by mutazmanaa on 3/3/17.
//

#include "polygon.h"
#include "triangle.h"
#include "ray.h"

#include <glm/glm.hpp>




// Constructor - create a default polygon //
Polygon::Polygon(){};


// Constructor - Create a polygon from the given vertices //
Polygon::Polygon(vector<Point3d> & vertices): _vertices(vertices), _textured(false)
{

    triangulate();
    	
    _normal = ((_vertices[2] - _vertices[0]) % (_vertices[1] - _vertices[0])).normalized();
    	

}

// Constructor - Create a polygon from the given vertices, using the given normal //
Polygon::Polygon(vector<Point3d> & vertices, Vector3d & normal): _vertices(vertices), _normal(normal),_textured(false)
{
    triangulate();
    if (_normal == Vector3d(0.0, 0.0, 0.0)) _normal = ((_vertices[2] - _vertices[0]) % (_vertices[1] - _vertices[0])).normalized();

}

// Constructor - Create a polygon from the given vertices, using the given texture map coordinates //
Polygon::Polygon(vector<Point3d> & vertices, vector<Point2d> textices) : _vertices(vertices), _textices(textices), _textured(true)
{
    triangulate();
    _normal = ((_vertices[2] - _vertices[0]) % (_vertices[1] - _vertices[0])).normalized();
}

// Constructor - Create a polygon from the given vertices, using the given normal and texture map coordintes //
Polygon::Polygon(vector<Point3d> & vertices, vector<Point2d> textices, Vector3d & normal) : _vertices(vertices), _textured(true), _textices(textices),_normal(normal)
{
    triangulate();
    if (_normal == Vector3d(0.0, 0.0, 0.0)) _normal = ((_vertices[1] - _vertices[0]) % (_vertices[2] - _vertices[0])).normalized();
}

// Destructor - get rid of a polygon  //
Polygon::~Polygon()
{
    for (int i = 0; i < _triangles.size(); i++)
    {
        delete _triangles[i];
    }
}

int Polygon::intersect(IN Ray& ray, IN double tMax, OUT double& t,
                       OUT Point3d& P, OUT Vector3d& N, OUT Color3d& texColor) {
    
    
    double O_dot_N = (_vertices[0] - ray.O()) | _normal;
    double D_dot_N =  ray.D() | _normal;
    if (fabs(O_dot_N) < 0) {
        return 0;
    }
    
    double t1 = O_dot_N / D_dot_N ;
    
    if (t1 < 0 && t1 > tMax) {
        return 0;
    }
    
    Point3d P1 = ray(t1);
   
    Point2d texCord;
    vector<Triangle*>::iterator Iter = _triangles.begin();
    for (; Iter != _triangles.end(); ++Iter) {
        if ((*Iter)->isInside(P1, texCord)) {
          t = t1;
        
          P = P1;
          N = _normal;
          return 1;
    }  
  
    }
    return 0;
}



void Polygon::triangulate() {
    
    for (size_t i = 1; i < _vertices.size()-1; ++i) {

        if (_textured) {
            _triangles.push_back(new Triangle(_vertices[0],_vertices[i], _vertices[i+1], _textices[0],_textices[i], _textices[i+1]));
        } else {
            _triangles.push_back(new Triangle(_vertices[0],_vertices[i], _vertices[i+1]));
        }
    }

   } 
