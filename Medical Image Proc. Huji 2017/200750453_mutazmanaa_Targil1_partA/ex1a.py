import os
import time
from PIL import Image
import matplotlib.pyplot as plt
import nibabel as nib
import numpy as np
from scipy import ndimage
from skimage import morphology
from skimage import measure



'''CONSTANTS NUMBERS AND STRINGS ARE HERE'''
Imax = 1300
CT_FILE_SUFFIX = ".nii.gz"
GRAPH_SUFFIX = ".jpg"
GRAPH_SUFFIX_PNG = ".png"
CT_FILE_SEGMENTATION = "{}_seg_{}_{}"
FULL_CT_FILENAME = "Case{}_CT"
GRAPH_FILENAME = "{}_Graph"
FORMAT = "{}"
FULL_SKELETON_FILENAME = "{}_SkeletonSegmentation"
FACTOR_DECREAS_COMPONENT = 100000
MIM_THRSHOLD = 150
MAX_THRESHOLD = 500
THRESHOLD_STEP = 5
NUMBER_OF_IMAGES = 5

def secondSmallest(e):
	'''Function get array/list and return the second smallest element value'''
	if len(e)==2 and e[0]<=e[1]:	
		return e[1]
	return secondSmallest(e[:-1]) if e[0]<=e[-1]>=e[1] else secondSmallest([e[-1]]+e[:-1])

def SegmentationByTH(fileName,Imin,Imax):
	'''Function get CT medical file  image and return boolean segmentation labeled Matrix of True/False
		if the CT medical image value between Imin and Imax where Imin and Imax the bones threshold.'''

	print "Stage 2: in SegmentationByTH"
	image = nib.load(fileName)
	image_data = image.get_data()
	dim1,dim2,dim3 = image_data.shape
	#building boolean segmentation matrix
	segMatrix = np.zeros((dim1,dim2,dim3), dtype=np.bool)
	#label 1 the voxels satify the condition
	#goodInices = np.logical_and(image_data >= Imin,image_data <= Imax).nonzero()[0]
	goodInices = np.bitwise_and(image_data >= Imin, image_data <= Imax) #more fast
	segMatrix[goodInices] = True
	image_data[goodInices] = 1
	image_data[goodInices != True] = 0
	origfileName = fileName[:fileName.find(CT_FILE_SUFFIX)]
	#saving the segmentation file to the directory 
	nib.save(image,(CT_FILE_SEGMENTATION+CT_FILE_SUFFIX).format(origfileName,Imin,Imax))
	#print "This Image data type:" ,image_data.dtype
	#print "This Image shape :" , image_data.shape
	#print "This Segmentation Matrix data type:" ,segMatrix.dtype
	#print "This Segmentation Matrix shape:" , segMatrix.shape
	return segMatrix

def SkeletonTHFinder(Imin = None, onlyOneSeg = False):
	'''Function iterate above the CT medical images and get the best Imin value threshold, when it chosen to be 
		the second smallest value led to minimum number of components'''	

	print "Stage 1: in SkeletonTHFinder, it will take long time fi iterate all over Imin Values"
	IminValuesArr = []#stores Best Imin values for the segmentation - one per image
	for i in xrange(5, 6): #image range, Change it to (1,6) if desired to iterate over all images
		fileName = (FULL_CT_FILENAME + CT_FILE_SUFFIX).format(i)
		print "Skeletonize and Segment:{}".format(fileName)
		IminValues = []
		numberOfComponents = []
		#enable a single value of Imin
		if(Imin != None and onlyOneSeg):
			SegMatrix = SegmentationByTH(fileName, Imin, Imax).astype(np.int16)
			labels, num = measure.label(SegMatrix.astype(np.int16), connectivity=2, return_num=True)#get Components number
			IminValues.append(Imin)
			numberOfComponents.append(num)
		#enable range of Imin values from Imin to MAX_THRESHOLD		
		elif (Imin != None and onlyOneSeg == False):	
			for Imin in xrange(Imin, MAX_THRESHOLD, THRESHOLD_STEP):
				SegMatrix = SegmentationByTH(fileName, Imin, Imax).astype(np.int16)
				#get Components number
				labels, num = measure.label(SegMatrix.astype(np.int16), connectivity=2, return_num=True)
				IminValues.append(Imin)
				numberOfComponents.append(num)
		#enable range of Imin values from MIM_THRSHOLD to MAX_THRESHOLD	
		elif (Imin == None and onlyOneSeg == False):	
			for Imin in xrange(MIM_THRSHOLD, MAX_THRESHOLD, THRESHOLD_STEP):
				#get Components number
				SegMatrix = SegmentationByTH(fileName, Imin, Imax).astype(np.int16)
				labels, num = measure.label(SegMatrix.astype(np.int16), connectivity=2, return_num=True)
				IminValues.append(Imin)
				numberOfComponents.append(num)
		print "Stage 3:Plotting The Graph"
		#Graph handle 
		plt.xlabel('IMin')
		plt.ylabel('Number Of Components')
		plt.plot(IminValues, numberOfComponents, 'ro')
		#get the second smallest best Imin Value that get munimum number of components.
		#IminValue = secondSmallest(numberOfComponents)
		IminValue = IminValues[numberOfComponents.index(min(numberOfComponents))]
		graphFileName = CT_FILE_SEGMENTATION.format(fileName[:fileName.find(CT_FILE_SUFFIX)], MIM_THRSHOLD, Imax)
		
		plt.savefig((GRAPH_FILENAME + GRAPH_SUFFIX_PNG).format(graphFileName))
		#Image.open((GRAPH_numberOfComponents + GRAPH_SUFFIX_PNG).format(seg_fileName)).save((GRAPH_numberOfComponents + 
									#GRAPH_SUFFIX).format(seg_fileName),'JPEG') //alpha channel problem
		plt.gcf().clear()
		plt.close()
		
		segFileName = CT_FILE_SEGMENTATION.format(fileName[:fileName.find(CT_FILE_SUFFIX)], IminValue, Imax)
		image = nib.load((FORMAT + CT_FILE_SUFFIX).format(segFileName))
		segMatrix1 = image.get_data()
		print "Stage 4: Do morphlogy"
		#removing small components less than filterSzize that may be noise or not bones objects, if it so large
		#we decrease it to get logic number of components,,choosen to be  14e6 according to best match 
		filteSize = 1000000
		while True:
			morphologyImageTemp = morphology.remove_small_objects(segMatrix1 == 1, min_size=filteSize, connectivity=2)
			labels1, num1 = measure.label(morphologyImageTemp, connectivity=2, return_num=True)
			
			if(num1 >= 1):
				break			
			else:
				filteSize -= FACTOR_DECREAS_COMPONENT
				continue
		#do morphology , here may be we loss some information about bones
		morphologyImage = ndimage.binary_dilation(morphologyImageTemp, iterations=2)
		morphologyImage = ndimage.binary_closing(morphologyImage, iterations=4)
		morphologyImage = ndimage.binary_fill_holes(morphologyImage)
		morphologyImage = ndimage.binary_closing(morphologyImage, iterations=4)


		#removing small components less than filterSzize that may be noise or not bones objects, if it so large
		#we decrease it to get logic number of components,choosen to be  14e6 according to best match 
		print "Stage 5: Do more Morphology"
		filteSize = 1400000
		while True:
			finalImage = morphology.remove_small_objects(morphologyImage == 1, min_size=filteSize, connectivity=2)
			labels2, num2 = measure.label(morphologyImageTemp, connectivity=2, return_num=True)
			
			if(num2 >= 1):
				break			
			else:
				filteSize -= FACTOR_DECREAS_COMPONENT
				continue
		print"Final Stage: save The image"			
		labels3, num3 = measure.label(finalImage, connectivity=2, background=0, return_num=True)
		#save the final image after skeletomization
		imageToSave = nib.Nifti1Image(finalImage.astype(np.int8), image.affine)
		nib.save(imageToSave,(FULL_SKELETON_FILENAME + CT_FILE_SUFFIX).format(segFileName))
		
		#store the choosen Imin value
		IminValuesArr.append(IminValue)	
		print "The Imin Value for image {} is:{}".format(i,IminValue)


	return IminValuesArr


def main():
	
	 
	start = time.time()
	#recomnded to use the function for desired value of Imin and the flag True
	IminValuesArr = SkeletonTHFinder(260,True)# the last value of Case5_CT , you can change it and the range of images on line 66
	print "Finished!"
	end = time.time()
	print "The time for this images:" , (end - start)#measure time for techniqes issues




if __name__ == "__main__":
	main()






		
