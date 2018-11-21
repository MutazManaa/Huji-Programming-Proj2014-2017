function [TrnErr,VldErr,falsePositive,falseNegative] = Part1()
trainSet = MapImgs('./dataset/train');
validSet = MapImgs('./dataset/valid');
trainGroup = TagImgs('./dataset/train');
validGroup = TagImgs('./dataset/valid');

SVMStruct = svmtrain(trainSet,trainGroup);
trainClassify =  svmclassify(SVMStruct,trainSet);
TrnErr = sum(trainClassify ~= trainGroup);
TrnErr = TrnErr / length(trainGroup);
validClassify =  svmclassify(SVMStruct,validSet);
VldErr = sum(validClassify ~= validGroup);
VldErr = VldErr / length(validGroup);

falsePositive = (validClassify ~= validGroup) & (validGroup ==0);
falsePositive = find(falsePositive);
falseNegative = (validClassify ~= validGroup) & (validGroup ==1);
falseNegative = find(falseNegative);

end