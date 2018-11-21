function [p_x, p_y, patches] = samplePatches( im , border )
% SAMPLEPATCHES Sample 5 × 5 patches from the input image. You are allowed to use 2D loops here.
% Arguments:
% im ? a grayscale image of size m × n
% border ? An integer that determines how much border we want to leave in the image. For example: if border=0 the
%center of the first patch will be at (3,3), and the last one will be at (end?2,end?2), so the number of patches in this
%case is (m ? 4) × (n ? 4). But if border=1 the center of the first patch will be at (4,4) and the last one will be at (
%end?3,end?3). So in general, the number of patches is (m ? 2 · (2 + border)) × (n ? 2 · (2 + border)).
%
% outputs:
% p x ? (m ? 2 · (2 + border)) × (n ? 2 · (2 + border)) matrix with the x indices of the centers of the patches
% p y ? (m ? 2 · (2 + border)) × (n ? 2 · (2 + border)) matrix with the y indices of the centers of the patches
% patches? (m ? 2 · (2 + border)) × (n ? 2 · (2 + border)) × 5 × 5 the patches

[M,N] = size(im);
patchSize = 5;
Xcenters = (3 + border):(M - border - 2);
Ycenters = (3 + border):(N - border - 2);

[p_x,p_y] = meshgrid(Ycenters,Xcenters);

patches = zeros(size(Xcenters,2),size(Ycenters,2),patchSize,patchSize);

for i = Xcenters
    for j = Ycenters
        patches(i - border  - 2,j - border - 2,:,:) = im((i - 2):(i + 2),(j-2):(j+2));
    end
end

end

