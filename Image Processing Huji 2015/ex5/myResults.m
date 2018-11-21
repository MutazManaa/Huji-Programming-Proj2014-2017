
ImRGB = imReadAndConvert('butterfly.png',2);
ImYIQ = rgb2ntsc(ImRGB);
Y = ImYIQ(:,:,1);
Y = superResolution(Y);
Q = imresize(ImYIQ(:,:,3),2,'cubic');
I = imresize(ImYIQ(:,:,2),2,'cubic');
ImYIQ = reshape([Y I Q], [size(Y,1) size(Y,2) 3]);
resizeRGB = imresize(ImRGB,2,'cubic');
HighResRGB = ntsc2rgb(ImYIQ);
figure; imshow(ImRGB); title('The original image');
figure; imshow(HighResRGB); title('The result of super resolution procedure');
figure; imshow(resizeRGB); title('An unsampled version(imresize)');

%================================================
ImRGB = imReadAndConvert('example3.jpg',2);
ImYIQ = rgb2ntsc(ImRGB);
Y = ImYIQ(:,:,1);
Y = superResolution(Y);
Q = imresize(ImYIQ(:,:,3),2,'cubic');
I = imresize(ImYIQ(:,:,2),2,'cubic');
ImYIQ = reshape([Y I Q], [size(Y,1) size(Y,2) 3]);
resizeRGB = imresize(ImRGB,2,'cubic');
HighResRGB = ntsc2rgb(ImYIQ);
figure; imshow(ImRGB); title('The original image');
figure; imshow(HighResRGB); title('The result of super resolution procedure');
figure; imshow(resizeRGB);  title('An unsampled version(imresize)');

%======================================================

ImRGB = imReadAndConvert('example4.jpg',2);
ImYIQ = rgb2ntsc(ImRGB);
Y = ImYIQ(:,:,1);
Y = superResolution(Y);
Q = imresize(ImYIQ(:,:,3),2,'cubic');
I = imresize(ImYIQ(:,:,2),2,'cubic');
ImYIQ = reshape([Y I Q], [size(Y,1) size(Y,2) 3]);
resizeRGB = imresize(ImRGB,2,'cubic');
HighResRGB = ntsc2rgb(ImYIQ);
figure; imshow(ImRGB);      title('The original image');
figure; imshow(HighResRGB); title('The result of super resolution procedure');
figure; imshow(resizeRGB);  title('An unsampled version(imresize)');

