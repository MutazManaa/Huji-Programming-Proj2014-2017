import utils
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
from matplotlib import pyplot as plt
from sklearn.metrics import mean_squared_error
from math import sqrt
from math import pi
from mpl_toolkits.mplot3d import Axes3D
from matplotlib import cm
from sklearn import preprocessing
from scipy.fftpack import fft, ifft
from numpy import unravel_index

def conv2(a,b):
	'''function convolv 2d matrix a and b'''
	ma,na = a.shape
	mb,nb = b.shape
   	return np.fft.ifft2(np.fft.fft2(a,[2*ma-1,2*na-1])*np.fft.fft2(b,[2*mb-1,2*nb-1]))



def normxcorr2(b,a):
	'''# compute a normalized 2D cross correlation using convolutions
		# this will give the same output as matlab, albeit in row-major order'''
   	c = conv2(a,np.flipud(np.fliplr(b)))
   	a = conv2(a**2, np.ones(b.shape))
   
   	b = sum(b.flatten()**2)
   
   	c = (c/np.sqrt(a*b))
   	c = np.absolute(c)
   	print "The Max NCC:{}".format(np.amax(preprocessing.normalize(c, norm='l2')))
    
   	return c


def calcPointBasedReg(fixedPoints, movingPoints):
	'''function get 2 sets of points and return a rigid tranformation matrix'''
	x = [p[0] for p in fixedPoints]
	y = [p[1] for p in fixedPoints]
	centroidFixed = (sum(x) / len(fixedPoints), sum(y) / len(fixedPoints))
	centroidFixedPoints = fixedPoints - centroidFixed  
	x = [p[0] for p in movingPoints]
	y = [p[1] for p in movingPoints]
	centroidMoving = (sum(x) / len(movingPoints), sum(y) / len(movingPoints))
	centroidMovingPoints = movingPoints - centroidMoving
	covar = np.dot(centroidMovingPoints.transpose(), centroidFixedPoints)
	U, s, V = np.linalg.svd(covar, full_matrices=True)
	determenant = np.linalg.det(np.dot(V,(U.transpose())))
	diaganol = np.diag([1,determenant])
	R = np.dot(np.dot(U.transpose(),diaganol), V)

	centroidFixedL = np.array([[centroidFixed[0],centroidFixed[1]]])
	centroidMovingL = np.array([[centroidMoving[0],centroidMoving[1]]])
	tran = (centroidMovingL.transpose() - np.dot(R,centroidFixedL.transpose()))
	translation = np.fliplr(tran.transpose())
	#print translation
	PaddinR = np.zeros((R.shape[1],R.shape[1]+1),)
	PaddinR[:,:-1] = R
	PaddingTrans = np.concatenate((translation,[[1]]),axis=1)
	rigidReg = np.concatenate((PaddinR,PaddingTrans),axis=0)
	return  rigidReg.T



def calcDist(fixedPoints, movingPoints, rigidReg):
	'''function get 2 sets of points and rigid trans. matrix and return vector of distances
		between the matchin points in pixels units'''
	a = np.append(movingPoints,np.ones((len(movingPoints),1)),axis=1)
	b = np.matmul(a,rigidReg.T)
	#print b
	transMovingPoints = b[:,0:2]
	#transMovingPoints = tf.warp(a,affTrans)
	#print rigidReg
	#print fixedPoints
	#print movingPoints
	#rms = sqrt(mean_squared_error(fixedPoints,transMovingPoints))
	deltaX = (fixedPoints.T[0] - transMovingPoints.T[0])
	deltaY = (fixedPoints.T[1] - transMovingPoints.T[1])
	delta = np.square(deltaX) + np.square(deltaY)
	RMS = sqrt(np.mean(delta))
	print "The RMS VALUE after transform:{}".format(RMS)
	return delta


def calcRobustPointBasedReg(fixedPoints, movingPoints):
	'''function get 2 sets of points and get randomaly number of points and calculate the best regid trans.
		when we assume out liers in the matching point and return the min distance in pixels'''
	# we choose 2 from all possibilities of choice and calculate shortes distance, we can choose
	#a 6 or 7 points from the ouliers and randomaly we got the best 5. we choose distance 5 because we know that
	#the maximum of distance limited by 5 pixels from the first set of points.
	minPtNum = 2
	iterNum = misc.comb(len(movingPoints), 2, exact=True)
	print iterNum
	thDist = 5
	thInlrRatio = 0.5
	f, inlierIdx = utils.ransac(fixedPoints,movingPoints,calcPointBasedReg,calcDist,minPtNum,iterNum,thDist,thInlrRatio)
	
	return f, inlierIdx
	



def transformImage(imgMoving,movingPoints,rigidReg):
	'''function get matrix and set of points and tranf. the image and the points set'''
	affTrans = tf.AffineTransform(rigidReg)
	transImgMoving = tf.warp(imgMoving,affTrans.inverse)		
	a = np.append(movingPoints,np.ones((len(movingPoints),1)),axis=1)
	b = np.matmul(a,rigidReg.T)
	transMovingPoints = b[:,0:2]
	return transImgMoving,transMovingPoints
	
	
def drawImages(imageFixed,imageMoving,fixedPoints,movingPoints):
	'''function get 2 images and 2 sets of matching points and draw/show them'''
	plt.figure(1)
	plt.title("Fixed BRAIN")
	plt.imshow(imageFixed, cmap='gray')
	plt.plot(fixedPoints.T[0], fixedPoints.T[1], 'ro')
	
	for i in xrange(0, len(fixedPoints)):
		x = fixedPoints.T[0][i]
		y = fixedPoints.T[1][i]
		plt.annotate("{}".format(i + 1), xy=(x, y), xytext=(x, y))
		
	plt.show()


	plt.figure(2)
	plt.title("Moved BRIAN")
	plt.imshow(imageMoving, cmap='gray')
	plt.plot(movingPoints.T[0], movingPoints.T[1], 'ro')
	for i in xrange(0, len(movingPoints)):
		x = movingPoints.T[0][i]
		y = movingPoints.T[1][i]
		plt.annotate("{}".format(i + 1), xy=(x, y), xytext=(x, y))

	plt.show()	
	
	
	
def main():
	
	#handle no ouliers points
	print "No outliers"
	fixedPoints, movingPoints = utils.getPoints('no_outliers')
	brainMat = io.loadmat("brain")
	brain_moving = brainMat.get("brain_moving")
	brain_fixed = brainMat.get("brain_fixed")
	rigidReg = calcPointBasedReg(fixedPoints,movingPoints)
	transBrain , transMovingPoints = transformImage(brain_moving,movingPoints,rigidReg)	
	drawImages(brain_fixed,brain_moving,fixedPoints,movingPoints)
	drawImages(brain_fixed,transBrain,fixedPoints,transMovingPoints)
	delta = calcDist(fixedPoints, movingPoints, rigidReg)
	plt.figure(1)
	plt.title("Fixed Brain vs. tranformed Moved Brain:No ouliers")
	plt.imshow(ndimage.sobel(transBrain),cmap=plt.cm.gray,alpha=1)
	plt.imshow(brain_fixed,cmap=plt.cm.gray,alpha=0.75)
	plt.show()
	

	#handle with ouliers points
	print "With Outliers"	
	fixedPoints, movingPoints = utils.getPoints('with_outliers')
	brainMat = io.loadmat("brain")
	brain_moving = brainMat.get("brain_moving")
	brain_fixed = brainMat.get("brain_fixed")
	rigidReg = calcPointBasedReg(fixedPoints,movingPoints)
	transBrain , transMovingPoints = transformImage(brain_moving,movingPoints,rigidReg)	
	drawImages(brain_fixed,brain_moving,fixedPoints,movingPoints)
	drawImages(brain_fixed,transBrain,fixedPoints,transMovingPoints)
	delta = calcDist(fixedPoints, movingPoints, rigidReg)
	plt.figure(2)
	plt.title("Fixed Brain vs. tranformed Moved Brain:With ouliers")
	plt.imshow(brain_fixed,cmap='gray' ,alpha=1)
	plt.imshow(ndimage.sobel(transBrain),cmap='gray',alpha=0.75)
	plt.show()
	
	#handle ransac for no outliers
	fixedPoints, movingPoints = utils.getPoints('no_outliers')
	f, inlierIdx = calcRobustPointBasedReg(movingPoints,fixedPoints)
	print "The points selected by the ranscac:{}".format(inlierIdx)
	
	#handle ransac for wiht  outliers
	fixedPoints, movingPoints = utils.getPoints('with_outliers')
	f, inlierIdx = calcRobustPointBasedReg(movingPoints,fixedPoints)

	print "The points selected by the ranscac:{}".format(inlierIdx)
	
	#calculate NCC
	fixedPoints, movingPoints = utils.getPoints('no_outliers')
	brainMat = io.loadmat("brain")
	brain_moving = brainMat.get("brain_moving")
	brain_fixed = brainMat.get("brain_fixed")
	rigidReg = calcPointBasedReg(fixedPoints,movingPoints)
	transBrain , transMovingPoints = transformImage(brain_moving,movingPoints,rigidReg)
	a = brain_moving
	b = brain_fixed
	c = transBrain
	d = brain_fixed
	'''
	# another way to calculate NCC ,for good results you can write a = a/a.std() for 2 correlated checked matricies
	normBrMv = brain_moving
	#normBrMvNormalized = preprocessing.normalize(normBrMv, norm='l2')
	normBrFx = brain_fixed
	#normBrFxNormalized = preprocessing.normalize(normBrFx, norm='l2')
	NCCBefore = signal.correlate2d(normBrMv,normBrFx)
	np.save("NCCBeforeee",NCCBefore)
	#print NCCBefore
	normTrBr = transBrain
	#normTrBrNormalized = preprocessing.normalize(normTrBr, norm='l2')
	NCCAfter = signal.correlate2d(normTrBr,normBrFx)
	np.save("NCCAfterrr",NCCAfter)
	

	'''
	
	#save and load because signal.correlate2d take much time
	NCCBefore = normxcorr2(b,a)
	NCCAfter = normxcorr2(d,c)
	
	np.save("NCCBefore",NCCBefore)
	np.save("NCCAfter",NCCAfter)

	
	

	
	NCCbefore = np.load("NCCBefore.npy")
	NCCafter = np.load("NCCAfter.npy")
	plt.figure(1)
	plt.imshow(NCCbefore,cmap='gray')
	plt.title("Fouries transorm of NCC Image Before Trans.")	
	plt.show()
	plt.figure(2)
	plt.imshow(NCCafter,cmap='gray')
	plt.title("Fouries transorm of NCC Image After Trans.")
	plt.show()
	

	
	fig = plt.figure(1)
	x,y = np.meshgrid(np.arange((NCCbefore.shape[0])),np.arange((NCCbefore.shape[1])))
	ax = plt.subplot(111, projection='3d')
	surf = ax.plot_surface(x,y,NCCbefore, cmap=cm.CMRmap, linewidth=0, antialiased=False)
	fig.colorbar(surf, shrink=0.5, aspect=10)
	plt.title("Sub_Surface of NCC Image Before Trans.")
	plt.show()
	


	fig = plt.figure(2)
	x,y = np.meshgrid(np.arange((NCCafter.shape[0])),np.arange((NCCafter.shape[1])))
	ax = plt.subplot(111, projection='3d')
	surf = ax.plot_surface(x,y,NCCafter, cmap=cm.CMRmap, linewidth=0, antialiased=False)
	fig.colorbar(surf, shrink=0.5, aspect=10)
	plt.title("Sub_Surface of NCC Image After Trans.")
	plt.show()
	
	print "finished"
##########################################################################################
if __name__ == "__main__":
	main()

