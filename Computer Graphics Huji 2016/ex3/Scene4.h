

//
//  Scene4.h
//  cg-projects
//
//

#ifndef cg_projects_Scene5_h
#define cg_projects_Scene5_h


struct Scene4 : public Scene
{

    Scene4()
            : Scene()
    {
        defineGeometry();
        defineLights();
    }

    void defineLights()
    {
        Scene & scene = *this;
        Point3d pos(-2,5,1);
        Color3d color(1,1,1);
        PointLight  * p = new PointLight(pos,color);
        scene.add_light(p);

	

    	Point3d pos1(10,20,30);
        Color3d color1(1,1,1);
        PointLight  * p1 = new PointLight(pos1,color1);
        scene.add_light(p1);


        Point3d pos2(5,20,30);
        Color3d color2(0.5,0.5,0.5);
        PointLight  * p2= new PointLight(pos2,color2);
        scene.add_light(p2);
       

    }

    void defineGeometry()
    {
        Scene & scene = *this;
#if !WITHOUT_TEXTURES
        BImage * mickey = new BImage("textures/mickey.bmp");
#endif
        /* define some colors */
        Color3d white(1.0);
        Color3d black(0.0);
        Color3d red(1,0,0.0);
        Color3d green(0,1.0,0.0);
        Color3d blue(0,0,1.0);
        Color3d iron( 0.30, 0.30, 0.30);

        scene.backgroundColor() = Color3d(0.5,0.5,0.8);

       Point3d center3(20,20,20);
        double radius = 3;
        Vector3d scale(1.5,2.5,3.5);
        Ellipsoid * sp3 = new Ellipsoid(center3,radius,scale);
        sp3->diffuse() = iron;
        sp3->reflection() = white*0.5;
        sp3->specular() = white * 0.5;
        sp3->shining() = 40;
        scene.add_object(sp3);

        Point3d center( 5, 3, -3.2);
        double radius1 = 5;
        Sphere* head = new Sphere(center,radius1);
        head->ambient() =  Color3d(0.7, 0.7, 0.7);
        head->diffuse() = Color3d(0.1, 0.5, 0.8);
        head->reflection() = white*0.4;
        head->specular() = white;
        head->shining() = 10;
        scene.add_object(head);


        Point3d center1( -7, 3, -3.5);
        double radius2 = 3.5;
        Sphere* ear1 = new Sphere(center1,radius2);
        head->ambient() =  Color3d(0.6, 0.6, 0.6);
        ear1->diffuse() = Color3d( 1.0, 0.0, 0.25);
        ear1->reflection() = Color3d(0.3,0.3,0.3);
        ear1->specular() = white;
        ear1->shining() = 6;
        scene.add_object(ear1);



        Point3d center4( -1, 9, -2.2);
        double radius4 = 2;
        Sphere* nose = new Sphere(center4,radius4);
        head->ambient() =  Color3d(0.2,0.2,0.2);
        nose->diffuse() = Color3d(0.0, 1.0, 0.25);
        nose->reflection() = Color3d(0.3,0.3,0.3);
        nose->specular() =Color3d(0.0,1.0,0.0);
        nose->shining() = 30;
        scene.add_object(nose);

        //create a plane
        vector<Point3d> plane(4);
        vector<Point2d> plane_uv(4);
        double x = 100;
        double z = -4;
        plane[0] = Point3d(-x,z,-x);
        plane[1] = Point3d(-x,z,x);
        plane[2] = Point3d(x,z,x);
        plane[3] = Point3d(x,z,-x);
        plane_uv[0] = Point2d(0,0);
        plane_uv[1] = Point2d(0,1);
        plane_uv[2] = Point2d(1,1);
        plane_uv[3] = Point2d(1,0);
        Polygon * poly = new Polygon(plane,plane_uv);
        poly->diffuse() = ((blue + red) * 0.5 + white * 0.5) * 0.2;;
        poly->reflection() = white * 0.4;
#if !WITHOUT_TEXTURES
        poly->set_texture_map(mickey);
#endif
        scene.add_object(poly);
    }

    virtual void setDefaultCamera(Camera& camera) const
    {
        Point3d pos(-1,6,25);
        double fov_h = 50 / 180.0 * M_PI ;
        Point3d coi(0,0,-0);
        Vector3d up(0,1,0) ;
        //		Point3d pos(0,8,35);
        //		double fov_h = 30 / 180.0 * M_PI;
        //		Point3d coi(0,0,-0);
        //		Vector3d up(0,1,0) ;
        camera = Camera(pos,coi,up,fov_h);
    }

    virtual ~Scene4() {

    }

};


#endif
