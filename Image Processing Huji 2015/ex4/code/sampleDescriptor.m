function desc = sampleDescriptor(im,pos,descRad)
% SAMPLEDESCRIPTOR Sample a MOPS?like descriptor at given positions in the image.
% Arguments:
% im ? nxm grayscale image to sample within.
% pos ? A Nx2 matrix of [x,y] descriptor positions in im.
% descRad ? ”Radius” of descriptors to compute (see below).
% Returns:
% desc ? A kxkxN 3?d matrix containing the ith descriptor
% at desc(:,:,i). The per?descriptor dimensions kxk are related to the
% descRad argument as follows k = 1+2?descRad.

k = 1 + 2*descRad;
N = size(pos,1);
desc = zeros(k,k,N);
L1 = 1;
L3 = 3;
PL1 = pos;
PL3 = (2.^(L1 - L3)) * (PL1 - 1) +1;% regard to equation in the exercise

for i = 1:N
    [x,y] = meshgrid((PL3(i,1) - descRad):(PL3(i,1) + descRad),...
        (PL3(i,2) - descRad):(PL3(i,2) + descRad));
    dGal = interp2(im,x,y,'linear',0);%interpolation
    Mue = mean2(dGal);%average
    d = dGal - Mue;
    desc(:,:,i) = d ./ norm(d(:));
end

end