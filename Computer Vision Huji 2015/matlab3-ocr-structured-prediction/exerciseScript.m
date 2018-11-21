load('datasets/letters.mat');

%results for part 4
fprintf('results for part 4\n');
[TrnErr,VldErr,minLambda,minTrnErr,minVldErr,minClassifyValidY] = Part4();
figure; hold on;
plot(-3:0.1:3,TrnErr);
plot(-3:0.1:3,VldErr);
title('part4: Train error VS Valid error');
xlabel('logLambda');
ylabel('Error');
legend('Train Error','Valid Error');
fprintf('ideal lambda = 10^%.7f\nTraning error: %.7f\nvalid error: %.7f\n' ,minLambda , minTrnErr, minVldErr);
figure;
conf_mat(letters.test.Y, minClassifyValidY, unique(letters.test.char));

%results for part 6
fprintf('results for part 6\n');
[errors,VldErr] = Part6();
fprintf('The number of errors for each sequently as demand:%d \nThe validation Error for each one sequently:%.7f\n',...
    errors,VldErr);
