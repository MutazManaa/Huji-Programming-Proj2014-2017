%PART 1 Q.3 --> B:
%we will solve this part in 6 steps:
%(1) we evaluatedthe SNR vector of 100 values in the range 0 --> 40 .
%(2) estimating the LMMSE estimator of X like we did im part A.
%(3)define new vector (decisionX) 4x1, initialize it with 0 , and then update each Xi with or 1 or -1 depending if the i LMMSE estimator >= 0 or not. 
% ~~ now will start the estimating of P_error:
%(4)defining P_error  and initialize it with 0 then add 1 to it  if the decision was wrong and 0 otherwise.
%(5)we adding the estimated P_error of all the Xi and divide by 4.
% NOTE1 : we will do this steps with repetition of 2000 as the Q asked !
%step(6) after we end the 2000 iteration over the same value of SNR, its the time yo divide the sum of all P_errors by 2000 so we got the final
%estimation of the value of P_error for the current SNR value.
%NOTE2 : as in the Q : SNR = 10 * log(1/varOfN) so varOfN =  1/(10^(SNR/10));

%the P_erroeEstimation function:
function PerrorEstimation(H)
% H: matrix 4x4 - that model the entrance to Linear deterministic channel.

%first of all we must compute the transpose matrix of matrix H.
transposeH = H' ;

%doing step (1) using linespace method:
SNR = linspace(0,40);

%caculating the varOfN using NOTE2:
varOfN =  1./(10.^(SNR./10)); 

%define the p_errors vector and initialize it with zero:
P_errors = zeros(100,1);

%define the snrCounter becuse we want to iterate over all the values in snr:
snrCounter = 1;%we cant start from 0 , if we do we got error in varOfN(snrCounter)

%loop to do the 100 iterations:
while snrCounter <= 100
	totalP_error = 0 ;
	%another counter to the 2000 repetition: 
	numOfIteration = 1; 
	while numOfIteration <= 2000
		%step (2):

		%randomely generateing X:
		X = randsrc (4,1);

		%randomely generate N:
		N = randn (4,1);

		%calculating Y:
		Y = (H*X) + N;

		%calculating the LMMSE estimator of x (from y) using result we got in Q.1:
		XLMMSE = transposeH * (inv(H.^2 + varOfN(snrCounter)) * Y);		

		%step(3):

		decisionX = zeros(4,1);
        
		for index = 1:4
			if XLMMSE(index) >= 0
				decisionX(index) = 1;
			else
				decisionX(index) = -1;
			end
		end

		%step(4):

		P_error = 0;
		for i = 1:4
			if X(i) == decisionX(i)
				P_error = P_error + 1;
			end
		end

		%step(5):
			
		P_error = P_error ./ 4;
		totalP_error = totalP_error + P_error;
		numOfIteration = numOfIteration + 1;
	end

	%step(6) :
	
	totalP_error = totalP_error ./ 2000;
	P_errors(snrCounter) =  totalP_error;
	snrCounter = snrCounter + 1;
end

%drawing semi logarithmic graph time:
semilogy(SNR,P_errors);

end

