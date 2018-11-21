//
//  ex0.cpp
//  cg-projects
//
//  Created by HUJI Computer Graphics course staff, 2013.
//


#include <GL/glew.h>
#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <GL/freeglut.h>
#endif

#include "ex0.h"
#include "Model.h"
#include "Sphere.h"
#include "ShaderIO.h"

/** Internal Definitions */

#define	WINDOW_SIZE         (600) // initial size of the window               //
#define	WINDOW_POS_X        (100) // initial X position of the window         //
#define	WINDOW_POS_Y        (100) // initial Y position of the window         //

#define RC_OK                 (0) // Everything went ok                       //
#define RC_INVALID_ARGUMENTS  (1) // Invalid arguments given to the program   //
#define RC_INPUT_ERROR        (2) // Invalid input to the program             //

#define	ARGUMENTS_PROGRAM     (0) // program name position on argv            //
#define	ARGUMENTS_INPUTFILE   (1) // given input file position on argv        //
#define	ARGUMENTS_REQUIRED    (2) // number of required arguments             //

/** Key definitions */

/**part A**/
#define KEY_ANIMATE         ('a') // Key used to start animation              //
#define KEY_ESC            ('\e') // Key used to terminate the program - ESC  //
#define KEY_QUIT            ('q') // Key used to terminate the program        //
#define KEY_RESET           ('r') // Key used to reset the applied TX's	      //
#define KEY_RELOAD          ('l') // Key used to reload the shaders 	      //
#define KEY_WIREFRAME       ('w') // Key used to toggle wireframe rendering   //
#define KEY_PROJECTION      ('p') // key used to toggle between perspective and orthogonal//

/**part B*/
#define KEY_NORMAL_EST      ('n') // Switch between normal estimation modes (i.e. per vertex / per face)//
#define KEY_PHONG           ('1') // Use the full Phong model//
#define KEY_GOURAUD_APPRO   ('2') // Use the Gouraud approximation//
#define KEY_NORMAL_ASSIGN   ('3') // Assign pixel colors as defined in the previous exercis//
#define KEY_SHINEES_UP      ('=') // Increase the shininess coefficient up to 2000//
#define KEY_SHINEES_DOWN    ('-') //Decrease the shininess coefficient down to 0//

/**part C*/
#define KEY_TEXTURING_MODE          ('t')// Switch between texturing modes //
#define KEY_DECREASE_TEXTURING      ('s')//Decrease the scaling of the texture coordinates//
#define KEY_INCREASE_TEXTURING      ('d')// Increase the scaling of the texture coordinates//
#define KEY_DECREASE_TURBULENCE     ('f')//Decrease turbulence magnitude//
#define KEY_INCREASE_TURBULENCE     ('g')//ncrease turbulence magnitude//


/** mouse clicks*/
#define LEFT_CLICK 1
#define MIDDLE_CLICK 0
#define RIGHT_CLICK 2

int mouseClick;

/** display callback */
void display(void);

/** window reshape callback  */
void windowResize(int width, int height);

/** keyboard callback  */
void keyboard(unsigned char key, int x, int y);

/** mouse click callback */
void mouse(int button, int state, int x, int y) ;

/** mouse dragging callback  */
void motion(int x, int y) ;

/** timer callback */
void timer(int value) ;

/** Global variables */

int     g_nFPS = 0, g_nFrames = 0;              // FPS and FPS Counter
int     g_dwLastFPS = 0;                        // Last FPS Check Time
bool    g_startAnimation = false;
bool    g_duringAnimation = false;

// A global variable for our model (a better practice would be to use a singletone that holds all model):
Model _model;
Sphere _sphere;

float _heightRatio = 1, _weightRatio = 1;

/** main function */
int main(int argc, char* argv[])
{
    std::cout << "Starting ex0..." << std::endl;

    if(argc != 2)
    {
        return -1;
    }
    _model = Model(argv[1]);

    // Initialize GLUT
    glutInit(&argc, argv) ;
#ifdef __APPLE__
    glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGBA | GLUT_DEPTH | GLUT_3_2_CORE_PROFILE) ;
#else
    glutInitContextVersion(3, 3);
    glutInitContextFlags(GLUT_FORWARD_COMPATIBLE | GLUT_DEBUG);
    glutInitContextProfile(GLUT_CORE_PROFILE);
    glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGBA | GLUT_DEPTH);
#endif
    glutInitWindowSize(WINDOW_SIZE, WINDOW_SIZE);
    glutInitWindowPosition(WINDOW_POS_X, WINDOW_POS_Y);
    glutCreateWindow("CG Ex2");

    // Initialize GLEW
    glewExperimental = GL_TRUE;
    int glewStatus = glewInit();
    if (glewStatus != GLEW_OK) {
        std::cerr << "Unable to initialize GLEW ... exiting" << std::endl;
        exit(1);
    }

#ifdef __APPLE__
    GLint sync = 1;
    CGLSetParameter(CGLGetCurrentContext(), kCGLCPSwapInterval, &sync);
#endif

    // Set callback functions:
    glutDisplayFunc(display) ;
    glutReshapeFunc(windowResize) ;
    glutKeyboardFunc(keyboard);
    glutMouseFunc(mouse);
    glutMotionFunc(motion);
    glutTimerFunc(100, timer, 0);   // uint millis int value

    // Init anything that can be done once and for all:
    _model.init();
    _model.initBuffers();
    _sphere.init();

    glEnable(GL_DEPTH_TEST);

    // Set clear color to black:
    glClearColor(0.0, 0.0, 0.0, 0.0);

    // Start events/drawing loop
    glutMainLoop();

    return 0;
}

void display(void)
{
    // Clear the screen buffer
    glClear(GL_COLOR_BUFFER_BIT  | GL_DEPTH_BUFFER_BIT);// to check

    // Let the model to draw itself...
    _sphere.draw();
    _model.draw();

    // Swap those buffers so someone will actually see the results... //
    glutSwapBuffers();
}

// This method is called when the window is resized
void windowResize(int w, int h)
{
    _weightRatio = float(WINDOW_SIZE)/w;
    _heightRatio = float(WINDOW_SIZE)/h;

    // Update model to fit the new resolution
    _model.resize(w, h);
    _sphere.resize(w,h);

    // set the new viewport //
    glViewport(0, 0, w, h);

    // Refresh the display //
    glutPostRedisplay();
}

/********************************************************************
 * Function  :	keyboard
 * Arguments :	key : the key that was pressed
 *              x   : x value of the current mouse location
 *              y   : y value of the current mouse location
 * Returns   : n/a
 * Throws    : n/a
 *
 * Purpose   : This function handles all the keyboard input from the user.
 *             It supports terminating the application when the KEY_QUIT is pressed.
 *
 \******************************************************************/
void keyboard(unsigned char key, int x, int y)
{
    unsigned int lower_key = tolower(key);

    switch(lower_key)
    {
        case KEY_RESET:
            _model.init();
            _model.initBuffers();
            glutPostRedisplay();
            break;
        case KEY_RELOAD:
            // Reload the shading programs of the object
            // For use in a future exercise
            break;
        case KEY_WIREFRAME:
            _model.changePolygonMod();
            glutPostRedisplay();
            break;
        case KEY_ANIMATE:
            if (!g_duringAnimation) {
                g_startAnimation = true;
            }
            break;
        case KEY_QUIT:
        case KEY_ESC:
            // Terminate the program:
            exit(RC_OK);
            break;

        case KEY_PROJECTION:
            _model.changeProjectionMode();
            glutPostRedisplay();
            break;

        case KEY_NORMAL_EST:
            _model.changeEstimateMode();
            glutPostRedisplay();
            break;

        case KEY_PHONG:
            _model.setShading(1.0f);
            glutPostRedisplay();
            break;

        case KEY_GOURAUD_APPRO:
            _model.setShading(2.0f);
            glutPostRedisplay();
            break;

        case KEY_NORMAL_ASSIGN:
            _model.setShading(3.0f);
            glutPostRedisplay();
            break;

        case KEY_SHINEES_UP:
            _model.shinessUP();
            glutPostRedisplay();
            break;

        case  KEY_SHINEES_DOWN:
            _model.shinessDown();
            glutPostRedisplay();
            break;

        case  KEY_TEXTURING_MODE:
            _model.changeTexturingMode();
            glutPostRedisplay();
            break;

        case KEY_DECREASE_TEXTURING:
            _model.decreaseTexturing();
            glutPostRedisplay();
            break;

        case KEY_INCREASE_TEXTURING:
            _model.increaseTexturing();
            glutPostRedisplay();
            break;

        case KEY_DECREASE_TURBULENCE:
            _model.decreaseTurbulence();
            glutPostRedisplay();
            break;

        case KEY_INCREASE_TURBULENCE:
            _model.increaeTurbulence();
            glutPostRedisplay();
            break;


        default:
            std::cerr << "Key " << lower_key << " undefined\n";
            break;
    }

    return;
}

bool isClicked = false;

/********************************************************************
 * Function  :   mouse
 * Arguments :   button  : the button that was engaged in some action
 *               state   : the new state of that button
 *               x       : x value of the current mouse location
 *               y       : y value of the current mouse location
 * Returns   :   n/a
 * Throws    :   n/a
 *
 * Purpose   :   This function handles mouse actions.
 *
 \******************************************************************/
void mouse(int button, int state, int x, int y)
{

    isClicked = (state == GLUT_DOWN);
    _model.getClick(isClicked,x,y);

    if(button == GLUT_LEFT_BUTTON)
    {
        mouseClick = LEFT_CLICK;

    }
    else if (button == GLUT_RIGHT_BUTTON)
    {
        mouseClick = RIGHT_CLICK;
    }
    else
    {
        mouseClick = MIDDLE_CLICK;
    }
}



/********************************************************************
 * Function  :   motion
 * Arguments :   x   : x value of the current mouse location
 *               y   : y value of the current mouse location
 * Returns   :   n/a
 * Throws    :   n/a
 *
 * Purpose   :   This function handles mouse dragging events.
 *
 \******************************************************************/
void motion(int x, int y)
{
    if(mouseClick == LEFT_CLICK)
    {
        _model.setRotationMatrix(x, y);
        glutPostRedisplay();
    }
    else
    if(mouseClick == RIGHT_CLICK)
    {
        _model.setTranlationMatrix(x, y);
        glutPostRedisplay();
    }
    else
    {
        _model.setScale(x, y);
        glutPostRedisplay();
    }
}

static const float animationDurationInFrames = 300;

void timer(int value) {
    std::cout << "try" << std::endl;
    /* Set the timer to be called again in X milli - seconds. */
    if (g_startAnimation)
    {
        value = 0;
        g_duringAnimation = true;
        g_startAnimation = false;
    }

//    glutTimerFunc(25, timer, ++value);   // uint millis int value

    if (g_duringAnimation) {
        float t = (float)value / (float)animationDurationInFrames;
        if (t > 1) {
            g_duringAnimation = false;
            return;
        }

        glutPostRedisplay();
    }
}













