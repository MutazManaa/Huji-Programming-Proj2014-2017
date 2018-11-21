function [pos] = HarrisCornerDetector( im )
% HARRISCORNERDETECTOR Extract key points from the image.
% Arguments:
% im ? nxm grayscale image to find key points inside.
% pos ? A Nx2 matrix of [x,y] key points positions in im

Ix = conv2(im,[1 0 -1],'same'); %derivatve rows
Iy = conv2(im,[1;0;-1],'same');%derivatave colums

IxIx = Ix .* Ix;
IyIy = Iy .* Iy;
IxIy = Ix .* Iy;
%IyIx = Iy .* Ix;

blurIxIx = blurInImageSpace(IxIx,3);
blurIyIy = blurInImageSpace(IyIy,3);
blurIxIy = blurInImageSpace(IxIy,3);

detM = (blurIxIx .* blurIyIy) - (blurIxIy .* blurIxIy);
traceM = (blurIxIx + blurIyIy);
R = detM - 0.04 * (traceM).^2;

result = nonMaximumSuppression(R);
[row, col] = find(result);
pos = [col row];

end