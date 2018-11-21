function [weights] = weightsSetting( imPatches, Dists, pyr ,dbPatchesStd )
% WEIGHTSSETTING Given 3 nearest neighbors for each patch of the input image
% Find a threshold (maximum distance) for each input patch.
% Next, give a weight for each candidate based on its distance from the input patch.
% denote m,n such that [m,n]=size(pyr{4})
% Arguments:
% imPatches ? (m ? 4) × (n ? 4) × 5 × 5 matrix with the patches that were sampled from the input image (pyr{4})
% Dists ? (m ? 4) × (n ? 4) × 3 matrix with the distances returned from findNearestNeighbors.
% pyr ? 7 × 1 cell created using createPyramid
% dbPatchesStd ? (m ? 4) × (n ? 4) × 3 matrix with the STDs of the neighbors patches returned from
%findNearestNeighbors.
%
% Outputs:
% weights ? (m ? 4) × (n ? 4) × 3 matrix with the weights for each DB candidates
%

patchSize = 5;
%• Get the two translated images from the input image using the supplied translateImageHalfPixel
[translatedX, translatedY] = translateImageHalfPixel(pyr{4});

%• Sample patches from each image using yours samplePatches with border=0
[p_x,P_y,patchesT_X] = samplePatches(translatedX, 0);
[P_x,p_y,patchesT_Y] = samplePatches(translatedY, 0);

%• Calculate the euclidean distance between each input patch to its two translated versions.

patchesT_X = reshape(permute(reshape(patchesT_X, numel(p_x),patchSize,patchSize),[2,3,1]),patchSize^2,numel(p_x));

patchesT_Y = reshape(permute(reshape(patchesT_Y, numel(p_y),patchSize,patchSize),[2,3,1]),patchSize^2,numel(P_y));

imPatches = reshape(permute(reshape(imPatches, numel(P_x),patchSize,patchSize),[2,3,1]),patchSize^2,numel(P_x));



%• Set threshold to be the average between the two calculated distances.

threshold = reshape((sqrt(sum((imPatches - patchesT_X).^2)) + sqrt(sum((imPatches - patchesT_Y).^2)))'/2,size(p_x));
threshold =  Dists(:,:,3) .* (Dists(:,:,3) < threshold);
threshold(threshold == 0) = inf;
Dists(:, :, 3) = threshold;

%• the weights
weights = exp(-((Dists.^2)./dbPatchesStd));
end