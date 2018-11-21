//
// Created by mutazmanaa on 3/4/17.
//

#include "sphere.h"
#include <cmath>
#include <glm/glm.hpp>
#include "ray.h"


bool solveQuadratic(const float &a, const float &b, const float &c, float &x0, float &x1);

// Constructor - create a default sphere  //
Sphere::Sphere() : _C(0.0,0.0,0.0),_r(1.0){};


// Constructor - create a sphere with the given parameters  //
Sphere::Sphere(Point3d C, double r): _C(C), _r(r){};


// Ray Sphere intersection //
int Sphere::intersect(IN Ray& ray, IN double tMax, OUT double& t, OUT Point3d& P, OUT Vector3d& N, OUT Color3d& texColor)
{
    float t0, t1; // solutions for t if the ray intersects

    // analytic solution
    Vector3d L = ray.O() - _C; //check if must to normalize
    if(L.norm() - _r > tMax) return 0; //
    Vector3d dir = ray.D();
    float a =  (dir | dir);
    float b = 2 * (L | dir);
    float c = (L | L) - _r*_r;
    if (!solveQuadratic(a, b, c, t0, t1)) return 0;


    if (t0 > t1) std::swap(t0, t1);

    if (t0 < EPS) {
        t0 = t1; // if t0 is negative, let's use t1 instead
        if (t0 < 0) return 0; // both t0 and t1 are negative
    }

    t = t0;

    P = ray.O() + t * dir;
    N = (P - _C).normalize();

    return 1;
}


bool solveQuadratic(const float &a, const float &b, const float &c, float &x0, float &x1)
{
    float discr = b * b - 4 * a * c;
    if (discr < 0) return false;
    else if (discr >= 0 && discr <= EPS) x0 = x1 = - 0.5 * b / a;
    else {
        float q = (b > EPS) ?
                  -0.5 * (b + sqrt(discr)) :
                  -0.5 * (b - sqrt(discr));
        x0 = q / a;
        x1 = c / q;
    }
    if (x0 > x1) std::swap(x0, x1);

    return true;
}
