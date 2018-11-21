%PART 1 Q.3 --> A:

% the LMMSE_Estimation function that will applies the monte carlo simulation on the LMMSE estimator of x (from y),
% and then it will calculate the matrix of error covariance.

function LMMSE_Estimation (H, varOfN ,m)
% H: matrix 4x4 - that model the entrance to Linear deterministic channel.
% varOfN: var of the noise N.
%m: number of realization.
% NOTE : we know the values alredy from the question but we give them to the function as partameters to help us use it in question 4!!

%first of all we must compute the transpose matrix of matrix H.
transposeH = H' ; 

%define the c matrix of size 4x4, and initialize it with the values 0:
C = zeros (4,4) ;

%now we must do m iteration (like the number of realization) so we use while , and define a counter that till us  the num of current iteration :
currentRealizationCounter = 0 ;
while currentRealizationCounter < m

%in each iteration we randomely generateing X & N , then we can calculate (1) Y = HX + N (2) LMMSE estimator of x (from y) using result we got in Q.1

%randomely generateing X:
X = randsrc (4,1);

%randomely generate N:
N = randn (4,1);

%calculating Y:
Y = (H * X) + N;

%calculating the LMMSE estimator of x (from y) using result we got in Q.1:
XLMMSE = transposeH * (inv(H.^2 + varOfN) * Y);

%we must calculate in each iteration the error vector (erroeVec) and the matrix in this iteration (Ci) so we can use them after the loop to estimate the C:

%computing the error vector depending on what we see in lecture:
errorVec = X - XLMMSE;

%computing the ci matrix:
Ci = errorVec * errorVec' ;

%adding the current matrix to the main matrix C:
C = C + Ci ;

%time for next iteration so :
currentRealizationCounter = currentRealizationCounter + 1;

end

% lets compute the C matrix we got from monte carlo and the analytic one from Q.2:

%C from monte carlo - all we have to do is to divide the sum of C from all iterations ( the Ci-es) by the num of simulations:
C = C ./ m;

% computin the analytic C :
I = eye(4);
analyticC = I - transposeH * inv(H.^2 + varOfN) * H;

%printing the two matrix:
display(C);
display(analyticC);

end