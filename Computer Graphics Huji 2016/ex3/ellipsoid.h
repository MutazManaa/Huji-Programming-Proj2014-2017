//
// Created by mutazmanaa on 3/6/17.
//

#ifndef MUTAZMANAA_ELLIPSOID_H
#define MUTAZMANAA_ELLIPSOID_H


//////////////////////////////
// Project Includes         //
//////////////////////////////

#include "general.h"
#include "object.h"
#include "sphere.h"



//////////////////////////////
// Class Decleration        //
//////////////////////////////


class Sphere;

class Ellipsoid : public Object
{
public:
    // Constructor - create a default sphere  //
    Ellipsoid();

    // Constructor - create a sphere with the given parameters  //
    Ellipsoid(Point3d C, double r, Vector3d  tranform_sphere);

    virtual ~Ellipsoid();
    // Ray Sphere intersection //
    virtual int intersect(IN Ray& ray, IN double tMax, OUT double& t, OUT Point3d& P, OUT Vector3d& N, OUT Color3d& texColor);

    Color3d textureDiffuse(const Point3d& P) const;

private:

    Point3d   _C; // The sphere's center  //
    double    _r; // The sphere's radius  ///
    Vector3d  _tranform_sphere;
    Vector3d  _inverse_tranforme_spher;
    Sphere* _boundingSphere;


} ;

#endif //MUTAZMANAA_ELLIPSOID_H
