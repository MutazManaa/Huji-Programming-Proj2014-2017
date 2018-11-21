function [Y] = classify_word(X, W, A)
classifyLetters = W*X';
[k,T] = size(classifyLetters);
Y = zeros(T,1);
P = zeros(k,T);  
for i = 2:1:T
    [maxClassiffy,maxClassifyIndex] = max(repmat(classifyLetters(:,i-1),1,k) + A) ;
    P(:,i) = maxClassifyIndex';
    classifyLetters(:,i) = classifyLetters(:,i) + maxClassiffy';
end

[~, Y(T)] = max(classifyLetters(:,T));
for t = T-1:-1:1
    Y(t) = P(Y(t+1),t+1);
end
end