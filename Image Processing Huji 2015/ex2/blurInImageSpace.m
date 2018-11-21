function [blurImage] = blurInImageSpace(inImage,kernelSize)

kernel1DTemp = [1 1];
for i = 0:1:(kernelSize - 3) %overall (kernelSize - 2) times iteration
    kernel1DTemp = conv(kernel1DTemp, [1 1]);%one dimention convolution
end

kernelCoeffMatrix = kernel1DTemp' * kernel1DTemp; %creat kernelsize Gausian 
sumToNormalize = sum(sum(kernelCoeffMatrix));%to sum all matrix members
NormalizedKernel = kernelCoeffMatrix / sumToNormalize;%normalize the kernel

%convoluation in 2D
blurImage = conv2(inImage, NormalizedKernel, 'same');%convoluation in two 2D

end
