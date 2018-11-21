function [image] = IDFT2(fourierImage)
image = IDFT(IDFT(fourierImage).').';%using IDFT twice ..
end
