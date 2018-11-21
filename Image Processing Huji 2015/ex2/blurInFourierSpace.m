function [blurImage] = blurInFourierSpace(inImage,kernelSize)
%assume that the kernel size is odd

kernel1DTemp = [1 1];
for i = 0:1:(kernelSize - 3) %overall (kernelSize - 2) times iteration
    kernel1DTemp = conv(kernel1DTemp, [1 1]);%one dimention convolution
end
%display(kernel1DTemp);
kernelCoeffMatrix = kernel1DTemp' * kernel1DTemp; %creat kernelsize Gausian 
sumToNormalize = sum(sum(kernelCoeffMatrix));%to sum all matrix members
NormalizedKernel = kernelCoeffMatrix / sumToNormalize;%normalize the kernel

[m,n] = size(inImage);%get the image size
g = zeros(m,n); %pad the kernel g with zeros

%centralize the gausian on the Zeros Matrix g. 
middle = floor(kernelSize/2);
g(floor(m/2)+1-middle:floor(m/2)+1+middle,...
  floor(n/2)+1-middle:floor(n/2)+1+middle) = NormalizedKernel;

F = DFT2(inImage);%fourier 2D for the original image
G = DFT2(g);%fourier 2D for the gausian the paded with zeros
FDotG = F .* G; %multiply the two fourier transforms pointwise

blurImage = ifftshift(IDFT2((FDotG)));%using ifftshift to centralize the gausian at the image

subplot(1,2,1), imshow(inImage), title('InImage');
%subplot(1,2,2), imshow(blurImage), title('Blurred Image');
subplot(1,2,2), imshow(log(1+abs(blurImage))), title('BlurImage');

end



