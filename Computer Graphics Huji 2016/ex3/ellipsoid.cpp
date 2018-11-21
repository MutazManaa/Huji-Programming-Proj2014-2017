//
// Created by mutazmanaa on 3/6/17.
//

#include "ellipsoid.h"


Ray& computeTranformedRay(Ray& ray, Vector3d& tranform, Point3d& c);



Ellipsoid::Ellipsoid() : _tranform_sphere(1.0,1.0,1.0), _inverse_tranforme_spher(1.0,1.0,1.0)
{
	 _boundingSphere = new Sphere();
}

Ellipsoid::Ellipsoid(Point3d C, double r, Vector3d  tranform_sphere):_C(C), _r(r), _tranform_sphere(tranform_sphere)
{
    _boundingSphere = new Sphere(_C, _r);
     _inverse_tranforme_spher = Vector3d(1.0/_tranform_sphere[0], 1.0/_tranform_sphere[1], 1.0/_tranform_sphere[2]);
}


Ellipsoid::~Ellipsoid()
{
   // delete  _boundingSphere;
}

int Ellipsoid::intersect(IN Ray& ray, IN double tMax, OUT double& t, OUT Point3d& P, OUT Vector3d& N, OUT Color3d& texColor)
{
   
     Ray tranformed_ray((ray.O() - _C) *  _inverse_tranforme_spher + _C, ray.D() *  _inverse_tranforme_spher);

    if(_boundingSphere->intersect(tranformed_ray,tMax, t, P, N, texColor))
    {

        P = (tranformed_ray(t) - _C) * _tranform_sphere + _C;

        N = ((P - _C) *  _inverse_tranforme_spher).normalize();

        return 1;

    }

    return 0;
}


