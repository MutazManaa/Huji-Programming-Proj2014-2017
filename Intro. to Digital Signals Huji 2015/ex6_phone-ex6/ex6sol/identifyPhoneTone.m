% First use FFT, to get a vector of the same length.
% to identify the number that the signal represents, we use the formula
% F = Fs * (k/N), Fs is given, N is the length of the vector ,
% F is the signal, then we can get k from this formula.
% After we get k, look for the number in the k indexs,
% we use the DFT to identify the number that the signal represents.

function [number] = identifyPhoneTone(x,Fs)

%the phone pad
pad = [1 2 3; 4 5 6; 7 8 9; 10 0 11];

[N, tmp] = size(x);

%rows & cols frequency
rowsFre = [697,770,852,941];
colsFre = [1209,1336,1477];

Xf = abs(fft(x));
%averaging
Xf = ((Xf+circshift(Xf,-1) + circshift(Xf,1))/3);

rowsVals = Xf(round(rowsFre*N/Fs)+1);
colsVals = Xf(round(colsFre*N/Fs)+1);

%get the input tone row
[tmp,row] = max(rowsVals);

%get the input tone col
[tmp,col] = max(colsVals);

number = pad(row,col);

end