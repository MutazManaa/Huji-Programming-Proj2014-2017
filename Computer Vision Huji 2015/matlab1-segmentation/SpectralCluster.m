function labels = SpectralCluster(W, k, algo)

%working as the algo

D = diag(sum(W,1));
m = size(W,1);
I = eye(m);
if (strcmp(algo, 'rc') == 1)
    Wc = W - D + I;
else
    Wc = D^(1/2)* W * D^(1/2);
end

[eigVecs, eigVals] = eig(Wc);
[~,I] = sort(diag(eigVals),'descend');
I = I(1:k);
Y = eigVecs(:,I);
Y = Y ./ (repmat (sqrt(sum(Y.^2,2)), 1 ,k));
Y = Y';
[~, labels] = KMeans(Y, k, 95, 1, 15);


end