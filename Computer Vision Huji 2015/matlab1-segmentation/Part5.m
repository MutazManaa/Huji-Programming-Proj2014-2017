
function [labels] = Part5(segmac,segmas,k,n,imPath)

origIm = imread(imPath);
origIm = im2double(origIm);
imLab = rgb2lab(origIm);
[X,Y] = meshgrid(1:size(imLab,2),1:size(imLab,1)); 
XY = zeros(size(imLab,1) * size(imLab,2), 2);
XY(:,1) = X(:);
XY(:,2) = Y(:);
random = randi ([1 size(imLab,2)*size(imLab,1)],1,n);
imTemp = reshape(imLab,size(imLab,1) * size(imLab,2),3);
choosenXY = XY(random,:);
vectrosLabeled = imTemp(random,:);
 A = exp((pdist2(vectrosLabeled,vectrosLabeled).^2 * 1/(-2* (segmac^2)))...
     + (pdist2(choosenXY,choosenXY).^2 * 1/(-2* (segmas^2))));
 
 otherVector = setdiff(1:size(imLab,2)*size(imLab,1),random);
 
 B = exp((pdist2(vectrosLabeled,imTemp(otherVector,:)) * 1/(-2* (segmac^2)))...
     + (pdist2(choosenXY, XY(otherVector,:)) * 1/(-2* (segmas^2))));
 
 labels = NystromNCuts(A,B,k);
 
allLabels = zeros(size(imLab,2)*size(imLab,1),1);
allLabels(random) = labels(1:n);
allLabels(otherVector) = labels(n+1:end);
labels = reshape(allLabels,size(imLab,1),size(imLab,2));

end




