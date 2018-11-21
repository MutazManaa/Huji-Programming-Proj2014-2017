function [result] = imDisplay(filename, representation)
result = imReadAndConvert(filename, representation);
figure;
imshow(result); %showing the image
impixelinfo; %showing the pixel information
end