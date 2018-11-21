% x: matrix PxN we get from create_gaussian_points function.
% x2 = beta!
% delta: the chosen delta.
% mu: vector with length p.
% covariance: matrix with size PxP.
% leftLimit: limit in X-axis from left.
% rightLimit: limit in X-axis from right.
% binWidth: section width in X-axis.
function display_conditional_pdf(x, x2, delta, mu, covariance, leftLimit, rightLimit, binWidth)
% first row vector in x:
x1Vec = x(1,:);
% second row vector in x:
x2Vec = x(2,:);
% samples that uphold : |X2-BETA|<= delta:
passVector = abs(x2Vec - x2) <= delta;
% taking the suitable values in X1:
suitableInX1 = x1Vec(passVector);
%getting the size of legal points:
numOfLegalPoints =  length(suitableInX1);
%determine the bins:
bins = [leftLimit:binWidth:rightLimit];
% drawing the histograms:
[yValues,xValues] = hist(suitableInX1, bins);
yValues = yValues/(numOfLegalPoints.*binWidth);
bar(xValues,yValues);

% now we want to calculate the real conditional PDF , so we calculate first
% the covariances and variances:
sigmaX1 = sqrt(covariance(1,1));
sigmaX2 = sqrt(covariance(2,2));
sigmaX1X2 = covariance(1,2);
varianceX1 = covariance(1,1);
sigma = sigmaX1X2 / (sigmaX1 * sigmaX2);
mu1 = mu(1);
mu2 = mu(2);

% conditional Expectation:
conditionalE = mu1 + (sigma * (sigmaX1 / sigmaX2) * (x2 - mu2));

% conditional Variance:
conditionalV = (1 - (sigma.^2)) * varianceX1;

z = [leftLimit-binWidth/2:binWidth/10:rightLimit+binWidth/2];
function realPDF = f(z)
realPDF = exp((-1/(2*conditionalV)*(z-conditionalE).^2))/(sqrt(2*pi*conditionalV));
end
hold on;
plot(z,f(z),'red');
xlabel('x1');
ylabel('f:x1|x2=1.0');
end