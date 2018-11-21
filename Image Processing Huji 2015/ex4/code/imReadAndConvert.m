function [result] = imReadAndConvert(filename, representation)

im = imread(filename); % readin the image
if representation == 1 %check representation
    im = rgb2gray(im); %converto to gray
    result = im2double(im);  %convert im to doub
elseif representation == 2
    result = im2double(im);  

end

