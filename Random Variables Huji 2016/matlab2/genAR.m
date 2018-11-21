function [samplefunction]=genAR(X0,alpha,numberOfSamples)
%This method generates  a sample function X[1]...X[numberOfSamples] ,of alpha-A.R.
%process . i.e of the process that fulfills the following recursion:
%X[n]=alpha*X[n-1] + W[n]   n>=1
% W[n] is an i.i.d process that receives {1,-1} w.p. 0.5 each.
%X0 is the initial condition ( can be a column vector )
    Xlast = X0;
    samplefunction=[];
    for i=1:numberOfSamples
        Xlast=getNextValueInRecursion(Xlast,alpha); % X[n]= alpha*X[n-1] +W[n]
        samplefunction = [ samplefunction Xlast];
    end
        
function [nextVal]=getNextValueInRecursion(Xlast,alpha)
% This method generates the next value an alpha -A.R. might get when given 
% the last value it got. Example : 
% X_i+i = getNextValueInRecursion(X_i,alpha)
    nextVal = alpha.*Xlast + ( 2.*randint(length(Xlast),1)-1);