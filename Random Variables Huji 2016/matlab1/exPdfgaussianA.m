function exPdfgaussianA
mu = [0;0];
N = 100000;

[P, ~] = size(mu);
X = randn(P, N);

x1 = X(1,:);
x2 = X(2,:);
plot(x1, x2, '.');

title('Sampels Before Transformation - i.i.d');

xlim([-10 10]);
ylim([-10 10]);

xlabel('X_1');
ylabel('X_2');

end