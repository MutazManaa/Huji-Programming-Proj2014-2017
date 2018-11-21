function [Y] = classify_letters(X, W)

[~,Y] = max(W*X');
end