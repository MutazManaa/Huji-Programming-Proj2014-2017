
//
// Created by mutazmanaa on 3/8/17.
//
#include "general.h"
#include "object.h"
#include "cmath"

#include <cmath>


class Cylinder : public Object
{
public:
// Constructor - create a default sphere  //
Cylinder() : _buttom(0.0,0.0,0.0), _top(0.0,0.0,1.0),_r(1){}

// Constructor - create a sphere with the given parameters  //
Cylinder(Point3d& buttom, Point3d& top, double& r) : _buttom(buttom), _top(top), _r(r) {}


// Ray Sphere intersection //
virtual int intersect(IN Ray& ray, IN double tMax, OUT double& t, OUT Point3d& P, OUT Vector3d& N, OUT Color3d& texColor)
{

    Point3d O = Point3d(0.0,0.0,0.0) + (ray.O() - _buttom);
    Vector3d D = ray.D();

    // Quadratic formula
    double a = D[1]*D[1] + D[2]*D[2];
    double b = 2*O[1]*D[1] + 2*O[2]*D[2];
    double c = O[1]*O[1] + O[1]*O[2] - _r*_r;
    double d = b*b - 4*a*c;


// No solutions
    if(d < 0)
    {
        return  0;
    }


    double sqrtD = sqrt(d);
    double t1 = (-b - sqrtD)/(2*a);
    double t2 = (-b + sqrtD)/(2*a);


    double L = (_top - _buttom).length();
    Point3d atT1 = O + t1*D, atT2 = O + t2*D;

    double a1 = (atT1 - _buttom).length();
    double b1 = (atT2 - _buttom).length();

    bool t1Valid = t1 >= EPS && t1 <  tMax && a1 >= 0 && a1 <= L;
    bool t2Valid = t2 >= EPS && t2 <  tMax && b1 >= 0 && b1 <= L;


    if(t1Valid && t2Valid)
    {
       t =  min(t1,t2);

    }else if(t1Valid)
    {
        t = t1;
    }else if(t2Valid)
    {
        t = t2;
    }else
    {
        return 0;
    }


    // Compute the normal

    P = O + t*D ;

    Vector3d temp = P - _buttom;
    Vector3d axis = (_top - _buttom).normalize();
    N = temp - (temp | axis) * axis;
    N.normalize();


    return 1;

}

private:

    Point3d    _buttom;
    Point3d     _top;
    //Point3d   _C; // The sphere's center  //
    double    _r; // The sphere's radius  ///

} ;

