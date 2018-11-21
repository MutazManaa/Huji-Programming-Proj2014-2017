
function exPdfBernoulli
    figure;
    
    leftLimit = -5 ;
    rightLimit = 5;
    % how does delta impacts our estimation?
    deltaVec = [ 1,0.5,0.25,0.1];

    for i = 1:4
        subplot(2,2,i);
        estimatedPdf(50000,leftLimit,rightLimit,deltaVec(i));
    end
    
    % how does the number of samples we take from the iid vector
    % impacts our estimation ?
    figure;
    
    Nvec = [ 100,1000,10000,1000000];
    
    for i = 1:4
        subplot(2,2,i);
        estimatedPdf(Nvec(i),leftLimit,rightLimit,1);
    end
end


% this function will plot the estimation of the pdf of Bernoulli r.v. 
function estimatedPdf(numberOfPoints,leftLimit,rightLimit,delta)

% numberOfPoints = are the number of i.i.d variables that will be generated
% in order to estimate the pdf
% The histogram that will estimate the pdf will be created using bins in
% the following points :
% leftLimit,leftLimit+delta,leftLimit+2*delta...RightLimit

% first step : generating Bernoulli i.i.d random variables using binornd 
% function(because Bernoulli r.v. is a Binomial r.v. with  number of
% experiance (n) equal to 1.
Xvec = binornd(1, 0.25, 1, numberOfPoints);

bins = [leftLimit:delta:rightLimit];
[yvalues,xvalues] = hist(Xvec,bins);

% we normalize to produce the probability densities
yvalues = yvalues/(numberOfPoints.*delta);
bar(xvalues,yvalues);
z = [leftLimit-delta/2:delta/10:rightLimit+delta/2];
% binopdf() suitable to Bernoulli r.v. 
realPdf = inline('binopdf(z, 1, 0.25)','z');
hold on;
plot(z, realPdf(z), 'red');
xlabel('x');
ylabel('f_X(x)');
title(sprintf('Delta = %f , numOfPoints = %d', delta, numberOfPoints));
end



