function [labels] = Part4(segmac,segmas,k,algo,imPath,factor)

if nargin < 6
   factor = 1/8;
end

origIm = im2double(imread(imPath));
imResized = imresize(origIm,factor);
imLab = rgb2lab(imResized);
[X,Y] = meshgrid(1:size(imLab,2),1:size(imLab,1)); 

XY = zeros(size(imLab,1) * size(imLab,2), 2);
XY(:,1) = X(:);
XY(:,2) = Y(:);
imTemp = reshape(imLab,size(imLab,2)*size(imLab,1),3); 
W = exp((-1/(2*segmac^2)) * squareform(pdist(imTemp))...
        + (-1/(2*segmas^2)) * squareform(pdist(XY))); 
labels = SpectralCluster(W, k, algo);
labels = reshape(labels, size(imLab,1), size(imLab,2));

end