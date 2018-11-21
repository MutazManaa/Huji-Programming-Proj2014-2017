200750453 mutazmanaa




*****************
*     EX2       *
*****************
This porject contains:
Readme.txt: this file
model.h: a model header for the mesh
model.cpp:a model cpp for implementing the function for the mesh
sphere.h: a header for the sphere drawn around the mesh.
shphere.cpp: the sphere cpp for implementing the sphere.
SimpleShader.frag: the fragment shader for the mesh.
SimpleShader.vert: the vertex shader for the mesh.
SphereShader.frag: the fragment shader for the sphere.
SphereShader.vert: the vertex shader for the sphere.
ex0.h: header file for the main driver to run the program.
ex0.cpp: a cpp file for the main driver and the functionality to run the program.
bimage.h: an API for uses texture image propereties.
bimage.cpp: an implemnting for the function handling the texture images.
Makefile: compile the whole files realted to the project.
other shaders IO:to handle the shaders issues.




*****************
* impementation *
*****************
I choose to implement the sphere and the mesh in different models, with seperated shaders.
The sphere implemented by the simple coordinates by rotate the vertex coordinates.
every vertex has 4 coordinates (x,y,z,1.f) ,then, 12 coordinates per one triangle.
I choose the an array for floats by length 4 * number of vertices. and starting with angle zero to 2*pi
for completing the triangles.
In related to the Mesh , I have used an floats array for the vertices possition, vertices normals, and faces normals,
12 float cells per one vertex, and buffering the possitions, normals, and the textures one time by specific function because
it so expenssive.
I load the points of the mesh and centered it between the cube {1,-1} per coordinate, and use the first distance metric to normalize
the coordinates between 1 and -1, I don't normalize/scaled the lower left point to -1 and upper right to 1 because the radius of the sphere
is 0.8 and if I scaled the LL and UR points to -1 and 1 it related that the  mesh will be outside the sphere, then it good to only center them around zero and
 normalize the between -1 and 1 witout scale to 1 and -1.
 so I don't use the transformations: 2 * (P - LL)/norma - 1, but I use the (P - objectCenter)/norma and get points between -1 and 1.
 The other function like scale, translate, rotation and etc implemented as learned in the class,I use lookAt matrix and the some projection matrix to
 view the mesh.
 lighting, shading and texturing implemented by get help from class and this referencees:
 https://www.opengl.org/sdk/docs/tutorials/ClockworkCoders/lighting.php
 http://www.tomdalling.com/blog/modern-opengl/07-more-lighting-ambient-specular-attenuation-gamma/
 https://open.gl/textures
http://www.opengl-tutorial.org/beginners-tutorials/tutorial-5-a-textured-cube/

*****************
*  description  *
*****************
This project show Meshes at the screen with functions of rotation, translation, zoom in and out, and two projection modes
orthogonal and prespective, and some features like the coluoring the trinagles or not.
We show also the Mesh drawn with vertices normals estimation or face normals estimation in addition to the simpple way with
the position.
We alse do some lighting and shading effect, and add a texturing effect by some images shown on the mesh.

USAGE: ./ex2 meshesh/SOME_MESH_NAME

