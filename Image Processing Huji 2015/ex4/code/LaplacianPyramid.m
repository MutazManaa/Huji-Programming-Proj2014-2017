function [pyr, filter] = LaplacianPyramid(im, maxLevels, filterSize)
[gauss,filter] = GaussianPyramid(im, maxLevels, filterSize);
filter = filter * 2; 
gauss = gauss';
sizePyrGauss = size(gauss,2);
pyr{1,sizePyrGauss} = gauss{end};
k = sizePyrGauss; 
while k > 1 
    gaussIamge = gauss{1,k};
    previosGauss = gauss{1,k-1};
    [rows,cols] = size(previosGauss);
    padImage = zeros([rows,cols]);
    padImage(1:2:end,1:2:end) = gaussIamge;
    rowConv = conv2(padImage, filter,'same');   
    colsConv = conv2(rowConv, filter','same');   
    previosGauss = gauss{1,k - 1};
    pyr{1,k - 1} = previosGauss - colsConv;
    k = k - 1;
end
filter = filter / 2; 
pyr = pyr';
end
