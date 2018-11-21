function [labels] = BonusPartI(k, max_iter, change_thresh, num_runs, u, imPath)

origIm = imread(imPath);
%labIm = double(rgb2lab(origIm));
intensities = im2double(origIm(:,:,1)) ./ 255;
[x,y] = meshgrid(1:size(origIm,2), 1:size(origIm,1));
X = cat(6,origIm(:,:,1), origIm(:,:,2), origIm(:,:,3),intensities ,u * x ,u * y);
X = reshape(X,  numel(origIm(:,:,1)),6)'; 
[~, labels] = KMeans(X, k, max_iter, change_thresh, num_runs);
labels = (reshape(labels,size(origIm,1),size(origIm,2)));
end