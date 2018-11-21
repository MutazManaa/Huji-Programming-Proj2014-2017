function [img] = LaplacianToImage(lpyr, filter, coeffMultVec)
levels = size(lpyr,1);
img = coeffMultVec(levels) * lpyr{levels};
k = levels - 1 ;
while k  > 0
    padImage = zeros(size(img) * 2);
    padImage(1:2:end, 1:2:end) = img;
    padedIamge = padarray(padImage, [floor(max(size(filter))/2),floor(max(size(filter))/2)],'circular');
    blurred = conv2(conv2(padedIamge, filter, 'same'), filter', 'same') * 4;
    expandedImage = blurred(floor(max(size(filter))/2)+1:end-floor(max(size(filter))/2),  ...
    floor(max(size(filter))/2)+1:end-floor(max(size(filter))/2));
    [r, c] = size(lpyr{k});
    [rows, cols] = size(expandedImage);
    if  r < rows
        expandedImage = expandedImage(1:r, :); 
    end
    if  c < cols
        expandedImage = expandedImage(:, 1:c);
    end
    img = coeffMultVec(k) * lpyr{k} + expandedImage;
    k = k - 1;
end
end
