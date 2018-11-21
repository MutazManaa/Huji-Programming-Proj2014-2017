function Htot = accumulateHomographies(Hpair,m)
% ACCUMULATEHOMOGRAPHY Accumulate homography matrix sequence.
% Arguments:
% Hpair ? Cell array of M?1 3x3 homography matrices where Hpair{i} is a
% homography that transforms between coordinate systems i and i+1.
% m ? Index of coordinate system we would like to accumulate the
% given homographies towards (see details below).
% Returns:
% Htot ? Cell array of M 3x3 homography matrices where Htot{i} transforms
% coordinate system i to the coordinate system having the index m.
% Note:
% In this exercise homography matrices should always maintain
% the property that H(3,3)==1. This should be done by normalizing them as
% follows before using them to perform transformations H = H/H(3,3).

N = size(Hpair,1) + 1;
Htot = cell(N,1);
Htot{m,1} = eye(3,3);

for i = m+1:N
    tempH = Htot{i-1,1} * (inv(Hpair{i-1,1}));
    Htot{i,1} = tempH / tempH(3,3);
end
for i = (m-1):-1:1
    tempH = Htot{i+1,1} * Hpair{i,1};
    Htot{i,1} = tempH / tempH(3,3);
end

end
