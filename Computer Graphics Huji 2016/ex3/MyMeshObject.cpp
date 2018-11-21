//
// Created by mutazmanaa on 3/4/17.
//

#include "MyMeshObject.h"

#include <vector>



// Constructor - create a MyMeshObject from the given mesh  //
MyMeshObject::MyMeshObject(MyMesh & mesh) : _mesh(mesh)
{
		
		this->calculateBoundingSphere(); 
		
		//as ex 2 
			  _mesh.request_face_normals();
		  _mesh.request_vertex_normals();
		   _mesh.update_normals();
		   
			MyMesh::FaceVertexIter fvIter;
       	 	MyMesh::FaceIter f_it, f_end(_mesh.faces_end());
       	 	
       	 	
			for (f_it = _mesh.faces_begin(); f_it != f_end; ++f_it)
		    {
		    	
		        MyMesh::FaceHandle faceH = f_it.handle();
		        vector <Point3d> vertices;
		        vector<Point2d> textices;


		        // iterate on the face vertices and adding them to the vertices array
		        for (fvIter = _mesh.fv_iter(faceH); fvIter; ++fvIter)
		        {
					
		            MyMesh::Point p = _mesh.point(fvIter.handle());
		            vertices.push_back(p);
		           if(_mesh.has_vertex_texcoords2D())
		            {
		            	
		                MyMesh::TexCoord2D TexCoords = _mesh.texcoord2D(fvIter.handle());
					    textices.push_back(TexCoords);
					}  

		        }

		        Vector3d  faceNormal = _mesh.calc_face_normal(faceH);
		        
		        
		        if (_mesh.has_vertex_texcoords2D())
		        {
		            if (_mesh.has_face_normals())
		            {
		                
		                _polygons.push_back(new Polygon(vertices, textices, faceNormal));
		            }
		            else
		            {
		                _polygons.push_back(new Polygon(vertices, textices));
		            }
		        }
		        else
		        {
		            if (_mesh.has_face_normals())
		            {
		                
		                 _polygons.push_back(new Polygon(vertices, faceNormal));
		            }   
		            else
		            {
		                _polygons.push_back(new Polygon(vertices));
		            }
		         }
		           
		   		_mesh.release_vertex_normals();
		    	_mesh.release_face_normals();
		    }
		
}

// Destructor - free a MyMeshObject //
MyMeshObject::~MyMeshObject(){}




int MyMeshObject::intersect(IN Ray& ray, IN double tMax, OUT double& t, OUT Point3d& P, OUT Vector3d& N, OUT Color3d& texColor)
{
  
    if(_boundingSphere->intersect(ray, tMax, t, P, N, texColor))
    {
		
		for (size_t i = 1; i < _polygons.size(); ++i) 
		{
		       
		        if (_polygons[i]->intersect(ray, tMax, t, P, N, texColor)) 
		        {
		          
		            return 1;
		        }
		}
		
    }
	
	
    return 0;



}

void MyMeshObject::set_texture_map(BImage* image){}

//get center as I do in ex2
void  MyMeshObject::calculateBoundingSphere()
{

    // Vertex iterator is an iterator which goes over all the vertices of the mesh:
    MyMesh::VertexIter vertexIter;
    // This is used to store the geometrical position of the vertices of the mesh:
    Point3d  p(0,0,0);
    double sphereRadius = 0;
    //const float fm = std::numeric_limits<float>::max();
    //Mesh::Point lowerLeft( fm, fm, fm);
    //Mesh::Point upperRight( 0, 0,  0);
    // number of vertices in the mesh:
    int vNum = _mesh.n_vertices();
   
    //lowerLeft = upperRight = mesh.point(vertexIter);
    // This is how to go over all the vertices in the mesh:
    for (vertexIter = _mesh.vertices_begin(); vertexIter != _mesh.vertices_end(); ++vertexIter)
    {
        // This is how to get the point associated with the vertex:
        p = _mesh.point(vertexIter);
        _center += p;
        sphereRadius = std::max(sphereRadius, p.length());

    }
    _center /= (double) vNum;
    _boundingSphere = new Sphere(_center, sphereRadius);
    
  

}
