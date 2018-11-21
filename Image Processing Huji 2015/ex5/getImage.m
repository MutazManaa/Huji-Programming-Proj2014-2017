function [image] = getImage(assignmentPositionsX,assignmentPositionsY,samplingPositionsX,samplingPositionsY,weights...
,emptyHighResImage,renderedPyramid)
% GETIMAGE given an image of the rendered pyrmamid, sampling indices from the rendered pyrmamid, and
%assignment indices in the highres image return a high resolution image
% Arguments:
% assignmentPositionsX ? (m ? 4) × (n ? 4) × 3 × 5 × 5 x assignment coordinates in the high resolution image (
%getSamplingInformation output)
% assignmentPositionsY ? (m ? 4) × (n ? 4) × 3 × 5 × 5 y assignment coordinates in the high resolution image (
%getSamplingInformation output)
% samplingPositionsX ? (m ? 4) × (n ? 4) × 3 × 5 × 5 x sampling coordinates in the rendered pyramid image (
%getSamplingInformation output)
% samplingPositionsY ? (m ? 4) × (n ? 4) × 3 × 5 × 5 y sampling coordinates in the rendered pyramid image (
%getSamplingInformation output)
% weights ? (m ? 4) × (n ? 4) × 3 matrix with the weights for each DB candidate
% emptyHighResImage ? M × N zeros image, where M and N are the dimensions of a level in the pyramid that should
%be reconstructed in this function
% renderedPyramid ? a single image containing all levels of the pyramid
%
% Outputs:
% image ? M × N high resolution image
%
NeighboursNum = 3;                        
patcheSize=5; 

%if size of images not divide by 5 to fix
%emptyHighResImage = padarray(emptyHighResImage, [patcheSize - mod(size(emptyHighResImage,1),patcheSize)...
%    patcheSize-mod(size(emptyHighResImage,2),patcheSize)], 'post');
sumCandidtesWeight = emptyHighResImage;
for i = 1:NeighboursNum
    for j = 1:patcheSize
        for k = 1:patcheSize
            
            %sample 5*5 patches from samplingPositions and assignmentPositions
            Xsamples = reshapePositions(samplingPositionsX(j:patcheSize:end, k:patcheSize:end, i, :, :));
            Ysamples = reshapePositions(samplingPositionsY(j:patcheSize:end, k:patcheSize:end, i, :, :));
            SampleWeights = weights(j:patcheSize:end, k:patcheSize:end, i);
            SampleWeights = kron(squeeze(SampleWeights), ones(patcheSize));
            
            %we cant interolate one time but I didn't succes to do that :(
            samples = interp2(renderedPyramid, Xsamples, Ysamples, 'cubic',0);
            samples = samples.* SampleWeights;
            assX = reshapePositions(assignmentPositionsX(j:patcheSize:end, k:patcheSize:end, i, :, :));
            assY = reshapePositions(assignmentPositionsY(j:patcheSize:end, k:patcheSize:end, i, :, :));
            linearindexes = sub2ind(size(emptyHighResImage), assY(:), assX(:));
            emptyHighResImage(linearindexes) = emptyHighResImage(linearindexes) + samples(:);
            sumCandidtesWeight(linearindexes) = sumCandidtesWeight(linearindexes) + SampleWeights(:);
        end
    end
end
image = emptyHighResImage ./ sumCandidtesWeight;
%if not a number put zero
image(isnan(image)) = 0;
end

function [poss] = reshapePositions(positions)
%positions = permute(positions, [4 1 5 2 3]);
poss = reshape(permute(positions, [4 1 5 2 3]), size(positions, 1) * size(positions, 4),size(positions, 2) * size(positions, 5));
end