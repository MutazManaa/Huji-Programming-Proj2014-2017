function [y]=getMusic(x,fs,pitch,harmonicsNumber,deltaF,overLap)

if nargin < 6
    deltaF = 0.01;
    overLap = 500;
end

y = ones(length(x),1);

NOF = size(pitch,1);% getting the number of frequencies
rowAdd = [length(x) / fs 0];
pitch = [pitch;rowAdd];
pichTimeIndex = round(pitch(:,1) * fs) + overLap;
Xpad = [zeros(overLap,1); x ;zeros(overLap,1)];

for i = 1:NOF
    f = pitch(i,2);
    Hd = makeFilter(f,fs,deltaF,harmonicsNumber);%creating filter
    strip= pichTimeIndex(i):pichTimeIndex(i + 1);
    %The overlapped strip    
    olStart = 1 + pichTimeIndex(i) - overLap;
    olEnd = pichTimeIndex(i + 1) + overLap;
        
    olStrip = olStart:olEnd;

   filteredOverlappedSignal = filter(Hd,1,Xpad(olStrip));
   y(strip) = filteredOverlappedSignal(end-length(strip) + 1:end);
end
%figure
%spectrogram(x,hamming(200),'yaxis');%
%title('hamming with length 200');
end
