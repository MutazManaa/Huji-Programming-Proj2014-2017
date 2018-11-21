function [assignmentPositionsX,assignmentPositionsY,samplingPositionsX,samplingPositionsY] = getSamplingInformation...
    (sampleCentersX,sampleCentersY,pyr,inputPatchesCentersX,inputPatchesCentersY,levelsUp)
% GETSAMPLINGINFORMATION
% Get the information for sampling a high resolution image. Pairs of: assignment positions in the high resolution image,
%and sampling positions from the rendered pyramid image
% Arguments:
% sampleCentersX ? (m ? 4) × (n ? 4) × 3 matrix with the x coordinates of the center of the high resolution patches in
%the rendered image. This variable should be returned from getSamplingCenters function. (green x locations)
% sampleCentersY ? (m ? 4) × (n ? 4) × 3 matrix with the y coordinates of the center of the high resolution patches in
%the rendered image. his variable should be returned from getSamplingCenters function. (green y locations).
% pyr ? 7 × 1 cell created using createPyramid
% inputPatchesCentersX ? (m ? 4) × (n ? 4) input patches center x coordinates
% inputPatchesCentersY ? (m ? 4) × (n ? 4) input patches center y coordinates
% levelsUp ? integer which tells how much levels up we need to sample the window, from the found patch. In the figure
%the case is levelsUp=1
%
% Outputs:
% assignmentPositionsX ? (m ? 4) × (n ? 4) × 3 × 5 × 5 x assignment coordinates in the high resolution image (see
%figure)
% assignmentPositionsY ? (m ? 4) × (n ? 4) × 3 × 5 × 5 y assignment coordinates in the high resolution image (see
%figure)
% samplingPositionsX ? (m ? 4) × (n ? 4) × 3 × 5 × 5 x sampling coordinates in the rendered pyramid image (see figure)
% samplingPositionsY ? (m ? 4) × (n ? 4) × 3 × 5 × 5 y sampling coordinates in the rendered pyramid image (see figure)
%

%For each center of an input patch (in the input image) we have to compute the location of its center
%in the newly computed higher resolution image using transformPointsLevelsUp

levels = ones(size(inputPatchesCentersX)) * 4;
[upPixelX, upPixelY] = transformPointsLevelsUp(inputPatchesCentersX, inputPatchesCentersY, levels, pyr, levelsUp );

%Since the pixel coordinates at the higher resolution image upPixelX,upPixelY, are not integers, they should be rounded
upPixelX = repmat(upPixelX, [1,1,3]);
upPixelY = repmat(upPixelY, [1,1,3]);
roundedCentersX = round(upPixelX);
roundedCentersY = round(upPixelY);

%• Subtract upPixelX from the rounded values in assignmentPositionsX and upPixelY from assignmentPositionsY
%This is the shift of the high resolution patch with rounded coordinates from the original non-integer coordinates.
shiftX = roundedCentersX - upPixelX;
shiftY = roundedCentersY - upPixelY;

%new sampleCenters after adding the shift
sampleCentersX = sampleCentersX + shiftX ;
sampleCentersY = sampleCentersY + shiftY ;

%• Create two coordinate matrices: a 5 × 5 matrix with x coordinates, and a 5 × 5 matrix with
%y coordinates that correspond to the higher resolution patch
[Xcoordinates,Ycoordinates] = meshgrid(-2:2,-2:2);
Xcoordinates = permute(Xcoordinates,[3 4 5 1 2]);
Ycoordinates = permute(Ycoordinates,[3 4 5 1 2]);
%• Add the sampleCentersX to the x locations, and the sampleCentersX to the y locations - these
%are the green centers from the previous step. Let’s call the result samplingPositionsX and samplingPositionsY
assignmentPositionsX = bsxfun(@plus,roundedCentersX,Xcoordinates);
assignmentPositionsY = bsxfun(@plus,roundedCentersY,Ycoordinates);
samplingPositionsX = bsxfun(@plus,sampleCentersX,Xcoordinates);
samplingPositionsY = bsxfun(@plus,sampleCentersY,Ycoordinates);


%Note that the performance of bsxfun is more faster than repmat.
end

