200750453 mutazmanaa

============================
     CG EX3 - RAY TRACING
============================


============================
This Project include
============================


-general.h
-ray.h
-MyVecs.h 
-bimage.h	
-bimage.cpp
-object.h
-object.cpp
-triangle.h
-triangle.cpp
-sphere.h
-sphere.cpp
-polygon.h	
-polygon.cpp
-MyMesh.h				
-MyVecs.h 				
-MyMeshObject.h
-MyMeshObject.cpp	
-ellipsoid.h		-
-ellipsoid.cpp
-cylinder.h
-cylinder.cpp
-camera.h	
-camera.cpp
-lights.h
-scene.h
-scene.cpp		
-Scene1.h			
-Scene2.h			
-Scene3.h			
-Scene4.h					
-SceneChooser.h						
-ex5.cpp	
-Makefile
-README.txt
==============================================

============
USAGE: ./ex5
============

==============
Description
==============
This project implement Ray tracer and support multi-rays to render the Scene.
in the project implemented parametric shapes - Sphere, Traingle,Ellipsoid and Cylinder.
Also implemented Mesh object and intersections these shapes.

I tried in the MyMeshObject class to claculate intersect with the bounding sphere before iterates 
over the face and verticces and to produce polygones that can reduse the time rendering if there
are not intersection but I didn't success and the program terminated out by long time of
proccessing.
Then I needed to implement it in the constructor and iterate over the faces and vertices and produce
Polygones(not for calculate center and radius of bounding sphere).
Maybe the reason is that wen need the polygon to represent the mesh in the scene.

I implement it without texturing but I implement all the function that get the textices 
and all the constructors that support texturing.. I changed Epsilons to 0 to get PAPER & SLAT
at polygines and spheres on the that add some partial texturing on the shapes and look more beautiful.

============
Preferences
============
Google.co.il
scratchPixel.com
Slides in the TA.












				
				
     
