//
// Created by mutazmanaa on 3/4/17.
//

#include "object.h"


Object::Object() : _ambient(COLOR_BLACK), _diffuse(COLOR_BLACK), _transparency(COLOR_BLACK), _specular(COLOR_BLACK),
                   _index(DEFAULT_INDEX), _diffuseTexture(NULL), _reflection(COLOR_BLACK), _shining(DEFAULT_SHINING){}




// Original methods (i left them because the ex3 scene uses them  //
// and the ex mentioned that we must work with your ex3 file)     //
Color3d& Object::diffuse()
{
    return _diffuse;
}
Color3d& Object::specular()
{
    return _specular;
}
Color3d& Object::ambient()
{
    return _ambient;
}
Color3d& Object::reflection()
{
    return _reflection;
}
Color3d& Object::transparency()
{
    return _transparency;
}
double& Object::index()
{
    return _index;
}
double& Object::shining()
{
    return _shining;
}

void Object::set_texture_map(BImage* image) {
    this->_diffuseTexture = image;
}

// Const get methods (to let the compiler do the optimizations we want it to do)  //
const Color3d&  Object::getDiffuse()      const
{
    return _diffuse;
}
const Color3d&  Object::getSpecular()     const
{
    return _specular;
}
const Color3d&  Object::getAmbient()      const
{
    return _ambient;
}
const Color3d&  Object::getReflection()   const
{
    return _reflection;
}
const Color3d&  Object::getTransparency() const
{
    return _transparency;
}
const double    Object::getIndex()        const
{
    return _index;
}
const double    Object::getShining()      const
{
    return _shining;
}