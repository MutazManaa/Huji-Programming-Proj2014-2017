function [W] = letter_hinge_SGD(X, Y, lambda, T)
[m,d] = size(X);
k = 26;
prevW = zeros(k,d);
W = zeros(k,d);
labelsMatrix = repmat(1:k,length(Y),1) ~= repmat(Y,1,k);
for t = 1:(T-1)
    random = randsample(m,1);
    featuresValue = labelsMatrix(random,:)' + prevW * X (random,:)';
    [~, argmaxFeture] = max(featuresValue);
    SG = zeros(k,d);
    SG(Y(random),:) = -X(random,:);
    SG(argmaxFeture,:) = SG(argmaxFeture,:) + X(random,:);
    W = W + (T)^(-1) * prevW;
    prevW = (1- (2*(t)^(-1))) * prevW - (lambda*t)^(-1) * SG; 
end
end