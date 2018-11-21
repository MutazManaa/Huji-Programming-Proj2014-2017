function panorama = renderPanorama(im,H)
% RENDERPANORAMA Renders a set of images into a combined panorama image.
% Arguments:
% im ? Cell array of n grayscale images.
% H ? Cell array of n 3x3 homography matrices transforming the ith image
% coordinates to the panorama image coordinates.
% Returns:
% panorama ? A grayscale panorama image composed of n vertical strips that
% were backwarped each from the relevant frame im{i} using homography H{i}.


N = size(im,1);
TLCorner = zeros(2,N);
TRCorner = zeros(2,N);
BRCorner = zeros(2,N);
BLCorner = zeros(2,N);
PanoCorners = zeros(4,2,N);
for i = 1:N
    [imSizeY , imSizeX] = size(im{i});
    TLCorner(:,i) = [1,1];
    TRCorner(:,i) = [1,imSizeX];
    BRCorner(:,i) = [imSizeY , imSizeX];
    BLCorner(:,i) = [imSizeY,1];
   
    PanoCorners(:,:,i) = applyHomography([TLCorner(:,i),TRCorner(:,i),BRCorner(:,i),BLCorner(:,i)]',H{i});
end
XCenters = PanoCorners(:,1,:);
YCenters = PanoCorners(:,2,:);
Xmin = min(XCenters(:));
Xmax = max(XCenters(:));
Ymin = min(YCenters(:));
Ymax = max(YCenters(:));

PanoCenters = zeros(2,N);
PanoCenters(:,:) = (PanoCorners(1, :, :) + PanoCorners(3, :, :)) / 2;
PanoStrips = zeros(N + 1, 1);
PanoStrips(1) = Xmin;
PanoStrips(end) = Xmax + 1;
PanoStrips(2:end-1) = (PanoCenters(1:end-1,1) + PanoCenters(2:end, 1)) / 2;
[XPano,YPano] = meshgrid(Xmin:Xmax,Ymin:Ymax);
panorama = zeros(size(XPano));

for i=1:N  
    pos = [XPano(:) YPano(:)];
    pos1 = applyHomography(pos, inv(H{i}));    
    IPano = interp2(im{i}, pos1(:,1), pos1(:,2), 'linear');
    IPano(isnan(IPano)) = 0;
    IPano = reshape(IPano, size(panorama));
    if i > 1
        mask = zeros(size(panorama));
        mask(PanoStrips(i) <= XPano) = 1;
        cornersX = PanoCorners(:,1,i-1:i);
        cornersX = cornersX(:);
        minCornersX = min(cornersX);
        maxCornersX = max(cornersX); 
        XminBound = floor(minCornersX - Xmin) + 1;
        XmaxBound = floor(maxCornersX - Xmin) + 1;
        StripRange = XminBound:XmaxBound;
        Xmin = XmaxBound;
            
        panorama(:,StripRange) = pyramidBlending(IPano(:, StripRange),...
            panorama(:, StripRange),mask(:, StripRange),7, 5, 11);
    else
        panorama = Ipano;
            
   end
        
end
end