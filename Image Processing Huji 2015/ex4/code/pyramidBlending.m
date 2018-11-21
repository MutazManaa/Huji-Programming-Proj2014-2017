function imBlend = pyramidBlending(im1, im2, mask, maxLevels, filterSizeIm, filterSizeMask)  

    if numel(size(im1)) == 3 
		imBlend = im1;
		for i = 1:3
			imBlend(:,:,i) = pyramidBlendingFunction(im1(:,:,i), im2(:,:,i), mask, maxLevels, filterSizeIm, filterSizeMask);
		end
    else 
                imBlend = pyramidBlendingFunction(im1, im2, mask, maxLevels,filterSizeIm, filterSizeMask);

    end  
end



function imBlend = pyramidBlendingFunction(im1, im2, mask, maxLevels, filterSizeIm, filterSizeMask)
    [l1 ,filter] = LaplacianPyramid(im1, maxLevels,filterSizeIm );
    l2  = LaplacianPyramid(im2, maxLevels,filterSizeIm );
	mask = im2double(mask);
    GM = GaussianPyramid(mask, maxLevels, filterSizeMask );
    
    levels = numel(l1);
    l_out = cell(levels, 1);
    for k = 1:levels
        l_out{k} = GM{k} .* l1{k} + (1 - GM{k}) .* l2{k};
    end
    imBlend=LaplacianToImage(l_out,filter,ones(levels,1));  
end