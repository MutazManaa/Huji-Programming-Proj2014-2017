function  [fourierSignal] = DFT(signal)
N = size(signal,2);
index = 0:1:N-1;
coefecientMatrix = index' * index; %coeefecint matrix of u and x
%computing the fouriel signal at scalar Multiply row by row of the coefficent matrix
fourierSignal = signal * exp((-2 * pi * i .* (coefecientMatrix))/N);
end

