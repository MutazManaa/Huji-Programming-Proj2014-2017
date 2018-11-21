function [TrnErr,VldErr,minLambda,minTrnErr,minVldErr,falsePositive,falseNegative] = Part3()
trainSet = MapImgs('./dataset/train');
validSet = MapImgs('./dataset/valid');
trainGroup = TagImgs('./dataset/train');
validGroup = TagImgs('./dataset/valid');
onesVector = ones(size(trainSet,1),1);
trainX = [trainSet onesVector];
onesVector = ones(size(validSet,1),1);
validX = [validSet onesVector];
trainY = trainGroup;
trainY(trainGroup == 0) = -1;
validY = validGroup;
validY(validGroup == 0) = -1;

TrnErr = 0; 
VldErr = 0;
minLambda = Inf;
minVldErr = Inf;
index = 2;
for i = 4:0.1:10
    w = RidgeTrain(trainSet,trainY,10^i);
    ELVTrain = sign(trainX * w);
    ELVValid = sign(validX * w);
    TrnErr = [TrnErr sum(ELVTrain ~= trainY)/ length(trainY)];
    VldErr = [VldErr sum(ELVValid ~= validY)/ length(validY)];
    if VldErr(index) < minVldErr
        minLambda = i;
        minVldErr = VldErr(index);
        minTrnErr = TrnErr(index);
        
    end
    index = index + 1;
end
TrnErr = TrnErr(2:end);
VldErr = VldErr(2:end);

falsePositive = (ELVValid ~= validY) & (validY ==0);
falsePositive = find(falsePositive);
falseNegative = (ELVValid ~= validY) & (validY ==1);
falseNegative = find(falseNegative);


end






