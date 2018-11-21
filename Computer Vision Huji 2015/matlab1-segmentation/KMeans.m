function [C, labels] = KMeans(X, k, max_iter, change_thresh, num_runs)

m = size(X,2);% number of the points
d = size(X,1);% dimension of the points

prevoiusError = inf;
minimalError = inf;
for run = 1:num_runs
    %I choose to init random centers as vectors from X and not zeros
    random = randperm(m);
    random = random(1:k);
    previousCenters = X(:,random);
    
    for iter = 1:max_iter
      distances = pdist2(X',previousCenters')';
      [distances,previousLabels] = min(distances);
      currentError = sqrt(sum(distances.^2));
      if currentError < prevoiusError - change_thresh
          C = zeros(d,k);
          for cluster = 1:k
              nextLabels = X(:,previousLabels == cluster);
              C(:,cluster) = sum(nextLabels,2)/size(nextLabels,2);
          end
          prevoiusError = currentError;
          
      end
    end
    
    if currentError < minimalError
        minimalError = currentError;
       labels = previousLabels;
    end
end


end

