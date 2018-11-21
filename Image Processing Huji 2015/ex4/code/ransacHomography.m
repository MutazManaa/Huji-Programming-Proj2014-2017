function [H12,inliers] = ransacHomography(pos1,pos2,numIters,inlierTol)
% RANSACHOMOGRAPHY Fit homography to maximal inliers given point matches
% using the RANSAC algorithm.
% Arguments:
% pos1,pos2 ? Two Nx2 matrices containing n rows of [x,y] coordinates of
% matched points.
% numIters ? Number of RANSAC iterations to perform.
% inlierTol ? inlier tolerance threshold.
% Returns:
% H12 ? A 3x3 normalized homography matrix.
% inliers ? A kx1 vector where k is the number of inliers, containing the indices in pos1/pos2 of the maximal set of
% inlier matches found.
N = size(pos1,1);
inliersSum = 0;

for i = 1:numIters
    randomIdxs = randperm(N,4);
    P1J = pos1(randomIdxs,:);
    P2J = pos2(randomIdxs,:);
    H12Temp = leastSquaresHomography(P1J,P2J);
    P1Tag = applyHomography(pos1,H12Temp);
    inliersTempSum = 0;
    inliersTemp = zeros(N);
    E = zeros(N);
    for j = 1:N
        E(j) = norm(P1Tag(j,:) - pos2(j,:))^2; 
        if (E(j) < inlierTol)
            inliersTempSum = inliersTempSum + 1; 
            inliersTemp(inliersTempSum) = j;
            
        end    
    end
    
    if(inliersSum < inliersTempSum)
        inliersSum = inliersTempSum;
        inliers = inliersTemp(1:inliersSum);
        H12 = H12Temp;
    end    
end

end