function [sampleCentersX,sampleCentersY,renderedPyramid] = getSamplingCenters( xCenters, yCenters, centersPyrLevel, pyr, levelsUp )
% GETSAMPLINGCENTERS Given 3 nearest neighbors for each patch of the input image, from the patches DB,
% find the location of parent patch in the rendered pyramid image
% Arguments:
% xCenters ? (m ? 4) × (n ? 4) × 3 matrix with the x coordinates of the closest patches (child patches) to each sampled
%patch from the image
% yCenters ? (m ? 4) × (n ? 4) × 3 matrix with the y coordinates of the closest patches (child patches) to each sampled
%patch from the image
% centersPyrLevel ? (m ? 4) × (n ? 4) × 3 matrix with the levels of the closest patches to each sampled patch from the
%image
% pyr ? 7 × 1 cell created using createPyramid
% levelsUp ? integer which tells how much levels up we need to sample the parent patch, from the found patch. In the
%figure the case is levelsUp=1.
%
% Outputs:
% sampleCentersX ? (m ? 4) × (n ? 4) × 3 matrix with the x coordinates of the center of the patches in the rendered
%image (the green points in the figure)
% sampleCentersY ? (m ? 4) × (n ? 4) × 3 matrix with the y coordinates of the center of the patches in the rendered
%image (the green points in the figure)
% renderedPyramid ? a single image containing all levels of the pyramid
%

%• Render the pyramid to one large image using renderPyramidEx5
renderedPyramid = renderPyramidEx5(pyr);

Pyrsize1=size(pyr{1},2);
Pyrsize2=size(pyr{2},2);
Pyrsize3=size(pyr{3},2);
Pyrsize4=size(pyr{4},2);
Pyrsize5=size(pyr{5},2);
Pyrsize6=size(pyr{6},2);
pyrSizes = [0 Pyrsize1 Pyrsize2 Pyrsize3 Pyrsize4 Pyrsize5 Pyrsize6];
pyrSizes = cumsum(pyrSizes);

%• Find the locations of the centers of the corresponding parent patches in the higher resolution image
%using the supplied function transformPointsLevelsUp. Note that the centers of the parent patches
%do not have integer coordinates.
%• For each patch in the input image, you are getting the centers of its child patches (xCenters,
%yCenters, centersPyrLevel) - resulted from findNearestNeighbors function.
%• Use the centers of the parent patches to get their locations in the rendered pyramid
[sampleCentersX, sampleCentersY,levels] = transformPointsLevelsUp(xCenters, yCenters, centersPyrLevel, pyr, levelsUp);

%Notice that My X direction is the heroizinal
sampleCentersX = sampleCentersX + pyrSizes(levels);


end


