function [labels] = Part2(k, max_iter, change_thresh, num_runs, u, imPath)


origIm = imread(imPath);
labIm = double(rgb2lab(origIm));
[x,y] = meshgrid(1:size(origIm,2), 1:size(origIm,1));
X = cat(5,labIm(:,:,1), labIm(:,:,2), labIm(:,:,3),u * x ,u * y);
X = reshape(X,  numel(labIm(:,:,1)),5)'; 
[~, labels] = KMeans(X, k, max_iter, change_thresh, num_runs);
labels = (reshape(labels,size(origIm,1),size(origIm,2)));

end