% Read the sound file by wavread, devide the signal using the SNR ratio (when the 
% signal > SNR ,it is a tone starting).
% Send the tone to IdentifyPhoneTone to get the number the tone represents,
% and adding it to the sequence of numbers that have been identified.

function [seq]=identifyPhoneSeq(filename)

[y,Fs,tmp] = wavread(filename);

N = length(y);
i = 1;
snr = 0.5;
toneLength = round(Fs*0.1);
y = y./max(y);
seq=[];

while (N-(toneLength)) > i
    
    if y(i) > snr
        toneEnd = toneLength + round(Fs*0.05) + i;
        if ((toneEnd-N) > 0) 
            toneEnd=N;
        end
        
        [number]=identifyPhoneTone(y(i:toneEnd),Fs);
        [seq]=[seq number];
        % Next tone
        i=toneEnd; 
    end
    
    i = i + 1;
    
end
