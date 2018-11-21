function [errors,VldErr] = Part6()
load('datasets/letters.mat');
load('datasets/words.mat');
load('A.mat');
load('W_wrd.mat');

X = letters.test.X;
Y = letters.test.Y;
ValidX = {words.test.X};
ValidY = {words.test.Y};

T = 75000;
W = letter_hinge_SGD(X,Y, 10^-2.2, T);
errors = zeros(4,1);


for i = 1:size(ValidX,2)
    Yt = classify_letters(ValidX{i}, W); 
    errors(1) = errors(1) + sum(ValidY{i}~=Yt');
    
    Yt = classify_letters(ValidX{i}, W_wrd); 
    errors(2) = errors(2) + sum(ValidY{i}~=Yt');
    
    Yt = classify_word(ValidX{i}, W, A);
    errors(3) = errors(3) + sum(ValidY{i}~=Yt); 
        
    Yt = classify_word(ValidX{i}, W_wrd, A);
    errors(4) = errors(4) + sum(ValidY{i}~=Yt); 
end

VldErr = errors ./ numel(ValidX);

end