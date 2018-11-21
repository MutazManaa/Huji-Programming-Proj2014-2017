import os
import time
from PIL import Image
import matplotlib.pyplot as plt
import nibabel as nib
import numpy as np
from scipy import ndimage
from scipy import io
from scipy import misc
from scipy import signal
from skimage import morphology
from skimage import measure
import PIL.ImageOps 
import glob
from skimage import transform as tf
from scipy import ndimage
from skimage.io import imread
from matplotlib import pyplot as plt
from sklearn.metrics import mean_squared_error
import math
from skimage.filters import sobel
from math import sqrt
from math import pi
from mpl_toolkits.mplot3d import Axes3D
from matplotlib import cm
from sklearn import preprocessing
from scipy.fftpack import fft, ifft
from numpy import unravel_index
from scipy import misc

FULL_ANGLE = 360


def show_radon_registration(img1, rotation_ccw_deg, translation_pixels, num_angles):
	'''The function  get image and transform parameters and apply transformation and radon transform,then show the results and the diffirences betwwen them'''

	imgGrey = img1
	allSize = imgGrey.shape[0]
	padSize = imgGrey.shape[0]/2
	#pad the image to get correct transform
	imgPad = np.pad(imgGrey, padSize, mode="edge")
	#get the transformation
	tform = tf.SimilarityTransform(scale=1, rotation=math.radians(rotation_ccw_deg),translation=translation_pixels)
	
	#apply the transformation	
	imgWarped = tf.warp(imgPad,tform.inverse)
	
	#show results
	plt.figure(1)
	#plt.title("Fixed Brain With Padding")
	plt.title("Phantom With Padding")
	plt.imshow(imgPad,cmap=cm.gray)
	plt.figure(2)
	#plt.title("Fixed Brain After Padding and transformation")
	plt.title("Phantom After Padding and transformation")
	plt.imshow(imgWarped,cmap=cm.gray)
	plt.show()
	
	#return images to original size
	unpadImgPad = imgPad[padSize: allSize - padSize+1,padSize: allSize - padSize+1]
	unpadImWraped = imgWarped[padSize: allSize - padSize+1,padSize: allSize - padSize+1]

	fixed_angles_deg = np.arange(180)
	moving_angles_deg = np.arange(0,180,180/num_angles) 
	
	#apply radon trnasform with angles range
	fixed_sinogram = tf.radon(unpadImgPad, fixed_angles_deg,circle=True)
	moving_sinogram = tf.radon(unpadImWraped, moving_angles_deg,circle=True)

	#get the best radon parameters for transfromation
	theta, trans = radon_register(fixed_sinogram, fixed_angles_deg, moving_sinogram, moving_angles_deg)

	tform1 = tf.SimilarityTransform(scale=1, rotation=math.radians(theta),translation=trans)
	
	#apply the trans																			
	imgWarped1 = tf.warp(imgPad,tform1.inverse)

	#show results
	plt.figure(3)
	#plt.title("Fixed Brain After Radon tranformation")
	plt.title("Phantom After Radon tranformation")
	plt.imshow(imgWarped1,cmap=cm.gray)
	plt.show()

	#show diffirences
	plt.imshow(imgPad,cmap=cm.gray)
	plt.imshow(sobel(imgWarped1),alpha=0.5)
	#plt.title("Differences Between Original Brain and Radon Transform Brain")
	plt.title("Differences Between Original Phantom and Radon Transform Phantom")
	plt.show()



def radon_register(fixed_sinogram, fixed_angles_deg, moving_sinogram, moving_angles_deg):
	'''function get fixed sinogram and moving sinogram with angles range and return the translation and rotation angle to make regression'''
	
	
	angles = np.zeros(FULL_ANGLE)
	#iterating over the moving sinogram angles number to get the best corellation angle
	dic = {} 
	for moving_angel in xrange(0, len(moving_angles_deg)):
		data1 = moving_sinogram[:, moving_angel]
		values = [-1, -1, -1, -1]
		#iterating over the fixed sinogram angles number to get the best corellation angle
		for fixed_angle in xrange(len(fixed_angles_deg)):
			data2 = fixed_sinogram[:, fixed_angle]
			data1 = (data1 - np.mean(data1))/np.std(data1)
			data2 = (data2 - np.mean(data2))/np.std(data2)
			#get the corellation
			correlations = signal.correlate(data1, data2, mode='full')
			#check wich angle get the max correlation
			if correlations.max() > values[2]:
				values[0] = moving_angles_deg[moving_angel]
				values[1] = fixed_angles_deg[fixed_angle]
				values[2] = correlations.max()
				values[3]= data2.size - np.argmax(correlations) - 1

		#get the delta difference	
		delta = int(math.floor(values[0] - values[1])) % FULL_ANGLE
		if delta not in dic:
			dic[delta] = []
		dic[delta].append((values[0], values[1], values[3]))
		angles[delta] += 1
	#get the arg of best angle
	theta = np.argmax(angles)
	diffirences = np.empty(len(dic[theta]))
	N = np.empty((len(dic[theta]), 2))
	for index, angleValue in enumerate(dic[theta]):
		diffirences[index] = angleValue[2]
		N[index][0] = np.cos(np.deg2rad(angleValue[1]))
		N[index][1] = -np.sin(np.deg2rad(angleValue[1]))
	#calculate linear equation system to get trabslation
	trans = np.linalg.lstsq(N, diffirences)[0]
		
	#get the tranformation and reverse direction
	trans[0] = -1 * trans[0]
	trans[1] = -1 * trans[1]
	theta = -1*theta
	return theta,trans

	
def main():
	#load Data
	brainMat = io.loadmat("brain")
	brain_moving = brainMat.get("brain_moving")
	brain_fixed = brainMat.get("brain_fixed")
	

	num_angles = 50;
	translation_pixels = (10,30)
	rotation_ccw_deg = -15
	
	#show results after radon registration
	show_radon_registration(brain_fixed, rotation_ccw_deg, translation_pixels, num_angles)
	#load the phantom image as grey image to normalize the air to zero
	phantom = imread("Phantom.png",as_grey=True)
	show_radon_registration(phantom, rotation_ccw_deg, translation_pixels, num_angles)
	



if __name__ == "__main__":
	main()
	
	

