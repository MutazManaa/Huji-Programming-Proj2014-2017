function [fourierImage] = DFT2(image)
fourierImage = DFT((DFT(image).')).';%using DFT twice on the inverses Matrix
end