//
// Created by mutazmanaa on 3/6/17.
//



#include "scene.h"
#include <cstdlib>
#include <typeinfo>
#include <math.h>


int Scene::recDepth = 0;

Scene::Scene() : _cutoffAngle(0.0), _numberOfRefRays(1.0){}

Scene::Scene(Color3d & color, AmbientLight & light, double cutoffAngle) :_ambientLight(light),
			 _background(color), _cutoffAngle(cutoffAngle),_numberOfRefRays(1) {}

Color3d Scene::trace_ray(Ray ray, double vis) const 
{
	double tMax;
	Object* objectIntersected;
	double t = INF;
	Point3d P;
	Vector3d N;
	Color3d texColor = COLOR_BLACK;
	recDepth++;

	if (vis < 0.2 || !findNearestObject(ray, &objectIntersected, t, P, N, texColor) || recDepth >= 5) 
	{
		recDepth--;
		return _background;
	}

	N = N.normalize();

	Color3d ambient = ((objectIntersected->getAmbient()) * (this->_ambientLight._color));

	Color3d Surface_Color = ambient + calcReflection(ray, P, N, *objectIntersected, vis) + calcRefraction(ray, P, N, *objectIntersected, vis);
	
	Vector3d tmoVec;
	Object* tmoObj;
	Point3d tmoP;
	Color3d tmoClr;
	
	Color3d diffuse(0.0);
	Color3d specular(0.0);
	Color3d lightEffect(0.0);
	double tmoLdist;

	for (size_t i = 0; i < _lights.size(); ++i) {
		Vector3d toLight = ((_lights[i])->_position - P).normalize();
		bool foundObj = (findNearestObject(Ray(P, toLight), &tmoObj,tmoLdist, tmoP, tmoVec, tmoClr));

		if ((foundObj == true && (tmoLdist < ((_lights[i])->_position - P).norm()))
				|| ((toLight| N)) < 0) {
			continue;
		}

			diffuse = ((objectIntersected->getDiffuse()) * ((_lights[i])->_color)) * (toLight | N);


		Vector3d dir = ray.D();
		Vector3d refl = (N * 2 * (toLight | N) - toLight).normalize();
		double shineRef = pow((refl | dir), objectIntersected->getShining());
		specular = ((objectIntersected->getSpecular()) * ((_lights[i])->_color))* shineRef;
		lightEffect = lightEffect + diffuse + specular;

	}

	Color3d addToLight(COLOR_BLACK);
	if (_sphereLights.size() == 0)
	{
		lightEffect += addToLight;
	}else
	{

		for (size_t i = 0; i < _lights.size(); ++i) 
		{
			Point3d center = (_sphereLights[i])->_position;
			double radius = (_sphereLights[i])->_radius;

			Vector3d direction = center - P;

			for (int i = 0; i < 5; i++) 
			{
				double xDir, yDir, zDir;
				xDir = double(rand()) / (RAND_MAX);
				yDir = double(rand()) / (RAND_MAX);
				zDir = double(rand()) / (RAND_MAX);

				Vector3d offset = (radius / pow(3, 0.5))* Vector3d(xDir, yDir, zDir);
				Ray toLight1(P, direction + offset);


				Point3d tmoP1;
				Object* tmoObj1;
				Vector3d tmoVec1;
				Color3d tmoClr1;
				double tmoLdist1;

				Color3d diffuse(0.0);
				Color3d specular(0.0);

				bool foundObj = findNearestObject(toLight1, &tmoObj1,tmoLdist1, tmoP1, tmoVec1, tmoClr1);
				if (foundObj == true)
				{
					if ((toLight1.D() | N) < 0 || tmoLdist1 < toLight1.D().norm()) 
					{
						continue;
					}
				}
				diffuse = objectIntersected->getDiffuse() * (_sphereLights[i])->_color * (N | toLight1.D());

				Vector3d dir = ray.D();
				Vector3d refl = (N * 2 * (toLight1.D() | N) - toLight1.D()).normalize();
				double shineRef = pow((refl | dir), objectIntersected->getShining());
				specular = ((objectIntersected->getSpecular()) * ((_sphereLights[i])->_color)) * shineRef;
				addToLight += specular + diffuse;
				
			}
			addToLight =  (addToLight / 5.);
		}
	}
	
	
	lightEffect += addToLight;
	recDepth--;
	return lightEffect + Surface_Color;
}



void Scene::add_object(Object* obj) {
	_objects.push_back(obj);
}

void Scene::add_light(PointLight* light) {
	_lights.push_back(light);
}

void Scene::add_light(SphereLight* light) {
	_sphereLights.push_back(light);
}

Ray Scene::perturbateRay(const Ray& r) const {

	if (_numberOfRefRays > 1) {
		double rand1 = (double) rand() / RAND_MAX;
		double rand2 = (double) rand() / RAND_MAX;
		double teta = rand1 * 2 * PI;
		double rand = rand2 * sin(_cutoffAngle / 2);
		Vector3d v;
		Vector3d u;
		if ((r.D() % Vector3d(1, 0, 0)).norm() > EPS) {
			v = (r.D() % (r.D() + Vector3d(1, 0, 0))).normalize();
		} else {
			v = (r.D() % r.D() + Vector3d(0, 1, 0)).normalize();
		}
		u = (r.D() % v).normalize();
		
		Vector3d randDir = r.D() + rand * cos(teta) * v
				+ rand * sin(teta) * u; 

		return Ray(r.O(), randDir);
	}
	return r;
}


bool Scene::findNearestObject(Ray ray, Object** object, double& t, Point3d& P,
		Vector3d& N, Color3d& texColor) const {
	bool found = false;
	double tMax = INF;
	t = INF;

	double tTemp;

	for (size_t i = 0; i < _objects.size();++i) {

		double tTemp;
		Point3d pTemp;
		Vector3d nTemp;
		Color3d texColorTemp;

		if ((_objects[i])->intersect(ray, tMax, tTemp, pTemp, nTemp, texColorTemp)) {
			if (tTemp < t && tTemp > EPS) {
				found = true;
				t = tTemp;
				P = pTemp;
				N = nTemp;
				texColor = texColorTemp;
				*object = _objects[i];
			}
		}
	}

	return found;

}

Color3d Scene::calcReflection(const Ray& ray, const Point3d& P,const Vector3d& N,
 const Object& object, double vis,bool isCritical) const 
 {
	vis = vis * object.getReflection().max();
	if (object.getReflection().max() < EPS) {
		return COLOR_BLACK;
	}

	Vector3d P_O = (P - ray.O()).normalize();
	Vector3d refl = P_O - (2 * (N | P_O)) * N;
	Ray reflection(P, refl, ray.rayHitObg());

	Color3d temp = COLOR_BLACK;
	for (int i = 0; i < _numberOfRefRays; i++) {
		temp +=(object.getReflection() * trace_ray(perturbateRay(reflection), vis));
	}

	return temp / double(_numberOfRefRays);

}

Color3d Scene::calcRefraction(const Ray& ray, const Point3d& P,
		const Vector3d& N, const Object& object, double vis) const 
		{

	

	if (object.getTransparency().max() < EPS) {
		return COLOR_BLACK;
	}

	double refractionIndex = ray.rayHitObg() ? object.getIndex() : 1.0 / object.getIndex();
	double sign = ray.rayHitObg() ? -1 : 1;

	Vector3d O_P = (ray.O() - P).normalize();
	double OP_DOT_N = (O_P| (sign * N));

	Vector3d direction = (refractionIndex * OP_DOT_N
			- sqrt(1 - refractionIndex * refractionIndex * (1 - OP_DOT_N * OP_DOT_N))) * (N * sign)
			- refractionIndex * O_P;
	direction = direction.normalize();

	Ray refraction(P, direction, !ray.rayHitObg());

	vis = vis * object.getTransparency().max();
	if ((direction | (N * (-1))) < EPS) {
		return (trace_ray(refraction, vis));
	} else {

		Color3d tmp = COLOR_BLACK;
		for (int i = 0; i < _numberOfRefRays; i++) 
		{
			tmp+= (object.getTransparency() * trace_ray(refraction, vis));
		}
		
		return tmp / double(_numberOfRefRays);
	}
}
