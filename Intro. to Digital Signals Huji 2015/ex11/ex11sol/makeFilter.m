function [Hd] = makeFilter(f,fs,deltaF,harmonicsNumber,N)

%default of N = 500
if nargin < 5
    N = 200;
end

%normalizin the frequency
freqNormalize = f /(fs/2);

%creat vector of  bands 
bands = zeros(1,(2*harmonicsNumber));
C = 0;
for i = 1:2:(2*harmonicsNumber)
    C = C + 1;
    bands(i) = C * freqNormalize - deltaF/2;
    bands(i + 1) = C * freqNormalize + deltaF/2;
    
end
Hd = fir1(N,bands,'DC-0',hamming(N + 1));
end