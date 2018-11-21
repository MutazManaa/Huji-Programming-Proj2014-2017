function [magnitude] = convDerivative(inImage)

%conv into 2 dimension
conVectorX = [1 0 -1];
conVectorY = [1;0;-1];
dfX = conv2(inImage, conVectorX, 'same');
dfY = conv2(inImage, conVectorY, 'same');
magnitude = sqrt( dfX.^2 + dfY.^2 );%magnitude computation
imshow(magnitude);%showing the image

end