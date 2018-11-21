//
//  lights.h
//  cg-projects
//
//  Created by HUJI Computer Graphics course staff, 2012-2013.
//  Purpose : Declerae and implement classes representing ambient and
//            point light sources.
//

#ifndef __LIGHTS__
#define __LIGHTS__


//////////////////////////////
// Project Includes         //
//////////////////////////////


#include "sphere.h"
#include "general.h"
#include "MyVecs.h"
#include <vector>
//////////////////////////////
// Class Decleration        //
//////////////////////////////

class AmbientLight 
{
public:
  AmbientLight(Color3d  color = COLOR_BLACK):_color(color){}
public:
  Color3d _color;

};


class PointLight 
{
public:
  PointLight(Point3d position = POINT_ORIGIN,Color3d color = COLOR_BLACK):
    _position(position),_color(color){}

  Point3d _position;
  Color3d _color;
};

class SphereLight
{
public:

    SphereLight(Point3d position = POINT_ORIGIN, double radius = 1.0, Color3d color = COLOR_BLACK) : 
    _s(position, radius), _position(position), _radius(radius), _color(color)
    {
	  
	}


public:
    Point3d _position;
    double _radius;
    Color3d _color;
    Sphere _s;
};


#endif
