function [HOGVectors,rows,cols] = MapImgsToHOG(dataPath)
files = dir(dataPath);
im = double(imread(strcat(dataPath,'/',files(3).name)));
H = HOG(im,10,10,9);
im = cell2mat(H);
[rows,cols] = size(im);
HOGVectors = zeros(length(files)-2,rows*cols);
HOGVectors(1,:) = im(:);
for i = 4:length(files)
    im = double(imread(strcat(dataPath,'/',files(i).name)));
    H = HOG(im,10,10,9);
    im = cell2mat(H);
    HOGVectors(i-2,:) = im(:);   
end
end