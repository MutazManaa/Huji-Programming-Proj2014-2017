function[imYIQ] = transformRGB2YIQ(imRGB)

R = imRGB(:,:,1);
G = imRGB(:,:,2);
B = imRGB(:,:,3);

%multiply the RGB form by the matrix given in the lectur to convert to YIQ

imYIQ(:,:,1) = 0.299 .*R + 0.587 .*G + 0.114 .*B;
imYIQ(:,:,2) = 0.596 .*R - 0.275 .*G - 0.321 .*B;
imYIQ(:,:,3) = 0.212 .*R - 0.523 .*G + 0.311 .*B;

end


