function [ind1,ind2] = matchFeatures(desc1,desc2,minScore)
% MATCHFEATURES Match feature descriptors in desc1 and desc2.
% Arguments:
% desc1 ? A kxkxn1 feature descriptor matrix.
% desc2 ? A kxkxn2 feature descriptor matrix.
% minScore ? Minimal match score between two descriptors required to be
% regarded as matching.
% Returns:
% ind1,ind2 ? These are m?entry arrays of match indices in desc1 and desc2.
%
% Note:
% 1. The descriptors of the ith match are desc1(ind1(i)) and desc2(ind2(i)).
% 2. The number of feature descriptors n1 generally differs from n2
% 3. ind1 and ind2 have the same length.


N1 = size(desc1,3);
N2 = size(desc2,3);
k = size(desc1,1);
desc1 = reshape(desc1,k*k,N1);
desc2 = reshape(desc2,k*k,N2);
matchEntyArray = desc1' * desc2;
matchEntyArray(matchEntyArray <= minScore) = 0;
matchEntyArray(isnan(matchEntyArray)) = 0;
SJK1 = sort(matchEntyArray, 1, 'descend');
SJK1 = SJK1(2,:);
SJK2 = sort(matchEntyArray, 2, 'descend');
SJK2 = SJK2(:,2);
[SJK2, SJK1] = meshgrid(SJK1, SJK2);
matchEntyArray(matchEntyArray < SJK1) = 0;
matchEntyArray(matchEntyArray < SJK2) = 0;
[ind1, ind2] = find(matchEntyArray);

end 