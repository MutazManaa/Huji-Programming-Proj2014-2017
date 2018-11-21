function [TrnErr,VldErr,minLambda,minTrnErr,minVldErr,minClassifyValidY] = Part4()
load('datasets/letters.mat');
trainX = letters.train.X;
trainY = letters.train.Y;
validX = letters.test.X;
validY = letters.test.Y;
T = 75000;
TrnErr = 0;
VldErr = 0;
minVldErr = inf;
minTrnErr = inf;
minLambda = Inf;
index = 2;
for i = -3:0.1:3;
    W = letter_hinge_SGD(trainX, trainY, 10^i, T);
    classifyTrainX = classify_letters(trainX, W);
    classifyValidY = classify_letters(validX, W);
    TrnErr = [TrnErr sum(classifyTrainX' ~= trainY)/ length(trainY)];
    VldErr = [VldErr sum(classifyValidY' ~= validY)/ length(validY)];
    if VldErr(index) < minVldErr
        minLambda = i;
        minVldErr = VldErr(index);
        minTrnErr = TrnErr(index);
        minClassifyValidY = classifyValidY;
        
    end
    index = index + 1;
end

TrnErr = TrnErr(2:end);
VldErr = VldErr(2:end);



end