function [RdVectors] = MapImgs(dataPath)
files = dir(dataPath);
im = imread(strcat(dataPath,'/',files(3).name));
[rows,cols] = size(im);
RdVectors = zeros(length(files)-2,rows*cols);
RdVectors(1,:) = im(:);
for i = 4:length(files)
    im = imread(strcat(dataPath,'/',files(i).name));
    RdVectors(i-2,:) = im(:);   
end
end