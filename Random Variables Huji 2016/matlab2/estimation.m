%part.2, Q.5:
%function that will estimate thr probability density function of
%X(n) for the values n = 1,2,3,n1,n2,n3: 
function values = estimation(realization,one,two,three,n1,n2,n3)

%matrix of the 6 n values:
Xn = [one,two,three,n1,n2,n3];
for i = 1:6
    n = Xn(i);
    % calculating the An and delta using equation we got from previous
    % questions:
    An = ((2.^n)-1)/(2.^(n-1));
    delta = 2.^(2-n);
    
    % deciding the format of printing:
    subplot(3,2,i);]
    
    %computing the elements of Sn:
    values = [-An:delta:An];
    valuesSize = numel(values);
    j = randsample(valuesSize,realization,true);
    Xvec = values(j(1:realization));
    
    %computing the bins of histogram:
    bins = [-An:delta:An];
    
    % drawing the histogram:
    [Yval,Xval] = hist(Xvec,bins);
    Yval = Yval/realization;
    bar(Xval,Yval);
    xlabel(sprintf('X[%d]',n));
    ylabel(sprintf('Px(X[%d]=x)',n));
end 
end
