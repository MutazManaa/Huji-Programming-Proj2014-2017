function [signal] = IDFT(fourierSignal)
N = size(fourierSignal,2);
index = 0:1:N-1;
coefecientMatrix = index' * index;
%the same like DFT2 but without the minus on the exponent
signal = fourierSignal * exp((2 * pi * i .* (coefecientMatrix)) / N);
signal = signal ./ N;
end
