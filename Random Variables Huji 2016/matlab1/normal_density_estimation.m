% This example is based on example 3.9 in the book : 
% Probability and Random Processes /S.L.Miller and D.G.Childers
% ( Elsevier Academic Press 2004)
function exPdfGaus
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
    delta = 0.25;
    Nvec = [ 100,1000,10000,1000000];
    
    for i = 1:4
        subplot(2,2,i);
        estimatedPdf(Nvec(i),leftLimit,rightLimit,delta);
    end
    

function estimatedPdf(numberOfPoints,leftLimit,rightLimit,delta)
% this function will plot the estimation of the pdf of normal r.v. 
% with expectation = 0 and variance = 1
% numberOfPoints = are the number of iid variables that will be generated in
% order to estimate the pdf
% The histogram that will estimate the pdf will be created using bins in
% the following points :
% leftLimit,leftLimit+delta,leftLimit+2*delta...RightLimit
        Xvec = randn(1,numberOfPoints); % this is the vector of iid variables' values
        bins = [leftLimit:delta:rightLimit];
        [yvalues,xvalues]=hist(Xvec,bins);
        yvalues = yvalues/(numberOfPoints.*delta); % we normalize to produce the probability densities
        bar(xvalues,yvalues);
        z = [ leftLimit-delta/2:delta/10:rightLimit+delta/2];
        realPdf = inline(' (exp(-(z.^2)./2))./sqrt(2*pi)','z');
        hold on;
        plot(z,realPdf(z),'red');
        xlabel('x');
        ylabel('f_X(x)');
        title(sprintf(' d = %f , #pnts = %d ',delta,numberOfPoints));
