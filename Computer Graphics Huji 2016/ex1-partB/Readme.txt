308450717 yoellevy
200750453 mutazmanaa

*****************
* impementation *
*****************
in the first part there was a need to change the vertices array to an array 
that represents a circle, change the drawing from line to fill.
in the fragment shader there was a need to calculate square(50x50 pixels) on the screen alternetly, and fill them with fill color or give them no color.

Model.cpp - change the vertices array to represent circle.
implement function to add ball to the scene:
each ball have location on the screen([1,-1]x[1,-1])
random color and direction the ball is moving.
in the draw method we create a loop that run on the balls and draw them in the right location and right size, each iteration we update all the attrubute that passes to the shaders.
we created a new method to shrink the balls before collision.
Model.h - adding some attributes to send for the shaders.
	adding method signature.
ex0.cpp - in thi file there was a need to call to the method inside model 
that add a new ball to the scene.
start the timer and remove the time of the animation to make it run until the program is closed.

*****************
*  description  *
*****************
ex0.h - header file of the main.
Makefile - file that help us compile and link easliy the program using the shell.
ShaderIO.h - header file of the shaderio  
ShaderIO.cpp - contains implementation to load and compile the shaders.

