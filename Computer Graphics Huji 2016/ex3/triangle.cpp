//
// Created by mutazmanaa on 3/4/17.
//

#include "triangle.h"

// Constructor - create a triangle from the given points  //
Triangle::Triangle(Point3d p0, Point3d p1, Point3d p2):_p0(p0), _u(p1 - p0), _v(p2 - p0), _d((_u % _v).norm()), _textured(false){

    _uu = (_u | _u);
    _uv = (_u | _v);
    _vv = (_v | _v);
}


Triangle::Triangle(Point3d p0, Point3d p1, Point3d p2, Point2d t0, Point2d t1, Point2d t2): _p0(p0), _u(p1 - p0), _v(p2 - p0), _d((_u % _v).norm()),
                                                                                            _t0(t0), _tu(t1), _tv(t2), _textured(true){

    _uu = (_u | _u);
    _uv = (_u | _v);
    _vv = (_v | _v);
}


Triangle::~Triangle(){}

bool Triangle::isInside(IN const Point3d& p, OUT Point2d& texCoord) const
{
	

	double upp0 = (_u | (p - _p0));
	double vpp0 = (_v | (p - _p0));

	double tmp = 1 / (_uu * _vv - _uv * _uv);
	double a = tmp * (_vv * upp0 - _uv * vpp0);
	double b = tmp * (_uu * vpp0 - _uv * upp0);
	double c = 1.0 - a - b;


    if((EPS <= a && a < 1) && (EPS <= b && b < 1) && (EPS <= c && c <= 1))
   
    {
		

        return 1;
    }

    return 0;

}
