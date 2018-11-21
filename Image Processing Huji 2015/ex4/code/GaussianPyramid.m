function [pyr, filter] = GaussianPyramid(im, maxLevels, filterSize)

filter = creatGauFilter(filterSize);%creating the filrer 
pyr = {[;]};%creat cell array as decribed in comment 1 in the exercise.

[rows,cols] = size(im);%get the image size
for i = 1:1:maxLevels % loop to save the pyramids in the cell array
    if (rows >= 16 && cols >= 16)% as described in the comments
        pyr{1,i} = im; %fill the pyramyds in the correct place in the cell array
        
        convRows = conv2(im, filter, 'same'); %conv with rows        
        convCols = conv2(convRows, filter', 'same'); %conv with colums
        
        im = convCols(1:2:end,1:2:end);%get the odd rows and cols
        
        rows = floor(rows ./ 2);%resize rows
        cols = floor(cols ./ 2);%resize cols
        
    end    
end

 


pyr = pyr';%get the cell array as col cell array

end


function [NormalizedKernel] = creatGauFilter(kernelSize)
kernel1DTemp = [1 1];
for i = 0:1:(kernelSize - 3) %overall (kernelSize - 2) times iteration
    kernel1DTemp = conv(kernel1DTemp, [1 1]);%one dimention convolution
end

%kernelCoeffMatrix = kernel1DTemp' * kernel1DTemp; %creat kernelsize Gausian 
sumToNormalize = sum(sum(kernel1DTemp));%to sum all matrix members
NormalizedKernel = kernel1DTemp / sumToNormalize;%normalize the kernel
end