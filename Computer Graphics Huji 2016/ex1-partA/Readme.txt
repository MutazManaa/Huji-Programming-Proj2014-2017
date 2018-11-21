308450717 yoellevy
200750453 mutazmanaa

*****************
* impementation *
*****************
in Model.cpp we make the vertices array to contains 864 vertices,
to contain triangles that have a 15 degree each and use a rotation
calculation to rotate them.
we also needed to say to opengl we are loading 864 vertices.

SimpleShader.frag - there was a need to calculate the position of
the pixel on the screen; if the pixel position mod 25 is between 0
and 12.5 we want to fill the square, else we are fill it with black
which is the background.

*****************
*  description  *
*****************

ShaderIO.cpp - load the shader compile it and link
it with the program

Model.cpp - the main model that we are loading,
has all the vertices the color that we want the
model to be paint, the type of
coloring(filling, lines, vertices) and more.

ex0.cpp - the main program that connect all data
to one scene and manage the mouse and keyboard events.

