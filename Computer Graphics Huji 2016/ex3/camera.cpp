//
// Created by mutazmanaa on 3/5/17.
//

#include "camera.h"


Camera::Camera() {}




Camera::Camera(Point3d & pos, Point3d & coi, Vector3d & up, double fov,
		size_t samples_per_pixel) :
		_position(pos), _coi(coi), _up(up.normalize()), _fov_h(fov), _samples_per_pixel(
				samples_per_pixel) {
	Vector3d direction = coi - pos;
	_leftDir = (direction % up).normalize();
	_upDir = (_leftDir % direction).normalize();
}


void Camera::setSamplesPerPixel(size_t samples_per_pixel) {
	_samples_per_pixel = samples_per_pixel;
}

void Camera::render(size_t row_start, size_t number_of_rows, BImage& img,
		Scene & scene) const {

	int width = img.getWidth();
	int height = img.getHeight();
	Vector3d direction = (_coi - _position).normalize();
	double pixelSize = 2 * tan(_fov_h) / double(width);


	Vector3d leftDir = pixelSize * _leftDir;
	Vector3d upDir = pixelSize * _upDir;

	int j = row_start;
	for (int i = 0; i < width; i++) {
		for (int rowOffset = 0; rowOffset < number_of_rows; rowOffset++) {
			Color3d pixel_colour = Color3d(0, 0, 0);
			for (int numOfIterPix = 0; numOfIterPix < _samples_per_pixel;
					numOfIterPix++) {
				Vector3d centerRay = leftDir * (i - width / 2 + 0.5)
						+ upDir * (height / 2 - 0.5 - (j + rowOffset))
						+ direction;
				centerRay = centerRay.normalize();
				Ray r(_position, centerRay);
				if (_samples_per_pixel > 1) 
				
				{
					Vector3d buttomLeft = centerRay - 0.5 * pixelSize * _leftDir + 0.5 * pixelSize * _upDir;
					Vector3d buttomRight = buttomLeft + _leftDir * pixelSize;
					Vector3d upperLeft = buttomLeft - _upDir * pixelSize;
					Vector3d upperRight = upperLeft + _leftDir * pixelSize;

					double rand1 = (double) rand() / RAND_MAX;
					double rand2 = (double) rand() / RAND_MAX;

					Vector3d buttomVec = (rand1 * buttomLeft + (1 - rand2) * buttomRight);

					Vector3d direction = (buttomVec - rand2 * pixelSize * _upDir).normalize();
					r = Ray(_position, direction);
				}

				pixel_colour += scene.trace_ray(r);

			}
			
			pixel_colour  *= 255.0;
			pixel_colour[0] = (pixel_colour[0] > 255.0) ? 255.0 : ((pixel_colour[0] < 0) ? 0 : pixel_colour[0]);
            pixel_colour[1] = (pixel_colour[1] > 255.0) ? 255.0 : ((pixel_colour[1] < 0) ? 0 : pixel_colour[1]);
            pixel_colour[2] = (pixel_colour[2] > 255.0) ? 255.0 : ((pixel_colour[2] < 0) ? 0 : pixel_colour[2]);
			

	

			img(i, j + rowOffset).r = (uchar) (pixel_colour[0]);
			img(i, j + rowOffset).g = (uchar) (pixel_colour[1]);
			img(i, j + rowOffset).b = (uchar) ( pixel_colour[2]);
		}
	}

}


