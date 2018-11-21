%part.2, Q.7:
function randEstimation(realization,one,two,three,n1,n2,n3)
Xn = [one,two,three,n1,n2,n3];
for i = 1:6
     n = Xn(i);
     An1 = ((2.^n)-2)/(-2.^(n-1));
     An2 = (2.^(n+1)+1)/(2.^n);
     subplot(3,2,i);
     Xvec = unifrnd(An1,An2,realization,1);
     bins = [An1:0.01:An2];
     [Yval,Xval] = hist(Xvec,bins);
     Yval = Yval/realization;
     bar(Xval,Yval);
     xlabel(sprintf('X[%d]',n));
    ylabel(sprintf('fx[%d](x)',n));
end
end


