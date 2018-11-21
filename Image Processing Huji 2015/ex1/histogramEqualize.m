function [imEq, histOrig, histEq] = histogramEqualize(imOrig)

%assume we get double image.
%imOrig = im2double(imOrig);

[height width c] = size(imOrig); % get the format of the image in var C 

if (c == 3) %check if RGB formnat
   
    YIQIm = transformRGB2YIQ(imOrig); %transform to YIQ
    imTemp = YIQIm(:,:,1) .* 255; %get only the Y channel 
    imBin = uint8(imTemp); %convert to 8 bit representation
    
    
else
    imBin = uint8(imOrig .* 255);
end   
histOrig = imhist(imBin); %get histograma of the original image in the 8 bit
imCumm = cumsum(histOrig); % calculate the S(X) vector
numOfPixels = sum(histOrig); % get the whole number of pixels
normalizedHisto = imCumm ./ numOfPixels; % normalized the S(x) 
muNormalizedHisto = normalizedHisto .* 255; %convert to [0 - 255] range
roMuNormalizedHisto = round(muNormalizedHisto); %round the values

imEqTemp=changem(imBin,roMuNormalizedHisto,0:255);%map
histEq=imhist(imEqTemp);

imEqTemp = imadjust(imEqTemp, [double(min(roMuNormalizedHisto)/255) ... 
    double(max(roMuNormalizedHisto)/255)], [0 1]); 
%--------end of equalization algorithm--------


% edit the Y chanel if the image is RGB
if (c == 3)    
    imEqYIQ = YIQIm;
    imEqYIQ(:,:,1) = (double(imEqTemp) ./ 255);
    imEq = transformYIQ2RGB(imEqYIQ);
    imEq =  uint8(imEq .*255);
    imEq = im2double(imEq) ; 
else
    imEq = im2double(imEqTemp); 
end

%subplot

subplot(1,2,1), imshow(imOrig), title('Original Image');
subplot(1,2,2), imshow(imEq), title('Equlized Image');


end
