function exPdfgaussianB_and_C
mu = [0;0];
covariance = [4,1.5;1.5,1];
N = 100000;

[P, ~] = size(mu);
X = randn(P, N);

% using eig function to calculate eigenVectors and eigenValues of the input
% covariance matrix
[eigenVector, eigenValue] = eig(covariance);

% applying diagonal transformation
Z = (eigenValue.^0.5) * X;

% applying the transformation on X
A = eigenVector * eigenValue.^0.5;
b = repmat(mu, 1, N);
W = A * X + b ;

%Drawing Gaussian random variables after the diagonal transformation:
figure;
x1 = Z(1,:);
x2 = Z(2,:);
plot(x1, x2, '.');

title('Sampels After Diagonal Transformation');

xlim([-10 10]);
ylim([-10 10]);

xlabel('X_1');
ylabel('X_2');


%Drawing Gaussian random variables after the orthogonal transformation:
figure;
x1 = W(1,:);
x2 = W(2,:);
plot(x1, x2, '.');

title('Sampels After Orthogonal Transformation');

xlim([-10 10]);
ylim([-10 10]);

xlabel('X_1');
ylabel('X_2');

end