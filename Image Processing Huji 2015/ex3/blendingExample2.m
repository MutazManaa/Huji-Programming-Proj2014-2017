function blendingExample2( )
sourceImage = imReadAndConvert('t2.jpg', 2);
targetImage = imReadAndConvert('s2.jpg', 2);
mask = imReadAndConvert('m2.jpg', 1);
imBlend=pyramidBlending(sourceImage,targetImage,mask,10,5,25);
figure;imshow(sourceImage) , title('Source');
figure;imshow(targetImage) , title('Target');
figure;imshow(mask) , title('Mask');
figure;imshow(imBlend) , title('Images Blending');
end