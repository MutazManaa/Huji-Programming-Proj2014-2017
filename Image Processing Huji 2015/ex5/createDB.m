function [p_x, p_y,levels, patches] = createDB( pyr )
% CREATEDB Sample 5 × 5 patches from levels 1,2,3 of the input pyramid.
% N represents the number of patches that are found in the three images.
% Arguments:
% pyr ? 7 × 1 cell created using createPyramid
%
% Outputs:
% p x ? N × 1 vector with the x coordinates of the centers of the patches in the DB
% p y ? N × 1 vector with the y coordinates of the centers of the patches in the DB
% levels ? N × 1 vector with the pyramid levels where each patch was sampled
% patches? N × 5 × 5 the patches
%
p_x = [];
p_y = [];
patches = [];
levels = [];
levelsNum = 3;
patchSize = 5;
for i = 1:levelsNum
    [p_xTemp, p_yTemp, patchesTemp] = samplePatches( pyr{i}, 2);
    patchesNumber = numel(p_xTemp);
    p_xTemp = reshape(p_xTemp, patchesNumber ,1);
    p_x = [p_x; p_xTemp];
    p_yTemp = reshape(p_yTemp, patchesNumber ,1);
    p_y = [p_y; p_yTemp];
    patchesTemp = reshape(patchesTemp, patchesNumber, patchSize, patchSize);
    patches = [patches;patchesTemp];
    levelsTemp = ones(patchesNumber,1).*i;
    levels = [levels;levelsTemp];

end

