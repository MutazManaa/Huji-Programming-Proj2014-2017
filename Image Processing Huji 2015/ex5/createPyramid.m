
function pyr = createPyramid( im )
% CREATEPYRAMID Create a pyramid from the input image, where pyr{1} is the smallest level,
% pyr{4} is the input image, and pyr{5},pyr{6},pyr{7} are zeros.
% The ratio between two adjacent levels in the pyramid is 2(1/3)
% Arguments:
% im ? a grayscale image
%
% outputs:
% pyr ? A 7 × 1 cell of matrices

factor = 2^(1/3);
pyr = cell(7,1);
pyr{4,1} = im;
for i = 3:-1:1
    pyr{i+4,1} = zeros(round(size(im)*(factor^i)));
    pyr{i,1} = imresize(im,factor^(i -4));
end
end


