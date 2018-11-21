function displayPyramid(pyr,levels)
%disp(levels);
%disp(size(pyr,1));
if (levels <=  size(pyr,1))
    figure; imshow(renderPyramid(pyr,levels));
else
     figure; imshow(renderPyramid(pyr,size(pyr,1)));
end
end