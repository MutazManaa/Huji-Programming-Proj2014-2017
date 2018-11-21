function displayMatches(im1,im2,pos1,pos2,inliers)
% DISPLAYMATCHES Display matched pt. pairs overlayed on given image pair.
% Arguments:
% im1,im2 ? two grayscale images
% pos1,pos2 ? Nx2 matrices containing n rows of [x,y] coordinates of matched
% points in im1 and im2 (i.e. the i’th match’s coordinate is
% pos1(i,:) in im1 and and pos2(i,:) in im2).
% inliers ? A kx1 vector of inlier matches (e.g. see output of
% ransacHomography.m)
figure; imshow([im1 im2]); 
hold on;
pos2(:,1) = pos2(:,1) + size(im1,2);
plot(pos1(:,1), pos1(:,2), 'r.');
plot(pos2(:,1), pos2(:,2), 'r.');
inliersPos1 = pos1(inliers,:);
inliersPos2 = pos2(inliers,:);
plot([inliersPos1(:,1) inliersPos2(:,1)]', [inliersPos1(:,2) inliersPos2(:,2)]', 'y-');
temp = 1:size(pos1,1);
temp(inliers) = 0;
outliers = temp(temp~=0);
outLiersPos1 = pos1(outliers,:);
OutLiersPos2 = pos2(outliers,:);
plot([outLiersPos1(:,1) OutLiersPos2(:,1)]',[outLiersPos1(:,2) OutLiersPos2(:,2)]','b-');

hold off;
end
