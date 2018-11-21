% this function will run the estimation of the pdf of Z2 r.v.
function exPdfZ2

  figure;
  
    leftLimit = -5 ;
    rightLimit = 5;
    
    estimatedPdf(100000,leftLimit,rightLimit,0.1);

end


% this function will plot the estimation of the pdf of normal r.v. 

function estimatedPdf(numberOfPoints,leftLimit,rightLimit,delta)
% numberOfPoints = are the number of iid variables that will be generated
% in order to estimate the pdf
% The histogram that will estimate the pdf will be created using bins in
% the following points :
% leftLimit,leftLimit+delta,leftLimit+2*delta...RightLimit


% first step : generating normal i.i.d random variables using randn 
Xvec1 = randn(1, numberOfPoints);
% we want ln(|Xvec1|) so :
Xvec = log(abs(Xvec1));
bins = [leftLimit:delta:rightLimit];
[yvalues,xvalues]=hist(Xvec,bins);
yvalues = yvalues/(2*(numberOfPoints.*delta)); % we normalize to produce the probability densities
bar(xvalues,yvalues);
z = [ leftLimit-delta/2:delta/10:rightLimit+delta/2];
realPdf = inline('(exp((z - (exp(2*z) / 2))))./sqrt(2*pi)','z');
hold on;
plot(z,realPdf(z),'red');
xlabel('x');
ylabel('f_X(x)');
title(sprintf(' d = %f , #pnts = %d ',delta,numberOfPoints));
end