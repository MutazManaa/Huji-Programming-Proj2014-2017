function exPdf_display_conditional_pdf(x, x2, delta, mu, covariance, leftLimit, rightLimit, binWidth)
mu = [0;0];
covariance = [4,1.5;1.5,1];
N = 100000;
x = create_gaussian_points(mu, covariance, N);
x2 = 0.1;
delta = 0.2;
leftLimit = -8;
rightLimit = 8;
binWidth = 0.2;
display_conditional_pdf11(x, x2, delta, mu, covariance, leftLimit, rightLimit, binWidth);
end