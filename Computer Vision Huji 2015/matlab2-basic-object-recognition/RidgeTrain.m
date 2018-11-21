function [w] = RidgeTrain(X, y, lambda)
X = [X ones(size(X,1),1)];
w = (X'*X + 2*lambda*eye(size(X,2)))\(X' * y);
end