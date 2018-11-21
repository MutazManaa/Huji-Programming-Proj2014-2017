import os
import time
from PIL import Image
import matplotlib.pyplot as plt
import nibabel as nib
import numpy as np
from scipy import ndimage
from scipy.spatial import ConvexHull
from skimage import morphology
from skimage import measure
import PIL.ImageOps 
import glob



def secondLargest(numbers):
	''' function get list of numbers and retur the second largest'''
	
	count = 0
	m1 = m2 = float('-inf')
	for x in numbers:
		count += 1
		if x > m2:
			if x >= m1:
				m1, m2 = x, m1            
			else:
				m2 = x
	return m2 if count >= 2 else None		


##########################################################################################
def IsolateBody(CTScan):
	'''The function extract/segment the body from CT scan'''
	goodIndicies = np.bitwise_and(CTScan >= -500, CTScan <= 2000)
	#print np.count_nonzero(goodIndicies)
	labels, num = measure.label(goodIndicies ==1, connectivity=2, return_num=True)
	#print num
	#rps = measure.regionprops(labels, cache=False)
	areas = []
	#for r in rps:
	#	areas.append(r.area)
	#print max(areas)
	#maxLabel = areas.index(max(areas))
	#print maxLabel
	for i in xrange(1,num+1):
		areas.append(np.count_nonzero(labels == i))
	
	maxLabel = areas.index(max(areas)) + 1
	goodIndicies[labels != maxLabel] = 0
	#print np.count_nonzero(goodIndicies)

	return goodIndicies

##########################################################################################
def IsolateBS(body):
	'''The function extract the Lungs Segmentaion from body segmentaion'''
	#print np.count_nonzero(body)
	lungs = 1 - body
	#print BodyScan.dtype
	#print np.count_nonzero(lungs)
	labels, num = measure.label(lungs == 1 , connectivity=2, return_num=True)
	#print num
	areas = []
	for i in xrange(1,num+1):
		areas.append(np.count_nonzero(labels == i))

	secondMax = areas.index(secondLargest(areas)) + 1 
	#print secondMax
	lungs[labels != secondMax] = 0
	#print np.count_nonzero(BodyScan)

	wide = np.sum(lungs,axis = (0,1))
	#print wide.shape
	#print np.argmax(wide)
	CC = np.argmax(wide)
	a = np.where(wide>0)
	#print a[0][0]
	BB = a[0][0]
	return lungs,BB,CC


##########################################################################################
def ThreeDBand(body,lungs,BB,CC):
	'''The function build 3D band around the lungs and on around the body on the lungs zone, and get convex Hull inside them.'''
	lungsBand = ndimage.binary_dilation(lungs[:, :,CC])
	lungsHull = morphology.convex_hull_image(lungsBand)
	hullBand = ndimage.binary_dilation(lungsHull)
	lungBoarder = ndimage.sobel(hullBand).astype(np.bool)
	fullBandLungs = ndimage.binary_dilation(lungBoarder)


	dim1,dim2,dim3 = body.shape

	band = np.zeros((dim1,dim2,dim3), dtype=np.bool)	
	body = body.copy(order='C')
	for i in xrange(BB,CC+1):
		#body = body.copy(order='C')	
		bodyHull = morphology.convex_hull_image(body[:, :, i]) # spatial.ConvexHul: doesn't work
		bodyHellErision = ndimage.binary_erosion(bodyHull, iterations=3)
		bodyBorder = ndimage.sobel(bodyHellErision).astype(np.bool)
		bodyBand = ndimage.binary_dilation(bodyBorder, iterations=2)
		externalBodyBorder = np.bitwise_or(fullBandLungs, bodyBand)
		band[:, :, i] = externalBodyBorder

	return band
	

##########################################################################################	
def SpineROI(body,CC, aorta, hardCase):
	'''The function build 3D rectangle around the spin.'''
	Xscale = 25
	Yscale = 1.3
	
	if hardCase:
		Yscale = 1 / Yscale
		
	xRange = [min(np.where(aorta >= 1)[0]),max(np.where(aorta >= 1)[0])]
	yRange = [int(min(np.where(aorta >= 1)[1]) * Yscale),max(np.where(aorta >= 1)[1])]
	zRange = [min(np.where(aorta >= 1)[2]),max(np.where(aorta >= 1)[2])]

	aortaMean = int(np.mean(xRange))
	if hardCase:
		xRange[0] -= 2 * Xscale
	else:
		xRange[1] += 2 * Xscale
	
	yRange[1] = max(np.where(body == True)[1])
	if hardCase:
		aortaMean = aorta.shape[1] - aortaMean
		bodyYRange = [min(np.where(body == True)[1]),0]
		tmp = aortaMean
		aortaMean = bodyYRange[0]
		bodyYRange[0] = tmp

	roi = np.zeros(aorta.shape)
	roi[xRange[0]:xRange[1], aortaMean:yRange[1],
	zRange[0]:zRange[1]] = 1
	rectangle_3d_indices = roi == 1
	body[np.bitwise_not(rectangle_3d_indices)] = 0
	bodyDialation = ndimage.binary_dilation(body, iterations=2)
	components, components_count = measure.label(bodyDialation, connectivity=2, return_num=True)
	components_size_sorted = sorted([np.sum(components == i) for i in xrange(1, components_count + 1)])
	cleanBody = morphology.remove_small_objects(bodyDialation == 1, min_size=components_size_sorted[-1], connectivity=2)
	spinXRange = [min(np.where(cleanBody == 1)[0]),max(np.where(cleanBody == 1)[0])] 
	spinYRange = [min(np.where(cleanBody == 1)[1]),max(np.where(cleanBody == 1)[1])]
	spinBound = np.zeros(body.shape)
	spinBound[spinXRange[0]:spinXRange[1],spinYRange[0]:spinYRange[1],zRange[0]:max([CC, zRange[1]])] = 1
	return spinBound

#########################################################################################
def MergedROI(band,spinBound):
	'''The function Merge The 3D rectangle that bound the Spin and the 3D band of the lungs and the body.'''
	MergedROI = band.astype(np.bool)
	MergedROI[spinBound.astype(np.bool)] = True
	return MergedROI

##########################################################################################
def main():
	##ATTENTION: IT IS EXAMPLE OF CT1 AND HARDCT1 , IF YOU WANT TO PLAY ALL SCAN, PLEASE CHANGE THE RANGE TO SCANS YOU WANT IN THE LOOP
###### Get Body Segmentation for regular cases CT:
	for i in xrange(1,2): # the range of the image
		print "segment Body of CT:{}".format(i)
		image1 = nib.load("Case{}_CT.nii.gz".format(i))
		CTScan = image1.get_data()
		body = IsolateBody(CTScan)
		imageToSave = nib.Nifti1Image(body.astype(np.int8), image1.affine)
		nib.save(imageToSave,"Case{}_Body.nii.gz".format(i))
		image1.uncache()
####### Get body Segmentation for Hard cases CT:
	for i in xrange(1,2):
		print "segment Body of HardCase CT:{}".format(i)
		image1 = nib.load("HardCase{}_CT.nii.gz".format(i))
		CTScan = image1.get_data()
		body = IsolateBody(CTScan)
		imageToSave = nib.Nifti1Image(body.astype(np.int8), image1.affine)
		nib.save(imageToSave,"HardCase{}_Body.nii.gz".format(i))
		image1.uncache()	

#################################################################################  
####### Get Lungs Segmentation for regular cases CT:
	for i in range(1,2):
		print "segment Lungs of CT:{}".format(i)
		image1 = nib.load("Case{}_Body.nii.gz".format(i))
		body = image1.get_data()	
		lungs,BB,CC = IsolateBS(body)
		imageToSave = nib.Nifti1Image(lungs.astype(np.int8), image1.affine)
		nib.save(imageToSave,"Case{}_Lungs{}_{}.nii.gz".format(i,BB,CC))
		image1.uncache()
###### Get Lungs Segmentation for Hard cases CT:		
	for i in range(1,2):
		print "segment Lungs of HardCase CT:{}".format(i)
		image1 = nib.load("HardCase{}_Body.nii.gz".format(i))
		body = image1.get_data()	
		lungs,BB,CC = IsolateBS(body)
		imageToSave = nib.Nifti1Image(lungs.astype(np.int8), image1.affine)
		nib.save(imageToSave,"HardCase{}_Lungs{}_{}.nii.gz".format(i,BB,CC))
		image1.uncache()
		
###################################################################################  
###### Get Lungs 3D band around and convex hull between body and lungs for regular cases CT:	
	for i in range(1,2):
		print "bound the lungs and the body of CT:{}".format(i)
		image1 = nib.load("Case{}_Body.nii.gz".format(i))
		body = image1.get_data()
		fileName = "Case{}_Lungs".format(i)
		file_list = os.path.dirname(os.path.abspath(__file__)) + "/"
		lungsFile = glob.glob(file_list + fileName + '*.nii.gz')
		image2 = nib.load(lungsFile[0])
		lungs = image2.get_data()
		temp = lungsFile[0].split("Lungs")
		temp1 = temp[1].split(".")
		temp2 = temp1[0].split("_")
		BB = int(temp2[0])
		CC = int(temp2[1])
    	
		band = ThreeDBand(body,lungs,BB,CC)
		imageToSave = nib.Nifti1Image(band.astype(np.int8),image1.affine)
		nib.save(imageToSave,"Case{}_Band.nii.gz".format(i))
		image1.uncache()
		image2.uncache()


###### Get Lungs 3D band around and convex hull between body and lungs for Hard cases CT:
	for i in range(1,2):
		print "bound the lungs and the body of Hard case CT:{}".format(i)
		image1 = nib.load("HardCase{}_Body.nii.gz".format(i))
		body = image1.get_data()
		fileName = "HardCase{}_Lungs".format(i)
		file_list = os.path.dirname(os.path.abspath(__file__)) + "/"
		lungsFile = glob.glob(file_list + fileName + '*.nii.gz')
		image2 = nib.load(lungsFile[0])
		lungs = image2.get_data()
		temp = lungsFile[0].split("Lungs")
		temp1 = temp[1].split(".")
		temp2 = temp1[0].split("_")
		BB = int(temp2[0])
		CC = int(temp2[1])
	    	
		band = ThreeDBand(body,lungs,BB,CC)
		imageToSave = nib.Nifti1Image(band.astype(np.int8),image1.affine)
		nib.save(imageToSave,"HardCase{}_Band.nii.gz".format(i))
		image1.uncache()
		image2.uncache()

#################################################################################  			
###### Get bound band for spin in regular cases CT:
	for i in range(1,2):
		print "Build rectangle bound of spin for CT:{}".format(i)
		image1 = nib.load("Case{}_Aorta.nii.gz".format(i))
		aorta = image1.get_data()
		image2 = nib.load("Case{}_Body.nii.gz".format(i))
		body = image2.get_data()
		fileName = "Case{}_Lungs".format(i)
		file_list = os.path.dirname(os.path.abspath(__file__)) + "/"
		lungsFile = glob.glob(file_list + fileName + '*.nii.gz')
		image2 = nib.load(lungsFile[0])
		lungs = image2.get_data()
		temp = lungsFile[0].split("Lungs")
		temp1 = temp[1].split(".")
		temp2 = temp1[0].split("_")
		BB = int(temp2[0])
		CC = int(temp2[1])
		spinBound = SpineROI(body,CC,aorta,0)
		imageToSave = nib.Nifti1Image(spinBound.astype(np.int8),image1.affine)
		nib.save(imageToSave,"Case{}_SpinBound.nii.gz".format(i))
		image1.uncache()
		image2.uncache()

###### Get bound band for spin in Hard cases CT:	
	for i in range(1,2):
		print "Build rectangle bound of spin for Hard case CT:{}".format(i)
		image1 = nib.load("HardCase{}_Aorta.nii.gz".format(i))
		aorta = image1.get_data()
		image2 = nib.load("HardCase{}_Body.nii.gz".format(i))
		body = image2.get_data()
		fileName = "HardCase{}_Lungs".format(i)
		file_list = os.path.dirname(os.path.abspath(__file__)) + "/"
		lungsFile = glob.glob(file_list + fileName + '*.nii.gz')
		image2 = nib.load(lungsFile[0])
		lungs = image2.get_data()
		temp = lungsFile[0].split("Lungs")
		temp1 = temp[1].split(".")
		temp2 = temp1[0].split("_")
		BB = int(temp2[0])
		CC = int(temp2[1])
		spinBound = SpineROI(body,CC,aorta,1)
		imageToSave = nib.Nifti1Image(spinBound.astype(np.int8),image1.affine)
		nib.save(imageToSave,"HardCase{}_SpinBound.nii.gz".format(i))
		image1.uncache()
		image2.uncache()

###########################################################################  
###### Get Merged ROI for spin and Lungs in regular cases CT: 	
	for i in range(1,2):
		print "Merge Lungs segmentation and spinROI for CT:{}".format(i)
		image1 = nib.load("Case{}_Band.nii.gz".format(i))
		band = image1.get_data()
		image2 = nib.load("Case{}_SpinBound.nii.gz".format(i))
		spinBound = image2.get_data()
		mergedROI = MergedROI(band,spinBound)
		imageToSave = nib.Nifti1Image(mergedROI.astype(np.int8),image1.affine)
    	nib.save(imageToSave,"Case{}_ROI.nii.gz".format(i))
    	image1.uncache()
    	image2.uncache()   

###### Get Merged ROI for spin and Lungs in Hard cases CT:  	
	for i in range(1,2):
		print "Merge Lungs segmentation and spinROI for Hard case CT:{}".format(i)
		image1 = nib.load("HardCase{}_Band.nii.gz".format(i))
		band = image1.get_data()
		image2 = nib.load("HardCase{}_SpinBound.nii.gz".format(i))
		spinBound = image2.get_data()
		mergedROI = MergedROI(band,spinBound)
		imageToSave = nib.Nifti1Image(mergedROI.astype(np.int8),image1.affine)
    	nib.save(imageToSave,"HardCase{}_ROI.nii.gz".format(i))
    	image1.uncache()
    	image2.uncache()	
    		
##########################################################################################
if __name__ == "__main__":
	main()






		
