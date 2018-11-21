function [magnitude] = fourierDerivative(inImage)
%%implementing the function by the algorithim in the Tirgul 3 slide 30

[M,N] = size(inImage);
FourierForInImage = DFT2(inImage); %comput DFT for the image

u = (0:1:M-1) .* ((2 * pi * 1i) / M);
v = (0:1:N-1) .* ((2 * pi * 1i) / N);
% Fix the vector's symmetry (0,1,...,u/2,...-1,0)
middleU = floor(M/2) - 1;
middleV = floor(N/2) - 1;
u(M:-1:M-middleU+1) = -u(1:middleU);%I didn't use fftishift
v(N:-1:N-middleV+1) = -v(1:middleV);

% Compute the x and y derivative of f
inImageFourierX = diag(u) * FourierForInImage;%span the U into diagonal Matrix.
inImageFourierY = FourierForInImage * diag(v);%span the V into diagonal Matrix.
dfX = IDFT2(inImageFourierX);%inverse by IDFT2
dfY = IDFT2(inImageFourierY);%inverse by IDFT2

magnitude = sqrt(dfX.^2 + dfY.^2);%magnitude computation

% showing the image
%imshow(magnitude);
imshow(log(1+abs(magnitude)));

end