function blendingExample1( )
sourceImage = imReadAndConvert('s1.jpg', 2);
targetImage = imReadAndConvert('t1.jpg', 2);
mask = imReadAndConvert('m1.jpg', 1);
imBlend=pyramidBlending(sourceImage,targetImage,mask,3,9,30);
figure;imshow(sourceImage) , title('Source');
figure;imshow(targetImage) , title('Target');
figure;imshow(mask) , title('Mask');
figure;imshow(imBlend) , title('Images Blending');
end