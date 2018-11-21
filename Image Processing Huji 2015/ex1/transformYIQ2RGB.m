function [imRGB] = transformYIQ2RGB(imYIQ)
Y = imYIQ(:,:,1);
I = imYIQ(:,:,2);
Q = imYIQ(:,:,3);

%multibly the image to the inverse matrix given in the lecture to conver
% back to RGB
ivr = inv([0.299 0.587 0.114; 0.596 -0.275 -0.321; 0.212 -0.523 0.311]);
imRGB(:,:,1) = ivr(1,1) .* Y + ivr(1,2) .*I + ivr(1,3) .* Q; 
imRGB(:,:,2) = ivr(2,1) .* Y + ivr(2,2) .*I + ivr(2,3) .* Q;
imRGB(:,:,3) = ivr(3,1) .* Y + ivr(3,2) .*I + ivr(3,3) .* Q;

end

