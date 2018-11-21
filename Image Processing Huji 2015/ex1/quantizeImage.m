function [imQuant, error] = quantizeImage(imOrig, nQuant, nIter)



[height width c] = size(imOrig);
if (c == 3) % %check if RGB formnat
    YIQIm = transformRGB2YIQ(imOrig); 
    imTemp = YIQIm(:,:,1) .* 255;
    imBin = uint8(imTemp);
    
else
    imBin = uint8(imOrig .* 255);
end  

error = [];
[histOrig, bins] = imhist(imBin);

%Optimal Quantization algorithim

%creat initial z as in the comments of the exercise
z = 0;
j = 1;

% calculate number of pixels per segment
imCumm = cumsum(histOrig);
numOfPixels = max(imCumm);
pixelsPerSegment = round(numOfPixels / nQuant);

%calqulating Z
for i = 1:(nQuant - 1)
    z = [z find(diff(imCumm < j .* pixelsPerSegment))];
    j = j + 1;
end
z = [z 255];

% if we have duplicates to fix them (STAK OVERFLOW)
if (length(z) ~= length(unique(z)))
    for i = 1:(nQuant - 1)
        if ( (z(i) >= z(i+1)) && (z(i) < 255) )
            z(i+1) = z(i) + 1;
   
        end
    end
end
%   If still needed, fix iterating to the left -
if (length(z) ~= length(unique(z)))
    for i = (nQuant + 1):-1:2
        if ( (z(i) <= z(i-1)) && (z(i) > 1) )
            z(i-1) = z(i) - 1;   
      
        end
    end
end

q = [];

for i = 1:nIter
    prevz = z;
    prevq = q;
    
    %calculating q
    [~,zmap] = histc(0:255, z);
    zmap(end) = zmap(end - 1);
    zmap = zmap';
    numerator = accumarray(zmap, (histOrig .* bins)');
    denominator = accumarray(zmap, histOrig);
    q = round(numerator ./ denominator);
    z = [0 round(mean([q(1:end-1), q(2:end)]')) 255];% calculate z
    
    [~,xmap] = histc(0:255, z);
    xmap(end) = xmap(end - 1);
    xmap = xmap';
    qMap = changem(xmap,q,1:nQuant);
    % use the equation given in the lecture
    err = sum(((qMap - bins).^2) .* histOrig);
    error = [error ; err]; 
    
    % check if z and previous iteration is equal or q with the prevous one
    % == operator doesn't work!!
    if (isequal(prevz,z) && isequal(prevq,q)) , break; 
    end
end

% End Of Optimal Quantization Algorithim. 
%--------------------------------------------------------------------------

%edit the Y chanel if the image is RGB
imQaTemp = im2double(changem(imBin, qMap, bins));
if (c == 3)     % image is RGB
    imQuYIQ = YIQIm;
    imQuYIQ(:,:,1) = imQaTemp;
    imQuant = transformYIQ2RGB(imQuYIQ);
    imQuant = uint8(imQuant .* 255);
else
    imQuant = imQaTemp;
end

%--------------------------------------------------------------------------
% Plot the images.

subplot(1,2,1), imshow(imOrig), title('Original Image');
subplot(1,2,2), imshow(imQuant), title('Quantized Image');
figure; plot(error), title('error of (nIter)');


end




