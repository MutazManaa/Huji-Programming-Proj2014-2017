
%results for part1
fprintf('results for part 1\n');
[TrnErr,VldErr,falsePositive,falseNegative] = Part1();
fprintf('Train error: %.7f \nvalid error: %.7f \n',  TrnErr, VldErr);
%the number of images in valid images that either false-positive or
%false-negative
fprintf('the false-Positive images Numbers: %d\n',falsePositive);
fprintf('the false-negative images Numbers: %d\n',falseNegative);




%results for part 3
fprintf('results for part 3\n');
[TrnErr,VldErr,minLambda,minTrnErr,minVldErr,falsePositive,falseNegative] = Part3();
figure; hold on;
plot(4:0.1:10,TrnErr);
plot(4:0.1:10,VldErr);
title('part3: Train error VS Valid error');
xlabel('logLambda');
ylabel('Error');
legend('Train Error','Valid Error');
fprintf('ideal lambda = 10^%.7f\nTraning error: %.7f\nvalid error: %.7f\n' ,minLambda , minTrnErr, minVldErr);
%the number of images in valid images that either false-positive or
%false-negative
fprintf('the false-Positive images Numbers: %d\n',falsePositive);
fprintf('the false-negative images Numbers: %d\n',falseNegative);



%results of part 5
fprintf('results for part 5\n');
[TrnErr,VldErr,falsePositive,falseNegative] = Part5();
fprintf('Train error: %.7f \nvalid error: %.7f \n',  TrnErr, VldErr);
%the number of images in valid images that either false-positive or
%false-negative
fprintf('the false-Positive images Numbers: %d\n',falsePositive);
fprintf('the false-negative images Numbers: %d\n',falseNegative);



%results for part 6
fprintf('results for part 6\n');
[TrnErr,VldErr,minLambda,minTrnErr,minVldErr,falsePositive,falseNegative] = Part6();
figure; hold on;
plot(-3:0.1:3,TrnErr);
plot(-3:0.1:3,VldErr);
title('part6: Train error VS Valid error');
xlabel('Lambda');
ylabel('Error');
legend('Train Error','Valid Error');
fprintf('ideal lambda = 10^%.7f\nTraning error: %.7f\n,valid error: %.7f\n' ,minLambda , minTrnErr, minVldErr);
%the number of images in valid images that either false-positive or
%false-negative
fprintf('the false-Positive images Numbers: %d\n',falsePositive);
fprintf('the false-negative images Numbers: %d\n',falseNegative);


%result for part 7
fprintf('results for part 7\n');
[TrnErr,VldErr,minT,minTrnErr,minVldErr,falsePositive,falseNegative] = Part7();
figure; hold on;
plot(1:20,TrnErr);
plot(1:20,VldErr);
title('part7: Train error VS Valid error');
xlabel('T');
ylabel('Error');
legend('Train Error','Valid Error');
fprintf('ideal T = %.d\nTraning error: %.7f\n,valid error: %.7f\n' ,minT , minTrnErr, minVldErr);
%the number of images in valid images that either false-positive or
%false-negative
fprintf('the false-Positive images Numbers: %d\n',falsePositive);
fprintf('the false-negative images Numbers: %d\n',falseNegative);


%result for part 8
fprintf('results for part 8\n');
[TrnErr,VldErr,minT,minTrnErr,minVldErr,falsePositive,falseNegative] = Part8();
figure; hold on;
plot(1:20,TrnErr);
plot(1:20,VldErr);
title('part8: Train error VS Valid error');
xlabel('T');
ylabel('Error');
legend('Train Error','Valid Error');
fprintf('ideal T = %.d\nTraning error: %.7f\n,valid error: %.7f\n' ,minT , minTrnErr, minVldErr);
%the number of images in valid images that either false-positive or
%false-negative
fprintf('the false-Positive images Numbers: %d\n',falsePositive);
fprintf('the false-negative images Numbers: %d\n',falseNegative);
