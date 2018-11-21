function [TrnErr,VldErr,minT,minTrnErr,minVldErr,falsePositive,falseNegative] = Part7()
[trainSet,rows,cols] = MapImgsToHOG('./dataset/train');
[validSet,rows,cols] = MapImgsToHOG('./dataset/valid');
trainGroup = TagImgs('./dataset/train');
validGroup = TagImgs('./dataset/valid');
trainY = trainGroup;
trainY(trainGroup == 0) = -1;
validY = validGroup;
validY(validGroup == 0) = -1;
trainClassify = zeros(length(trainY), rows);
validClassify = zeros(length(validY), rows);
for i=1:rows
    trainSetAdabost = trainSet(:,1 + (i-1)*cols:i*cols);
    validSetAdabost = validSet(:,1 + (i-1)*cols:i*cols);
    SVMStruct = svmtrain(trainSetAdabost,trainY);
    trainClassify(:,i) = svmclassify(SVMStruct ,trainSetAdabost);
    validClassify(:,i) = svmclassify(SVMStruct,validSetAdabost);  
end
 
TrnErr = 0;
VldErr = 0;
minVldErr = inf;
minTrnErr = inf;
minT = 0;
for T = 1:20
    [y_est, h] = adaboost('train', trainClassify, trainY, T);
    validClassifyAdaboost = adaboost('apply', validClassify, h);
    TrnErr = [TrnErr sum(y_est ~= trainY)/ length(trainY)];
    VldErr = [VldErr sum(validClassifyAdaboost ~= validY)/ length(validY)];
    if VldErr(T+1) < minVldErr
        minT = T;
        minVldErr = VldErr(T+1);
        minTrnErr = TrnErr(T+1);
        idealvalidClassifyAdaboost = validClassifyAdaboost;
    end
end

TrnErr = TrnErr(2:end);
VldErr = VldErr(2:end);


falsePositive = (idealvalidClassifyAdaboost ~= validY) & (validY==0);
falsePositive = find(falsePositive);
falseNegative = (idealvalidClassifyAdaboost ~= validY) & (validY==1);
falseNegative = find(falseNegative);

end

