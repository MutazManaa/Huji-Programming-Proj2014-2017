% create_gaussian_points function get the following parameters:
% mu: vector with length p.
% covariance: matrix with size PxP.
% N: num of vectors we should generate.
% and return x: matrix with size PxN of Gaussian random variables, so that
%each column represent a vector with expectation equal to mu and Covariance
% matrix equal to the input matrix coveriance.

function [X] = create_gaussian_points(mu, covariance, N)
% using size function to get the P - length of the input vector:
[P, ~] = size(mu);

% generating matrix X with size PxN :
X = randn(P, N);

% using eig function to calculate eigenVectors and eigenValues of the input
% covariance matrix
[eigenVector, eigenValue] = eig(covariance);

% applying diagonal transformation
X = (eigenValue.^0.5) * X;

% applying the transformation on X
A = eigenVector * eigenValue.^0.5;
b = repmat(mu, 1, N);
X = A * X + b ;

end
